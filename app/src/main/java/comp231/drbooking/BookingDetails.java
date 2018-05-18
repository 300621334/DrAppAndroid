package comp231.drbooking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.TextureView;
import android.widget.TextView;

public class BookingDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

        //ref to views
        TextView txtV = findViewById(R.id.txtBookingActivity);

        //get Extras passed from InfoWindow of marker
        txtV.setText("Name: " + getIntent().getStringExtra("infoWinTitle") + "\n\n\nAddress: " + getIntent().getStringExtra("infoWinAddress"));
    }
}
