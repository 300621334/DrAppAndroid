package comp231.drbooking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FindClinic extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_clinic);
        getSupportActionBar().setTitle("Clinics Near You");

    }
}
