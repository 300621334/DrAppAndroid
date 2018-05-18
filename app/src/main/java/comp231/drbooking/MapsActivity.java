package comp231.drbooking;

import android.app.Activity;
import android.content.Context;
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
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    int PROXIMITY_RADIUS = 2000;
    double longitude, latitude;
    private GoogleMap mMap;
    private static final String TAG = "CurrentLocation";
    protected LocationManager locationManager;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    //Bitmap bmp;
    InfoWinAdapter infoWinAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //chk if service available
        if (!isGooglePlayServicesAvailable()) {
            return;
        }
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

    private void displayCurrentLocation(GoogleMap mMap) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
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
        Location location = locationManager.getLastKnownLocation(bestProvider);

        if (location != null) {
            onLocationChanged(location);

        }

        locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);

    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override    public void onLocationChanged(Location location) {
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

    @Override
    public void onProviderDisabled(String s) {

    }

    public void onClick(View v)
    {
        //Toast.makeText(this, "onClick fn entered", Toast.LENGTH_LONG).show();

        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
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
                        addressList = geocoder.getFromLocationName(location, 5);

                        if(addressList != null)
                        {
                            for(int i = 0;i<addressList.size();i++)
                            {
                                LatLng latLng = new LatLng(addressList.get(i).getLatitude() , addressList.get(i).getLongitude());
                                MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions.position(latLng);
                                markerOptions.title(location);
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
                                        i.putExtra("infoWinAddress", marker.getSnippet());//null !!!

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
        googlePlaceUrl.append("&key=" + "AIzaSyBDV5k9vHipVPgZimt0zMZnodMHmvWXa3Q");//diff key for Place search than from Map API

        Log.d("MapsActivity", "url = "+googlePlaceUrl.toString());
        //https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=0.0,0.0&radius=10000&type=hospital&sensor=true&key=AIzaSyBDV5k9vHipVPgZimt0zMZnodMHmvWXa3Q

        return googlePlaceUrl.toString();

    }
}
