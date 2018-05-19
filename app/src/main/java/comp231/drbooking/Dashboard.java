package comp231.drbooking;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceFilter;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import static com.google.android.gms.location.places.Place.TYPE_DOCTOR;
import static com.google.android.gms.location.places.Place.TYPE_HOSPITAL;

public class Dashboard extends AppCompatActivity {

    private static final String TAG = "Catch Block says: ";
    //region >>> Class Variables
    int PLACE_PICKER_REQUEST = 1;
    double longitude,latitude;
    //endregion


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }


    public void clk_newAppMap(View v)
    {
        //go to MapActivity
        Intent i = new Intent(this, MapsActivity.class);
        startActivity(i);

    }

    public void clk_newAppPlacePicker(View view)
    {
        getCurrentLoc();

        //top-left & bottom-right corners of what PlacePicker will display
        //use this site to get coords : https://www.latlong.net
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(new LatLng(43.790272, -79.234127))
                .include(new LatLng(43.759768, -79.225224))
                .build();

        //filter places e.g. clinics etc
        PlaceFilter placeFilter = new PlaceFilter();
        placeFilter.equals(TYPE_DOCTOR | TYPE_HOSPITAL);//int Place_IDs permitted : https://developers.google.com/android/reference/com/google/android/gms/location/places/Place

        //region Only Autocomplete widget - NO map
        //https://en.proft.me/2017/05/18/android-google-places-api-tutorial/

/*        try {
            PlaceAutocomplete.IntentBuilder builder = new PlaceAutocomplete.
                    IntentBuilder(PlaceAutocomplete.MODE_OVERLAY);
            Intent intent = builder.build(this);
            startActivityForResult(intent, PLACE_PICKER_REQUEST);
        } catch ( GooglePlayServicesRepairableException e) {
            Log.d(TAG, "GooglePlayServicesRepairableException thrown");
        } catch ( GooglePlayServicesNotAvailableException e) {
            Log.d(TAG, "GooglePlayServicesNotAvailableException thrown");
        }*/
        //endregion


        //region PlacePicker with map - but NO filtering of results supported yet
        //go to PlacePicker : https://www.youtube.com/watch?v=Rh9x90lqPHc
        PlacePicker.IntentBuilder iBuilder = new PlacePicker.IntentBuilder(); //compile 'com.google.android.gms:play-services-places:9.2.0'
        //iBuilder.setLatLngBounds(MapUtils.getLatLngBounds(new LatLng((double) latitude, (double) longitude)));
        Intent i;

        //region AUtocpmplete Filter ONLY can work with autocom widget - NOT w PlacePicker
/*        //set filter type by country 'CANADA'
        //https://stackoverflow.com/questions/30852349/how-to-set-types-in-google-place-placepicker-in-android?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa#_=_
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(50) //50 = hospitals //or : .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES) //codes here : https://developers.google.com/android/reference/com/google/android/gms/location/places/Place
                .setCountry("CA")
                .build();*/
        //endregion

        try
        {
            i = iBuilder
                    .setLatLngBounds(bounds)
                    .build(Dashboard.this);


            startActivityForResult(i, PLACE_PICKER_REQUEST);
        }
        catch (GooglePlayServicesRepairableException e)
        {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e)
        {
            e.printStackTrace();
        }
        //endregion
    }

    public void clk_allAppointments(View view)
    {
    }


    //
    private void getCurrentLoc() //grant permissions for "Location" from phone/emulator
    {
        //https://stackoverflow.com/questions/2227292/how-to-get-latitude-and-longitude-of-the-mobile-device-in-android
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
        Toast.makeText(this, longitude +"---"+ latitude, Toast.LENGTH_LONG).show();
    }
    //
    //get PlacePicker returned data
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)//err if no 'protected'
    {
        if(requestCode == PLACE_PICKER_REQUEST )//i made this constant so we can id returned result w our request
        {
            if(resultCode==RESULT_OK)//'RESULT_OK' is default constant of activity class
            {
                Place place = PlacePicker.getPlace(this, data);
                String clinicAddress = String.format("Place: %s", place.getAddress());
                String clinicName = String.format("Place: %s", place.getName());

                //display selected address
                Toast.makeText(this, "Name: " + clinicName
                + "\nAddress: " + clinicAddress, Toast.LENGTH_LONG).show();

                getSupportActionBar().setTitle(clinicAddress);


            }


        }
    }
}
