package comp231.drbooking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class DbAdapter extends AsyncTask<Object, Integer, String>//<args,progress,result>
{
    //region Class Variables
    String jsonResponse, url, formData, httpMethod;
    Context ctx;
    Intent i;
    //endregion

    //constructor just to receive ctx needed for TOAST


    public DbAdapter(Context ctx)
    {
        this.ctx = ctx;
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
            }
            else
            {
                Toast.makeText(ctx, jsonResponse + " Login Successful", Toast.LENGTH_LONG).show();

                i = new Intent(ctx, Dashboard.class);
                ctx.startActivity(i);
            }
            break;
            //
            case "NewUserRegister":
                if(jsonResponse.equals("0"))
                {

                    //NewUserRegister.AfterAsyncTask(jsonResponse, ctx);//callbk to calling thread
                    Log.e("Server Returned ==>>", jsonResponse);
                    //
                    //Snackbar.make(((Activity)ctx)., "Loading Weather", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                    Toast.makeText(ctx.getApplicationContext(), jsonResponse + " Login-Name already exists!", Toast.LENGTH_LONG).show();
                }
                else if(jsonResponse.equals(""))
                {
                    Toast.makeText(ctx, jsonResponse + " No Response From Server!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    //go to Dashboard
                    Toast.makeText(ctx, jsonResponse + " User Created", Toast.LENGTH_LONG).show();
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
            default:
                Toast.makeText(ctx, jsonResponse + ctx.getClass().getSimpleName() , Toast.LENGTH_LONG).show();
                break;
        }
        //endregion
    }
}
