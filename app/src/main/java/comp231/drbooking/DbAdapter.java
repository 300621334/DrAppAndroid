package comp231.drbooking;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;

public class DbAdapter extends AsyncTask<Object, Integer, String>//<args,progress,result>
{
    //region Class Variables
    String jsonResponse, url, formData, httpMethod;
    Context ctx;
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
        //region (Step-3)go to Dashboard - on successful login
        if(!jsonResponse.equals("0"))
        {
            Toast.makeText(ctx, jsonResponse + " Login Successful", Toast.LENGTH_LONG).show();

            Intent i = new Intent(ctx, Dashboard.class);
            ctx.startActivity(i);
        }
        else
        {
            Toast.makeText(ctx, jsonResponse + " Login Failed", Toast.LENGTH_LONG).show();

        }

        //endregion
    }

}
