package comp231.drbooking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.Map;

public class Login extends AppCompatActivity {

    //region >>> Variables
    boolean isFaculty = false , isFacultyRadChecked = true;
    RadioGroup radGp;
    RadioButton radChecked;
    EditText uNameView, uPassView;
    boolean hasAccount = false;
    SharedPreferences prefs;
    Intent intent;
    Map<String,?> allPrefs;
    int PLACE_PICKER_REQUEST  = 1, numOfPrefs , uName_uPass_pairs;// counter = 1;
    String uName, uPass, uNameEntered, uPassEntered;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login or Create New Account");
        //get references
        uNameView = (EditText)findViewById(R.id.txtLoginName);
        uPassView = (EditText)findViewById(R.id.txtLoginPass);

    }

    //Login btn clk
    public void clk_Login(View view)
    {
        //go to PlacePicker : https://www.youtube.com/watch?v=Rh9x90lqPHc
        PlacePicker.IntentBuilder iBuilder = new PlacePicker.IntentBuilder(); //compile 'com.google.android.gms:play-services-places:9.2.0'

        Intent i;

        try
        {
            i = iBuilder.build(Login.this);
            startActivityForResult(i, PLACE_PICKER_REQUEST);
        }
        catch (GooglePlayServicesRepairableException e)
        {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e)
        {
            e.printStackTrace();
        }



/*
        //go to MapActivity
        Intent i = new Intent(this, MapsActivity.class);
        startActivity(i);
*/


    }

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

                getSupportActionBar().setTitle(clinicAddress);


            }


        }
    }
}
