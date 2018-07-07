package comp231.drbooking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

/*
 * By: SHAFIQ-UR-REHMAN
 * Purpose: Base class for all activities that will show a menuoptions (three dots) on top right corner. e.g. to logout etc
 */
public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    Intent i;
    android.support.v7.widget.Toolbar mToolbar;//as opp to android.widget.Toolbar
    protected DrawerLayout fullLayout;
    protected FrameLayout frameLayout;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mToggle;
    View inflatedView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setNavigationViewListener();//calling it here gives NULL bcoz inflate not happened yet. Call this in EACH act inheriting from base

    }

    @Override
    public void setContentView(int layoutResID) {

        fullLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        frameLayout = (FrameLayout) fullLayout.findViewById(R.id.drawer_frame);

        getLayoutInflater().inflate(layoutResID, frameLayout, true);

        //setNavigationViewListener();//even here inflate not happened fully so still get null

        super.setContentView(fullLayout);

        //Your drawer content...

    }

    public void drawer_navigation_setup()
    {
        //https://stackoverflow.com/questions/2271570/android-findviewbyid-finding-view-by-id-when-view-is-not-on-the-same-layout-in
      //LayoutInflater inflater = getLayoutInflater();
        //View mDrawerLayV = inflater.inflate(R.layout.activity_find_clinic, null);
        //mDrawerLayout = (DrawerLayout)mDrawerLayV.findViewById(R.id.layNavDrawer);

        //mToolbar  = (Toolbar)getLayoutInflater().inflate(R.layout.drawer_actionbar, null);//only from main launcher act findViewById() worked, from nect acts it gave NULL : https://stackoverflow.com/questions/28821018/findviewbyid-not-working-when-using-a-seperate-class


        //SET ACTION-BAR:
        mToolbar = findViewById(R.id.drawerActBar);//bcoz of missing <include>, this line works on 1st act but for next act becomes NULL!!!
        setSupportActionBar(mToolbar);//sets a Toolbar obj to be an ActionBar for act

        //MAKE HAMBURGER-ICON TOGGLE THE DRAWER:
        //https://www.youtube.com/watch?v=dpE8kzZznAU         //watch 3:15+ to open drawer on clk : https://www.youtube.com/watch?v=dpE8kzZznAU
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.dashOpen, R.string.dashClose);//last 2 args for accessibility
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//back arrow

    }


    /*   DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //https://stackoverflow.com/questions/2271570/android-findviewbyid-finding-view-by-id-when-view-is-not-on-the-same-layout-in
        LayoutInflater inflater = getLayoutInflater();
        View mDrawerLayV = inflater.inflate(R.layout.activity_find_clinic, null);
        mDrawerLayout = (DrawerLayout)mDrawerLayV.findViewById(R.id.layNavDrawer);

        //https://www.youtube.com/watch?v=dpE8kzZznAU
        //mDrawerLayout = (DrawerLayout) findViewById(R.id.layNavDrawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.dashOpen, R.string.dashClose);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
    }*/

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
        //FOR DRAWER
        if(mToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        //return super.onOptionsItemSelected(item);


        //FOR MENU
        WhichItemClked(item);
        return super.onOptionsItemSelected(item);
    }

    private void WhichItemClked(MenuItem item)
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
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        //Drawer clk = https://stackoverflow.com/questions/42297381/onclick-event-in-navigation-drawer

        //FOR DRAWER-MENU
        WhichItemClked(item);

        //close navigation drawer
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    public void setNavigationViewListener()
    {
        NavigationView navigationView = (NavigationView) findViewById(R.id.drawer_drawer);
        navigationView.setNavigationItemSelectedListener(this);
    }


}
