package comp231.drbooking;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;

public class DbAdapter extends AsyncTask<Object, Integer, String>//<args,progress,result>
{
    //region Class Variables
    String jsonResult, url;
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

        //obj to send http request to API
        DownloadUrl downloadUrl = new DownloadUrl();

        //API request
        try
        {
            jsonResult = (String) downloadUrl.readUrl(url);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        //return JSON string returned from API
        return jsonResult;
    }

    //process the result(JSON) from API(db)
    @Override
    protected void onPostExecute(String s)//JSON string passed
    {
        Toast.makeText(ctx, jsonResult, Toast.LENGTH_LONG).show();
    }

}
