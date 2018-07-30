package comp231.drbooking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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

public class AdminDashboard extends BaseActivity implements ICallBackFromDbAdapter {
    Object[] paramsApiUri;
    EditText etUserName;
    String stUserName;
    DbAdapter dbAdapter;
    ListView lvUserList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        paramsApiUri = new Object[3];
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        onSearchClick(null);
    }

    public void onSearchClick(View v)
    {
        etUserName = (EditText) findViewById(R.id.etUserName);
        stUserName = etUserName.getText().toString();
        if(stUserName.trim().isEmpty())
        {
            return;
        }
        dbAdapter = new DbAdapter(AdminDashboard.this, new AdminDashboard());//new Bookings_All() just to give access to DbAdapter to onResponseFromServer()

        //At college WiFi, IP given by Conveyer extendion of VS is different than the one from ipconfig. Use latter ip but use port from Conveyer.
        //paramsApiUri[0] = "http://10.24.72.180:45455/api/values/Appointments/" + "1";
        //paramsApiUri[0] = "http://192.168.1.6:45455/api/values/Appointments/" + "1";//VS extension to allow access to localhost(10.0.2.2 in emulator)https://marketplace.visualstudio.com/items?itemName=vs-publisher-1448185.ConveyorbyKeyoti
        paramsApiUri[0] = VariablesGlobal.API_URI + "/api/values/searchUserByName/" + stUserName;
        paramsApiUri[1] = "";//formData not needed for this GET req since user_id is appended to URL
        paramsApiUri[2] = "GET";
        //pass args to AsyncTask to read db
        dbAdapter.execute(paramsApiUri);

    }

    @Override
    public void onResponseFromServer(String result, Context ctx)
    {
        //Toast NOT work here??? Null err!! but Log.e works. Even chk (isFinishing) on Actvity doesn't help :
        //So I SOLVED it by passing ctx from this-to-DbAdapter-&-bk-here
        if (!AdminDashboard.this.isFinishing()) {
            Toast.makeText(ctx, "Call Back successful", Toast.LENGTH_SHORT).show();
        }
        Log.e("Call Back Success", "==== >>>>> Call Back Success");


        //extract Array of Appoints from json-str
        try {
            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonObj = jsonArray.getJSONObject(0);//the first & the only obj in array
            String userJsonArrayStr = jsonObj.getString("UsersFound");
            JSONArray userJsonArray = new JSONArray(userJsonArrayStr);
            String allUserJsonObj = userJsonArray.getString(0);

            List<Model_User> allUserList = new LinkedList<Model_User>(); //(List<Model_Booking>) gson.fromJson(allAppJsonObj, new TypeToken<Model_Booking>(){}.getType()); //https://stackoverflow.com/questions/16676913/json-to-listview-in-android-with-gson?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa

            Model_User usr;

            for (int i = 0; i < userJsonArray.length(); i++) {
                JSONObject j = userJsonArray.getJSONObject(i);

                usr = new Model_User();
                usr.loginName = j.getString("loginName");
                usr.nameOfUser = j.getString("nameOfUser");
                usr.Id_User = j.getInt("Id_User");


                //list for Docs has 1 extra prop "PatientName". List for patients doesn't. See sample JSON fo Docs below:
                //if(null != j.getString("PatientName"))//this always throw excep for patient's json bcos NO "PatientName" key - so ch for key using "has"
//                if (j.has("PatientName")) {
//                    b.User = j.getString("PatientName");//name of patient
//                }
                //region sample JSON fo Docs below - NOTE it's an ARRAY : API : http://drappapi.azurewebsites.net/api/values/AppointmentsForDr/111
/*
                [
                    {
                        "Appointments": [
                            {
                                "Id_Appointment": 1,
                                "Id_User": 1,
                                "Clinic": "test clinic",
                                "Doctor": "Lady Doctor 1",
                                "AppointmentTime": "Wed, 30 May 2018 11:27 AM",
                                "CreationTime": "Wed, 30 May 2018 11:27 AM",
                                "PatientName": "John Doe"
                            },
                            {
                                "Id_Appointment": 2,
                                "Id_User": 1,
                                "Clinic": "Address : 940 progress Ave Toronto",
                                "Doctor": "Lady Doctor 1",
                                "AppointmentTime": "Sun, 20 May 2018 10:27 PM",
                                "CreationTime": "Sun, 20 May 2018 10:27 PM",
                                "PatientName": "John Doe"
                            },
                            {
                                "Id_Appointment": 6,
                                "Id_User": 2,
                                "Clinic": "Address : 940 progress Ave Toronto",
                                "Doctor": "Lady Doctor 1",
                                "AppointmentTime": "Sat, 30 Jun 2018 07:14 PM",
                                "CreationTime": "Thu, 31 May 2018 07:14 PM",
                                "PatientName": "Name1"
                            }
                        ]
                    }
                ]
*/
                //endregion
                allUserList.add(usr);
            }

            //ArrayAdapter<Model_Booking> adapter = new ArrayAdapter<Model_Booking>(ctx, android.R.layout.simple_expandable_list_item_1, allAppList);
            lvUserList = (ListView) ((Activity) ctx).findViewById(R.id.lvUserList);
            User_Adapter adapter = new User_Adapter((Activity) ctx, R.layout.eachuser, allUserList);

            lvUserList.setAdapter(adapter);//listAllAppV ref fetched in onCreate becomes NULL in this callBk!!! So get a fresh ref!

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

    public void onNewUserClick(View view)
    {
        i = new Intent(this, NewUserRegister.class);
        startActivity(i);
    }
}
