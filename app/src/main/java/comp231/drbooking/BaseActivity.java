package comp231.drbooking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
/*
 * By: SHAFIQ-UR-REHMAN
 * Purpose: Base class for all activities that will show a menuoptions (three dots) on top right corner. e.g. to logout etc
 */
public class BaseActivity extends AppCompatActivity
{
    //https://stackoverflow.com/questions/3270206/same-option-menu-in-all-activities-in-android?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflator = getMenuInflater();
        inflator.inflate(R.menu.menuoptions, menu);
        return true;
        //return super.onCreateOptionsMenu(menuoptions);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menuLogout:
                getSharedPreferences("prefs",0).edit().putString("Id_User", "").commit();//logout by removing logged-in user's ID

                //taken back to Login screen
                Intent i = new Intent(this, Login.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
