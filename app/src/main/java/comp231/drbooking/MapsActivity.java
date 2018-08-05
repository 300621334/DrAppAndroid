package comp231.drbooking;
/*
 * By: SHAFIQ-UR-REHMAN
 * Purpose: Displays the map. If GPS is on then Map centers itself.
 * User can search nearby clinics on this activity.
 */

/*
To use maps in WebView on devices wout Play-Services => use Play Service "Library" : https://stackoverflow.com/questions/29747390/develop-google-map-program-without-using-google-play-service
or AirBnb library : https://github.com/airbnb/AirMapView/blob/master/README.md#how-to-use
*/

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.IntentService;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;

import java.io.IOException;
import java.util.List;
/*JSON result looks like : change radius=2000 to see more results.
https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=43.7811609,-79.229795&radius=1000&type=hospital&sensor=true&key=AIzaSyBDV5k9vHipVPgZimt0zMZnodMHmvWXa3Q
{
   "html_attributions" : [],
   "results" : [
      {
         "geometry" : {
            "location" : {
               "lat" : 43.7775918,
               "lng" : -79.231882
            },
            "viewport" : {
               "northeast" : {
                  "lat" : 43.7789138302915,
                  "lng" : -79.2306659197085
               },
               "southwest" : {
                  "lat" : 43.7762158697085,
                  "lng" : -79.23336388029151
               }
            }
         },
         "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/doctor-71.png",
         "id" : "ddda02d9b3a7f2a7a6ce41afb2e32792cf68e4ae",
         "name" : "Walk In Medical Clinic",
         "place_id" : "ChIJW2f_2vfQ1IkRJSptdou-1Y4",
         "reference" : "CmRSAAAA6u_OJYX7FtmxO8QrPfeJ6ShT2m-fZtK8uOYTEv3bV0qq5awZ4AQx3QtY7vhxPReUoQgv5GUfgWyLAIgXmDH9_YS7ZhocjeqDk9J6JlPWJrwQjmL1EB0odwu_qVUP-r9qEhB7my7GrdwGGZ_Z9zEykF4CGhRZi_9oTh_aqlXeWBtggYajM4_DuQ",
         "scope" : "GOOGLE",
         "types" : [ "hospital", "point_of_interest", "establishment" ],
         "vicinity" : "1209 Markham Road, Scarborough"
      }
   ],
   "status" : "OK"
}
*/
//http://androidmastermind.blogspot.ca/2016/06/android-google-maps-show-current.html
public class MapsActivity extends BaseActivity implements OnMapReadyCallback, LocationListener {

    //region >>> Vars
    int PROXIMITY_RADIUS = 2000, GPS_ENABLE_REQUEST = 1, ACCESS_FINE_LOCATION_REQUEST = 2;
    double longitude, latitude;
    private GoogleMap mMap;
    private static final String TAG = "CurrentLocation";
    protected LocationManager locationManager;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    //Bitmap bmp;
    InfoWinAdapter infoWinAdapter;
    public static MapsActivity instance = null;
    AlertDialog mGPSDialog;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        //chk if service available
        if (!isGooglePlayServicesAvailable()) {
            return;
        }
        /*(ctrl+2) => chk for permission
        * (ctrl+3) => chk for GPS
        */
        isLocPermissionGiven(); //ctrl + 2
        isGpsOn(); //ctrl + 4

        //inflate layout
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        //-----------custom Info Window----------
        //bmp = null; //BitmapFactory.decodeResource(getResources(), R.id.);//if using adapter
        infoWinAdapter = new InfoWinAdapter(getLayoutInflater()/*, bmp*/);//custom info window adapter

    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//
        displayCurrentLocation(mMap);


        /*// Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        */
    }

    private void displayCurrentLocation(GoogleMap mMap)
    {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Criteria criteria = new Criteria();

        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(bestProvider);//returns NULL after re-installing app. Need to enable GPS & then surf Inbuilt Maps for few locations then try DrApp again.

        if (location != null) {
            onLocationChanged(location);

        }

        locationManager.requestLocationUpdates(bestProvider, 1800000, 0, this);//originaly it was 20sec, but it was adding a new marker q time! so I changed it to 30min

    }

    private boolean isGooglePlayServicesAvailable()
    {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                Toast.makeText(this, "This device is not supported.", Toast.LENGTH_LONG);
                finish();
            }
            return false;
        }
        return true;
    }

    @Override    public void onLocationChanged(Location location)
    {
         latitude = location.getLatitude();
         longitude = location.getLongitude();

        LatLng currentLocation = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(currentLocation).title("My Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    private void isLocPermissionGiven()
    {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_REQUEST);
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, this);
    }

    private void isGpsOn()
    {
        //https://stackoverflow.com/questions/7713478/how-to-prompt-user-to-enable-gps-provider-and-or-network-provider/7713511?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
        try
        {
            int off = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);//import android.provider.Settings; <> there's a comp231.DrApp.Settings too w doesn't have .Secure in it
            if(off == 0)//LOCATION_MODE will be LOCATION_MODE_OFF if GPS is off, which will return value 0.
            {
                /*Intent turnOnGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(turnOnGPS);*/
                onProviderDisabled(LocationManager.GPS_PROVIDER);
            }
        }
        catch (Settings.SettingNotFoundException e)
        {
            e.printStackTrace();
        }

    }

    @Override//GPS activation - watch for GPS being disabled
    public void onProviderDisabled(String provider)
    {

        //https://stackoverflow.com/questions/7713478/how-to-prompt-user-to-enable-gps-provider-and-or-network-provider/7713511?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
        if(provider.equals(LocationManager.GPS_PROVIDER))
        {
           showGPSDiabledDialog();
        }
    }

    private void showGPSDiabledDialog()//called as soon as previously enabled GPS is disabled - called from onProviderDisabled()
    {
        Log.d(TAG, "howGPSDiabledDialog() ===>>> entered ");
        //https://stackoverflow.com/questions/7713478/how-to-prompt-user-to-enable-gps-provider-and-or-network-provider/7713511?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
        AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(this);
        dlgBuilder.setTitle("GPS disabled !").setMessage("GPS is disabled, in order to use the application properly you need to enable GPS of your device")
                .setPositiveButton("Enable GPS", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        //open location settings //ctrl+F11 fo neumonic bookmark
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS) , GPS_ENABLE_REQUEST);
                        //callbk => onActivityResult()
                    }
                })
        .setNegativeButton("No, just exit", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                //MapsActivity.this.finish();
                Toast.makeText(MapsActivity.this, "You won't be able to see nearby clinics", Toast.LENGTH_SHORT).show();
            }
        });
        Log.d(TAG, "howGPSDiabledDialog() ===>>> btns done ");

        mGPSDialog  = dlgBuilder.create();

        Log.d(TAG, "howGPSDiabledDialog() ===>>> going to dosplay dlg ");
        mGPSDialog.show();
        Log.d(TAG, "howGPSDiabledDialog() ===>>> dlg showing");


    }

    public void onClick(View v)
    {
        //Toast.makeText(this, "onClick fn entered", Toast.LENGTH_LONG).show();

        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData(this);
        Object dataTransfer[] = new Object[2];//will hold 2 objs


        //
        switch (v.getId())
        {
            case R.id.B_search:
                EditText tf_location =  findViewById(R.id.TF_location);
                String location = tf_location.getText().toString();
                List<Address> addressList;

                if(!location.equals(""))
                {
                    Geocoder geocoder = new Geocoder(this);

                    try {
                        addressList = geocoder.getFromLocationName(location, 1);

                        if(addressList != null)
                        {
                            for(int i = 0;i<addressList.size();i++)
                            {
                                latitude = addressList.get(i).getLatitude();
                                longitude = addressList.get(i).getLongitude();
                                //https://developer.android.com/reference/android/location/Address
                                /*
                                    street_address = getThoroughfare() e.g. "1600 Ampitheater Parkway"
                                    city = addresses.get(0).getLocality(); e.g. Toronto
                                    //--------------------------------------
                                    address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                    city = addresses.get(0).getLocality();
                                    state = addresses.get(0).getAdminArea();
                                    country = addresses.get(0).getCountryName();
                                    postalCode = addresses.get(0).getPostalCode();
                                    knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
                                    */

                                LatLng latLng = new LatLng(addressList.get(i).getLatitude() , addressList.get(i).getLongitude());
                                MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions.position(latLng);
                                markerOptions.title(location);
                                markerOptions.snippet(addressList.get(i).getFeatureName() + " " + addressList.get(i).getThoroughfare() +" "+ addressList.get(i).getLocality() +" "+ addressList.get(i).getAdminArea()+" "+addressList.get(i).getCountryName());
                                mMap.addMarker(markerOptions);

                                //---------custom Info Window-------
                                //https://stackoverflow.com/questions/18567563/google-map-v2-custom-infowindow-with-two-clickable-buttons-or-imageview?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
                                //https://developers.google.com/maps/documentation/android-sdk/infowindows




                                mMap.setInfoWindowAdapter(infoWinAdapter);
                                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                    @Override
                                    public void onInfoWindowClick(Marker marker)
                                    {
                                        Intent i = new Intent(getApplicationContext(), BookingDetails.class);
                                        i.putExtra("infoWinTitle", marker.getTitle());//address included here!!
                                        //i.putExtra("infoWinAddress", marker.getSnippet());//null !!!

                                        startActivity(i);


                                    }
                                });
                                //----------------------------

                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.B_hopistals:
                mMap.clear();
                String hospital = "hospital";
                String url = getUrl(latitude, longitude, hospital);
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbyPlacesData.execute(dataTransfer);//AsyncTask.execute();

                Toast.makeText(this, "Showing nearby hospitals", Toast.LENGTH_LONG).show();
                break;
                /*If quota limits reached, get err from Google Places like:
                    {
                   "error_message" : "You have exceeded your daily request quota for this API.",
                   "html_attributions" : [],
                   "results" : [],
                   "status" : "OVER_QUERY_LIMIT"
                    }
                  */

            case R.id.B_restaurants:
                mMap.clear();
                String school = "school";
                url = getUrl(latitude, longitude, school);
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbyPlacesData.execute(dataTransfer);//AsyncTask.execute();

                Toast.makeText(this, "Showing nearby schools", Toast.LENGTH_LONG).show();

                break;
            case R.id.B_schools:
                mMap.clear();
                String restaurants = "restaurant";
                url = getUrl(latitude, longitude, restaurants);
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbyPlacesData.execute(dataTransfer);//AsyncTask.execute();

                Toast.makeText(this, "Showing nearby restaurants", Toast.LENGTH_LONG).show();

                break;
        }
    }

    /*//custom listener for InfoWindow
    class MyClkListener implements View.OnClickListener
    {
        String userIdStr;
        //ViewHolder vh;
        Context ctx;
        int userId;

        //constructor to pass user ID to listener for each btn
        public MyClkListener(int userId)
        {
            this.userIdStr = String.valueOf(userId);
            this.userId = userId;
        }

        @Override
        public void onClick(View view)
        {
            ctx = view.getContext();

            Intent i = new Intent(ctx, BookingDetails.class);
            SharedPreferences prefs = ctx.getSharedPreferences("login",0);
            prefs.edit().putInt("userId", userId).commit();
            ctx.startActivity(i);
            ((Activity)ctx).finish();
        }
    }*/

    //fn
    private String getUrl(double latitude, double longitude, String nearbyPlace)//
    {
        //https://developers.google.com/places/web-service/search
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location=" + latitude + "," + longitude);
        googlePlaceUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type=" + nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key=" + "AIzaSyA3WABbO18GPtvg3VTl-TosotiD6Zba5kE");//diff key for Place search than from Map API

        Log.d("MapsActivity", "url = "+googlePlaceUrl.toString());
        //https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=43.7839848,-79.2330439&radius=2000&type=hospital&sensor=true&key=AIzaSyAn5RWheV7c0-dk2USdW8ZOQn6rP3tojZ4
        //https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=0.0,0.0&radius=10000&type=hospital&sensor=true&key=AIzaSyC0NcrWiTD7AcTjHzygF3MCnDFcCltr_88
        //https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=0.0,0.0&radius=10000&type=hospital&sensor=true&key=AIzaSyBDV5k9vHipVPgZimt0zMZnodMHmvWXa3Q

        return googlePlaceUrl.toString();

    }

    @Override//(ctrl+1) callbk from Location settings screen (to create bookmark ctrl+F11)
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //https://stackoverflow.com/questions/7713478/how-to-prompt-user-to-enable-gps-provider-and-or-network-provider/7713511?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
        if(requestCode == GPS_ENABLE_REQUEST)
        {
            if (locationManager != null)
            {
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            }
            if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            {
                showGPSDiabledDialog();
            }
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == ACCESS_FINE_LOCATION_REQUEST)
        {
            for (int i = 0; i < permissions.length; i++)
            {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if(permission.equals(Manifest.permission.ACCESS_FINE_LOCATION))
                {
                    if(grantResult == PackageManager.PERMISSION_GRANTED)
                    {
                    }
                    else
                    {
                        Toast.makeText(MapsActivity.this, "You won't be able to see nearby clinics", Toast.LENGTH_SHORT).show();
                        //requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_REQUEST);
                    }

                }

            }
        }
    }
}

