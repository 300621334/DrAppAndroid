package comp231.drbooking;
/*
 * By: SHAFIQ-UR-REHMAN
 * Purpose: Async db read/write operations, in background thread
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

public class DbAdapter extends AsyncTask<Object, Integer, String>//<args,progress,result>
{
    //region Class Variables
    String jsonResponse, url, formData, httpMethod;
    Context ctx;
    Intent i;
    SharedPreferences prefs;
    ICallBackFromDbAdapter callBk;
    //endregion

    //constructor just to receive ctx needed for TOAST


    //constructor # 1
    public DbAdapter(Context ctx)
    {
        this.ctx = ctx;
    }
    //constructor # 2 = pass a callBack fn
    public DbAdapter(Context ctx, ICallBackFromDbAdapter callBk)
    {
        this.ctx = ctx;
        this.callBk = callBk;
    }

    //read db via API in bg
    @Override
    protected String doInBackground(Object... objects)
    {
        //get URI for API from params
        url = (String)objects[0];
        formData = (String) objects[1];
        httpMethod = (String) objects[2];

        if(httpMethod.equals("POST"))
        {
            SendToUrl sendToUrl  = new SendToUrl();

            try
            {
                jsonResponse = sendToUrl.sendToUrl(url, formData);//POST to and receive reply from server
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            return jsonResponse;
        }//exe stops here if POST
        

        //obj to send http request to API
        DownloadUrl downloadUrl = new DownloadUrl();

        //API request
        try
        {
            jsonResponse = (String) downloadUrl.readUrl(url);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        //return JSON string returned from API
        return jsonResponse;
    }

    //process the result(JSON) from API(db)
    @Override
    protected void onPostExecute(String s)//JSON string passed
    {
        //region
        switch (ctx.getClass().getSimpleName())
        {
            //Go to Dashboard - on successful login
            case "Login":
            if(jsonResponse.equals("0"))//existing User_Id returned as jsonResponse (non-zero)
            {
                Toast.makeText(ctx, jsonResponse + " Login Failed", Toast.LENGTH_LONG).show();
            }
            else if(jsonResponse.equals(""))
            {
                Toast.makeText(ctx, jsonResponse + " No Response From Server!", Toast.LENGTH_LONG).show();

                //------------For testing without login server---
                //go to Dashboard
                i = new Intent(ctx, Dashboard.class);
                ctx.startActivity(i);
                //----------------------------------------------
            }
            else
            {
                Toast.makeText(ctx, jsonResponse + " Login Successful", Toast.LENGTH_LONG).show();
                //save User_Id
                prefs = ctx.getSharedPreferences("prefs" , 0);
                String UserId_Prefs = jsonResponse.equals("")?"1":jsonResponse;//if server not up, code returns empty "". So replace User_Id w "1" in such case.
                prefs.edit().putString("Id_User", jsonResponse).commit();
                //Go to dashboard
                i = new Intent(ctx, Dashboard.class);
                ctx.startActivity(i);
            }
            break;
            //
            case "NewUserRegister":
                if(jsonResponse.equals("0"))//user already exists
                {

                    //NewUserRegister.AfterAsyncTask(jsonResponse, ctx);//callbk to calling thread
                    Log.e("Server-NewUser ==>>", jsonResponse);
                    //
                    //Snackbar.make(((Activity)ctx)., "Loading Weather", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                    Toast.makeText(ctx.getApplicationContext(), jsonResponse + " Login-Name already exists!", Toast.LENGTH_LONG).show();
                }
                else if(jsonResponse.equals(""))//server NOT up - code returns empty ""
                {
                    Toast.makeText(ctx, jsonResponse + " No Response From Server!", Toast.LENGTH_LONG).show();

                    //------------For testing without login server---
                    //go to Dashboard
                    i = new Intent(ctx, Dashboard.class);
                    ctx.startActivity(i);
                    //----------------------------------------------

                }
                else//User created successfully
                {
                    //go to Dashboard
                    Toast.makeText(ctx, jsonResponse + " User Created", Toast.LENGTH_LONG).show();//jsonResponse is user_id
                    i = new Intent(ctx, Dashboard.class);
                    ctx.startActivity(i);
                }
/*               //uName already exists => 0 is returned. If not then create a new user_id & return it
                if(!jsonResponse.equals(0))
                {
                    Button btn = ((Activity)ctx).findViewById(R.id.btnCreateNewUser);
                    if (btn.isEnabled()==false)
                    {
                        Toast.makeText(ctx, jsonResponse + " Login Name is available", Toast.LENGTH_LONG).show();
                        //enable 'Create New User' btn
                        btn.setEnabled(true);
                    }
                }*/
            break;
            case "BookingDetails":
                if(jsonResponse.equals("0"))//booking time unavailable
                {
                    Toast.makeText(ctx.getApplicationContext(), jsonResponse + " Appointment unavailable, Choose another time!", Toast.LENGTH_LONG).show();
                }
                else if(jsonResponse.equals(""))//server NOT up - code returns empty ""
                {
                    Toast.makeText(ctx, jsonResponse + " No Response From Server!", Toast.LENGTH_LONG).show();

                    //------------For testing go to ALL bookings if server is not up---
                    //go to Dashboard
                    i = new Intent(ctx, Bookings_All.class);
                    ctx.startActivity(i);
                    //----------------------------------------------

                }
                else//Appointment created successfully
                {
                    //go to Dashboard
                    Toast.makeText(ctx, jsonResponse + " Appointment Created", Toast.LENGTH_LONG).show();//jsonResponse is Appoint_id
                    i = new Intent(ctx, Bookings_All.class);
                    ctx.startActivity(i);
                }

                break;
                case "Bookings_All":
                    callBk.onResponseFromServer(jsonResponse, ctx);
                    break;
            default:
                Toast.makeText(ctx, jsonResponse + ctx.getClass().getSimpleName() , Toast.LENGTH_LONG).show();
                break;
        }
        //endregion
    }
}
