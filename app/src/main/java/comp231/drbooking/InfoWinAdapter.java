package comp231.drbooking;
/*
 * By: SHAFIQ-UR-REHMAN
 * Purpose: Customized InfoWindow for Maps. Displays name & address of a clinic.
 * Allows clicking on InfoWindow to fire BookingDetails activity & passes on the data about that Place as well.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

public class InfoWinAdapter implements InfoWindowAdapter
{
    LayoutInflater inflater=null;
    //    Bitmap bmp;

    InfoWinAdapter(LayoutInflater inflater/*, Bitmap bmp*/)
    {
        this.inflater=inflater;
        //this.bmp = bmp;
    }

    //GoogleMap.InfoWindowAdapter interface needs 2 fn below
    @Override
    public View getInfoWindow(Marker marker)//customise whole infoWin. If u don't want this then just return null & next fn will get called. If that too returns null then default infoWid & default contents r displayed
    {
        return null;
    }

    @Override
    public View getInfoContents(final Marker marker)//infoWin remains default, only it's contents can be customised
    {
        //https://stackoverflow.com/questions/14256828/creating-custominfowindow-in-google-map-v2?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa

        View myMarkLay = inflater.inflate(R.layout.infowindowcustom, null);
        //ImageView iv = myMarkLay.findViewById(R.id.markerIcon);
        Button btn = myMarkLay.findViewById(R.id.markerIcon);

        //MyClkListener listen = new MyClkListener(1 /*userID*/);
        //btn.setOnClickListener(listen);//not possible to set listener on elements (e.g btn) inside infowindow bcoz it's rendered as img : https://github.com/googlemaps/android-samples/issues/56


/*
        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext().getApplicationContext(), "InfoWindow Clicked", Toast.LENGTH_LONG).show();
                Log.d("InfoWindowAdapter", "***********************");
            }
        });
*/


        //iv.setImageResource(R.drawable.centennial);
        //iv.setImageBitmap(bmp);
        //btn.setBackground((Drawable) R.mipmap.ic_launcher);//err trying to set bg img for btn

        TextView tv = (TextView)myMarkLay.findViewById(R.id.markerTitle);
        tv.setText(marker.getTitle());
        tv = (TextView)myMarkLay.findViewById(R.id.markerSnippet);
        tv.setText(marker.getSnippet());
        return(myMarkLay);
        //return null;
    }



    //custom listener for InfoWindow
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
    }




}
