package comp231.drbooking;
/*
 * By: SHAFIQ-UR-REHMAN
 * Purpose: User can login or register
 */
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.Gson;

import java.util.Map;
/*Model_User JSON sample:
{"role":"1","pw":"DnVELRcAZH97k+lj5ivzYQ\u003d\u003d","loginName":"name","isLoggedIn":false,"Id_User":0}
*/
public class Login extends BaseActivity
{

    //region >>> Variables
    boolean isFaculty = false, isFacultyRadChecked = true;
    RadioGroup radGp;
    RadioButton radChecked;
    EditText uNameView, uPassView;
    boolean hasAccount = false;
    SharedPreferences prefs;
    Intent intent;
    Map<String, ?> allPrefs;
    int numOfPrefs, uName_uPass_pairs;// counter = 1;
    //int PLACE_PICKER_REQUEST = 1;
    String uName, uPass, uNameEntered, uPassEntered, formData;
    //double longitude,latitude;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login or Create New Account");
        //get references
        uNameView = (EditText) findViewById(R.id.txtLoginName);
        uPassView = (EditText) findViewById(R.id.txtLoginPass);

        ////if no permission then assign some default location like downtown or Centennial college
        //String dummyForBreakPoint = "";
    }

    private void getCurrentLoc() //grant permissions for "Location" from phone/emulator
    {
/*        //https://stackoverflow.com/questions/2227292/how-to-get-latitude-and-longitude-of-the-mobile-device-in-android
        //More detailed Criteris : https://stackoverflow.com/questions/2699215/get-the-current-location-gps-wifi
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ////if no permission then assign some default location like downtown or Centennial college
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //location obj is NULL in newly installed app bcoz no "Last Known Loc" there. So turn Google Maps inbuilt app & click target icon to get at least one loc in memory
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        Toast.makeText(this, longitude +"---"+ latitude, Toast.LENGTH_LONG).show();*/
    }

    //Login btn clk
    public void clk_Login(View view)
    {
        //get form data into class
        Model_User uModel = new Model_User();
        uModel.loginName = uNameView.getText().toString();
        //encrypt pw
        try {
            uModel.pw = AESCrypt.encrypt(uPassView.getText().toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        //convert obj to JSON str: https://github.com/google/gson/blob/master/README.md
        Gson gson = new Gson();
        formData = gson.toJson(uModel);


        //region (Step-1)Send Object[ApiUri , params] to AsyncTask to read DB to verify login+pw
            //init AsyncTack class
            DbAdapter dbAdapter = new DbAdapter(this);

            //create API's URI
            Object paramsApiUri[] = new Object[3];//[uri , form-data , Http-Method e.g POST ]

            //API's URI gets access issues
            //https://stackoverflow.com/questions/6760585/accessing-localhostport-from-android-emulator?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa

        //At college WiFi, IP given by Conveyer extendion of VS is different than the one from ipconfig. Use latter ip but use port from Conveyer.
            //paramsApiUri[0] = "http://localhost:50036/api/values/login";
            //paramsApiUri[0] = "http://10.0.2.2:45455/api/values/login"; //emulator uses this
            //paramsApiUri[0] = "http://10.24.72.180:45455/api/values/login?login=xxx&pw=xxx";//VS extension to allow access to localhost(10.0.2.2 in emulator)https://marketplace.visualstudio.com/items?itemName=vs-publisher-1448185.ConveyorbyKeyoti
            //paramsApiUri[0] = "https://jsonplaceholder.typicode.com/posts/3";//works
        paramsApiUri[0] = VariablesGlobal.API_URI + "/api/values/login";
        paramsApiUri[1] = formData;
            paramsApiUri[2] = "POST";


        //pass args to AsyncTask to read db
            dbAdapter.execute(paramsApiUri);

            //Store user_id in Shared-Prefs

        //endregion

        //region (Step-2)verify that login+pw are correct
            //
            //
        //endregion

/*        //region (Step-3)go to Dashboard - on successful login
            Intent i = new Intent(this, Dashboard.class);
            startActivity(i);
        //endregion*/



        /*getCurrentLoc();

        //top-left & bottom-right corners of what PlacePicker will display
        //use this site to get coords : https://www.latlong.net
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(new LatLng(43.790272, -79.234127))
                .include(new LatLng(43.759768, -79.225224))
                .build();

        //filter places e.g. clinics etc
        PlaceFilter placeFilter = new PlaceFilter();
        placeFilter.equals(TYPE_DOCTOR | TYPE_HOSPITAL);//int Place_IDs permitted : https://developers.google.com/android/reference/com/google/android/gms/location/places/Place



        //go to PlacePicker : https://www.youtube.com/watch?v=Rh9x90lqPHc
        PlacePicker.IntentBuilder iBuilder = new PlacePicker.IntentBuilder(); //compile 'com.google.android.gms:play-services-places:9.2.0'
        //iBuilder.setLatLngBounds(MapUtils.getLatLngBounds(new LatLng((double) latitude, (double) longitude)));
        Intent i;

        try
        {
            i = iBuilder
                    .setLatLngBounds(bounds)
                    .build(Login.this);


            startActivityForResult(i, PLACE_PICKER_REQUEST);
        }
        catch (GooglePlayServicesRepairableException e)
        {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e)
        {
            e.printStackTrace();
        }*/
    }

    //get PlacePicker returned data
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)//err if no 'protected'
    {
 /*       if(requestCode == PLACE_PICKER_REQUEST )//i made this constant so we can id returned result w our request
        {
            if(resultCode==RESULT_OK)//'RESULT_OK' is default constant of activity class
            {
                Place place = PlacePicker.getPlace(this, data);
                String clinicAddress = String.format("Place: %s", place.getAddress());
                String clinicName = String.format("Place: %s", place.getName());

                getSupportActionBar().setTitle(clinicAddress);


            }


        }*/
    }


    //Create a new user account
    public void clk_NewUserRegister(View view)
    {
        Intent i = new Intent(this, NewUserRegister.class);
        startActivity(i);
    }
}
