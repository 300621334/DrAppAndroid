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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DbAdapter extends AsyncTask<Object, Integer, String>//<args,progress,result>
{
    //region Class Variables
    String jsonResponse, url, formData, httpMethod;
    Context ctx;
    Intent i;
    SharedPreferences prefs;
    ICallBackFromDbAdapter callBk;
    Gson gson;
    //String[] DrNamesList;
    boolean isGettingDrList = false;
    //endregion

    //constructor just to receive ctx needed for TOAST


    //constructor # 1
    public DbAdapter(Context ctx)
    {
        this.ctx = ctx;
        gson = new Gson();
    }
    //constructor # 2 = pass a callBack fn
    public DbAdapter(Context ctx, ICallBackFromDbAdapter callBk)
    {
        this.ctx = ctx;
        this.callBk = callBk;
        gson = new Gson();
    }

    //constructor # 3 = To pass extra data like array-of-Dr names = Java doesn't support optiona params so we're stuch w overloading constructors
    public DbAdapter(Context ctx, String purpose, ICallBackFromDbAdapter callBk)
    {
        switch (purpose)
        {
            case "GetDrNamesArray":
                isGettingDrList = true;
                break;
        }
        this.ctx = ctx;
        gson = new Gson();
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
        //region >>> if fetching list of Drs names
        if(isGettingDrList)
        {
            try
            {
                //https://stackoverflow.com/questions/3395729/convert-json-array-to-normal-java-array?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
                JSONArray jsonArrDrNames = new JSONArray(jsonResponse);
                int len = jsonArrDrNames.length();
                VariablesGlobal.DrNamesList.clear();//clear dummy Dr names like "Un-Known Doctor"
                VariablesGlobal.DrNamesList.add("~~ Please Select a Doctor ~~");
                VariablesGlobal.DrNamesListFiltered.clear();
                VariablesGlobal.DrNamesListFiltered.add("~~ Please Select a Doctor ~~");
                VariablesGlobal.DrProfiles.clear();

                JSONObject jObj;
                Model_DrProfile dr;// = new Model_DrProfile();
                for (int j = 0; j < len; j++)
                {
                    dr = new Model_DrProfile();//MUST create a new dr ea time or else WHOLE List contains the very last dr add()ed

                    jObj = jsonArrDrNames.getJSONObject(j);
                    dr.id_doc      =jObj.getInt("id_doc");
                    dr.Id_User     =jObj.getInt("Id_User");
                    dr.name        =jObj.getString("name");
                    dr.phone       =jObj.getString("phone");
                    dr.email       =jObj.getString("email");
                    dr.specialty   =jObj.getString("specialty");

                    VariablesGlobal.DrNamesList.add(dr.name/* + " (" + dr.specialty + ")"*/);
                    VariablesGlobal.DrNamesListFiltered.add(dr.name/* + " (" + dr.specialty + ")"*/);
                    VariablesGlobal.DrProfiles.add(dr);
                }
                //Collections.reverse(VariablesGlobal.DrProfiles);//reverses the List

                VariablesGlobal.spinAdapter.notifyDataSetChanged();
                callBk.onResponseFromServer(null, ctx);//reset Dr name on spinner - only is coming from list of bookings
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            return;

            /*
            [
    {
        "id_doc": 1,
        "Id_User": 111,
        "name": "Dr.John Doe",
        "phone": "111",
        "email": "doc1@e.e",
        "specialty": "General Physician"
    },
    {
        "id_doc": 2,
        "Id_User": 222,
        "name": "Dr.Elizabeth Dianne",
        "phone": "222",
        "email": "doc2@e.e",
        "specialty": "Gynecologist"
    },
    {
        "id_doc": 3,
        "Id_User": 333,
        "name": "Dr.Miney Moe",
        "phone": "333",
        "email": "doc3@e.e",
        "specialty": "Pediatrician"
    },
    {
        "id_doc": 4,
        "Id_User": 444,
        "name": "Dr.Amelia Byng",
        "phone": "444",
        "email": "doc4@e.e",
        "specialty": "General Physician"
    }
]
            */
        }
        //endregion

        //region >>> chk which activity called for API
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
                //https://www.mkyong.com/java/how-do-convert-java-object-to-from-json-format-gson-api/
                Model_User u = gson.fromJson(jsonResponse, Model_User.class);//jsonResponse is user-obj as JSON
                //


                Toast.makeText(ctx, "User ID: " + u.Id_User + " Login Successful", Toast.LENGTH_LONG).show();
                //save User_Id
                prefs = ctx.getSharedPreferences("prefs" , 0);
                String UserIdStr = jsonResponse.equals("")?"1":String.valueOf(u.Id_User);//if server not up, code returns empty "". So replace User_Id w "1" in such case.
                String roleStr = jsonResponse.equals("")?"1":String.valueOf(u.role);
                prefs.edit().putString("Id_User", UserIdStr).putString("role", roleStr).commit();
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
            /*
            //uName already exists => 0 is returned. If not then create a new user_id & return it
                if(!jsonResponse.equals(0))
                {
                    Button btn = ((Activity)ctx).findViewById(R.id.btnCreateNewUser);
                    if (btn.isEnabled()==false)
                    {
                        Toast.makeText(ctx, jsonResponse + " Login Name is available", Toast.LENGTH_LONG).show();
                        //enable 'Create New User' btn
                        btn.setEnabled(true);
                    }
                }
                */
            break;
            //
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
                    ((Activity)ctx).finish();
                    //----------------------------------------------

                }
                else//Appointment created successfully
                {
                    //go to Dashboard
                    Toast.makeText(ctx, jsonResponse + " Appointment Created", Toast.LENGTH_LONG).show();//jsonResponse is Appoint_id
                    i = new Intent(ctx, Bookings_All.class);
                    ctx.startActivity(i);
                    /*((Activity)ctx).finish();//this causes flash-back to last-activity before Bookings_All is launched bcoz db call takes some tome for latter.
                    so rather use STATIC.instance for all acts & .finish em once newer act onCreates
                    https://stackoverflow.com/a/37248558*/
                }
             break;
            //
            case "Bookings_All"://calling ctx was "Bookings_All"
                callBk.onResponseFromServer(jsonResponse, ctx);
                break;
            //
            case "Settings"://calling ctx was "Settings"
                if(callBk != null)//called from fn btnClk_EditUserProfile()
                {
                    callBk.onResponseFromServer(jsonResponse, ctx);
                }
                else//called from fn btnClk_UpdateUser()
                {
                    Toast.makeText(ctx, jsonResponse + ctx.getClass().getSimpleName() , Toast.LENGTH_LONG).show();
                }
            break;
            //
            default:
                Toast.makeText(ctx, jsonResponse + ctx.getClass().getSimpleName() , Toast.LENGTH_LONG).show();
                break;
        }
        //endregion
    }
}
