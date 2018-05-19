package comp231.drbooking;

import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GetNearbyPlacesData extends AsyncTask<Object, String, String>//args,progress,result
{
    //Class Variables
    String googlePlacesData, url;
    GoogleMap mMap;

    @Override
    protected String doInBackground(Object... objects)
    {
        mMap = (GoogleMap)objects[0];
        url = (String)objects[1];

        DownloadUrl downloadUrl = new DownloadUrl();

        try
        {
            googlePlacesData = downloadUrl.readUrl(url);//get JSON string
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return googlePlacesData;
    }

    // 'ctrl + O' to generate override fns
    @Override
    protected void onPostExecute(String s)//JSON string passed
    {
        List<HashMap<String,String>> nearbyPlacesList = null;
        DataParser parser = new DataParser();
        nearbyPlacesList = parser.parse(s);//parse() gets List<HashMaps<ea Place>> by passing JSON str from Google to => getPlaces() gets HashMap<ea place> by passing ea json-array-str to => getPlace()
        showNearbyPlaces(nearbyPlacesList);


        //super.onPostExecute(s);
    }

    //Add markers to map to show ALL places - move camera to marker
    private void showNearbyPlaces(List<HashMap<String,String>> nearbyPlacesList)
    {
        for (int i = 0; i < nearbyPlacesList.size(); i++)
        {
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String,String> googlePlace = nearbyPlacesList.get(i);
            String placeName = googlePlace.get("placeName");
            String vicinity = googlePlace.get("vicinity");
            double lat = Double.parseDouble(googlePlace.get("latitude"));
            double lng = Double.parseDouble(googlePlace.get("longitude"));

            LatLng latLng  = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName + " : " + vicinity);

            mMap.addMarker(markerOptions);//add multiple markers, one for ea place

            //move Camera to:
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        }
    }
}
