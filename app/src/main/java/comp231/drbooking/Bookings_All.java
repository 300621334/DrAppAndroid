package comp231.drbooking;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class Bookings_All extends AppCompatActivity implements ICallBackFromDbAdapter
{

    //region Vars
    DbAdapter dbAdapter;
    Model_Booking bModel;
    Gson gson;
    Object[] paramsApiUri;
    ListView listAllAppV;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings_all);
        listAllAppV = (ListView)findViewById(R.id.listAllAppoints);
        gson = new Gson();
        paramsApiUri = new Object[3];


        LoadAllAppoints();

    }

    //ICallBackFromDbAdapter's fn
    @Override
    public void onResponseFromServer(String result, Context ctx)
    {
        //Toast NOT work here??? Null err!! but Log.e works. Even chk (isFinishing) on Actvity doesn't help :
        //So I SOLVED it by passing ctx from this-to-DbAdapter-&-bk-here
        if(!Bookings_All.this.isFinishing())
        {
            Toast.makeText(ctx, "Call Back successful" , Toast.LENGTH_SHORT).show();
        }
        Log.e("Call Back Success", "==== >>>>> Call Back Success");




        //extract Array of Appoints from json-str
        try
        {
            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonObj = jsonArray.getJSONObject(0);//the first & the only obj in array
            String appointsJsonArrayStr = jsonObj.getString("Appointments");
            JSONArray appointsJsonArray = new JSONArray(appointsJsonArrayStr);
            //String allAppJsonObj = appointsJsonArray.getString(0);

            List<Model_Booking> allAppList = new LinkedList<Model_Booking>(); //(List<Model_Booking>) gson.fromJson(allAppJsonObj, new TypeToken<Model_Booking>(){}.getType()); //https://stackoverflow.com/questions/16676913/json-to-listview-in-android-with-gson?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa

            Model_Booking b;

            for (int i = 0; i <appointsJsonArray.length(); i++)
            {
                JSONObject j = appointsJsonArray.getJSONObject(i);

                b = new Model_Booking();
                b.Id_Appointment = Integer.parseInt( j.getString("Id_Appointment") );
                b.Id_User = Integer.parseInt( j.getString("Id_User") );
                b.Clinic =  j.getString("Clinic");
                b.Doctor =  j.getString("Doctor");
                b.AppointmentTime =  j.getString("AppointmentTime");
                b.CreationTime =  j.getString("CreationTime");

                allAppList.add(b);
            }

            //ArrayAdapter<Model_Booking> adapter = new ArrayAdapter<Model_Booking>(ctx, android.R.layout.simple_expandable_list_item_1, allAppList);
            listAllAppV = (ListView)((Activity)ctx).findViewById(R.id.listAllAppoints);
            Booking_Adapter adapter = new Booking_Adapter((Activity) ctx, R.layout.eachbooking, allAppList);

            listAllAppV.setAdapter(adapter);//listAllAppV ref fetched in onCreate becomes NULL in this callBk!!! So get a fresh ref!

            //region Sample json will be like this : an array w only 1 json-obj inside -> that obt has key-values for Model_User & also an array of Appoints
/*
[
    {
        "Appointments": [
            {
                "Id_Appointment": 1,
                "Id_User": 1,
                "Clinic": "test clinic",
                "Doctor": "test dr",
                "AppointmentTime": "2018-02-03 12:00:00.999",
                "CreationTime": "2018-01-01 12:00:00.999"
            },
            {
                "Id_Appointment": 2,
                "Id_User": 1,
                "Clinic": "Address : 940 progress Ave Toronto",
                "Doctor": "Lady Doctor 1",
                "AppointmentTime": "Sun, 20 May 2018 10:27 PM",
                "CreationTime": "Sun, 20 May 2018 10:27 PM"
            },
            {
                "Id_Appointment": 3,
                "Id_User": 1,
                "Clinic": "Address : 940 progress Ave Toronto",
                "Doctor": "Pediatrician",
                "AppointmentTime": "Mon, 21 May 2018 10:32 PM",
                "CreationTime": "Sun, 20 May 2018 10:32 PM"
            }
        ],
        "Id_User": 1,
        "nameOfUser": "John Doe",
        "loginName": "name",
        "pw": "name",
        "address": "name",
        "email": "name@e.e",
        "phone": "111"
    }
]*/
            //endregion


        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }




    }

    private void LoadAllAppoints()
    {
        dbAdapter = new DbAdapter(Bookings_All.this, new Bookings_All());//new Bookings_All() just to give access to DbAdapter to onResponseFromServer()

        //At college WiFi, IP given by Conveyer extendion of VS is different than the one from ipconfig. Use latter ip but use port from Conveyer.
        //paramsApiUri[0] = "http://10.24.72.180:45455/api/values/Appointments/" + "1";
        //paramsApiUri[0] = "http://192.168.1.6:45455/api/values/Appointments/" + "1";//VS extension to allow access to localhost(10.0.2.2 in emulator)https://marketplace.visualstudio.com/items?itemName=vs-publisher-1448185.ConveyorbyKeyoti
        paramsApiUri[0] = VariablesGlobal.API_URI + "/api/values/Appointments/" + "1";
        paramsApiUri[1] = "";//formData not needed for this GET req since user_id is appended to URL
        paramsApiUri[2] = "GET";
        //pass args to AsyncTask to read db
        dbAdapter.execute(paramsApiUri);
    }
}
