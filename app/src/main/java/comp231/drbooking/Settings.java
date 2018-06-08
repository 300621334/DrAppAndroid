package comp231.drbooking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Settings extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getActionBar().setTitle("Settings");
    }
}
