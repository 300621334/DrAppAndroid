package comp231.drbooking;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import comp231.drbooking.databinding.ActivityDrProfileBinding;

public class DrProfile extends AppCompatActivity {

    //http://www.vogella.com/tutorials/AndroidDatabinding/article.html
    /*MUST import comp231.drbooking.databinding.ActivityDrprofileBinding;
      if UNKNOWN class err => rename xml would fix the issue*/
    ActivityDrProfileBinding binding;//https://guides.codepath.com/android/Applying-Data-Binding-for-Views

    //region >>> Vars
    int DrSelectedPos;
    Model_DrProfile dr;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dr_profile);
        TextView txtDrNameV = binding.txtDrName;
        txtDrNameV.setText("a a a a a a ");
        //setContentView(R.layout.activity_dr_profile);

        //
        dr = new Model_DrProfile();
        DrSelectedPos = getIntent().getIntExtra("DrSelectedIndex", 0);
        if(DrSelectedPos != 0)
        {
            dr = VariablesGlobal.DrProfiles.get(DrSelectedPos - 1);
        }
        else
        {
            dr.name = "No Doctor Was Selected";
            dr.specialty = "No Doctor Was Selected";
            dr.email = "No Doctor Was Selected";
            dr.phone = "No Doctor Was Selected";
            dr.id_doc = 0;
            dr.Id_User = 0;
        }

        binding.setDr(dr);
    }
}
