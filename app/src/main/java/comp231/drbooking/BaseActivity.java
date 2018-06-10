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
    Intent i;

    //https://stackoverflow.com/questions/3270206/same-option-menu-in-all-activities-in-android?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflator = getMenuInflater();
        inflator.inflate(R.menu.menuoptions, menu);
        //alter bw "Login / Register" & "Logout" on menu item; based on whether user is loggedin or not
        MenuItem logInOutItem = menu.findItem(R.id.menuLogout);
        logInOutItem.setTitle(getSharedPreferences("prefs",0).getString("Id_User", "").equals("")?"Login / Register":"Logout");

        return true;
        //return super.onCreateOptionsMenu(menuoptions);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menuLogout:
                getSharedPreferences("prefs",0).edit().putString("Id_User", "").putString("role", "").commit();//logout by removing logged-in user's ID

                //taken back to Login screen
                i = new Intent(this, Login.class);
                startActivity(i);
                finish();
                break;
            case R.id.menuDashboard:
                //only if logged-in then show Dashboard
                if(getSharedPreferences("prefs",0).getString("Id_User", "").equals(""))
                    break;
                i = new Intent(this, Dashboard.class);
                startActivity(i);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
