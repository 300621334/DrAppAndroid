package comp231.drbooking;
/*
 * By: SHAFIQ-UR-REHMAN
 * Purpose: Activity to either display a new booking details or existing booking details once user clicks on an item in list of all bookings
 */
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
/*Model_Booking sample JSON:
{"AppointmentTime":"Sun, 20 May 2018 10:27 PM","Clinic":"Address : 940 progress Ave Toronto","CreationTime":"Sun, 20 May 2018 10:27 PM","Doctor":"Lady Doctor 1","Id_Appointment":0,"Id_User":1}
* */
public class BookingDetails extends BaseActivity implements ICallBackFromDbAdapter
{

    //region Variables
    TextView dateTxtV, timeTxtV, txtV;
    Button btnCancelApp;
    Spinner spinDrList;
    Calendar cal;
    String formData, dateStr, timeStr,AppointmentTime, dateTimeStr, descriptionStr;
    Long dateTimeUnix, rowsIdCreated, rowsAffected;
    DbAdapter dbAdapter;
    Model_Booking bModel, app;//app is Model_Booking sent via Intent from list of all bookings = used to decide whether to save a new app or edit an existing one
    Gson gson;
    Object[] paramsApiUri;
    String[] DrNamesArray;
    public static BookingDetails instance;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);
        instance = this;
        //
        paramsApiUri = new Object[3];
        gson = new Gson();//util to convert JSON

        //get list of all Drs from API
        //DrNamesArray = getResources().getStringArray(R.array.DrNames);
        GetDrArray();//populate static array of names of Drs loc in VariablesGlobal.java
        //List<String> DrNamesArrayList = VariablesGlobal.DrNamesList;

        //ref to views
        txtV = findViewById(R.id.txtBookingActivity);
        spinDrList = findViewById(R.id.spinDrList);
        VariablesGlobal.spinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, VariablesGlobal.DrNamesList);
        spinDrList.setAdapter(VariablesGlobal.spinAdapter);

        btnCancelApp = (Button)findViewById(R.id.btnCancelApp);
        btnCancelApp.setVisibility(View.INVISIBLE);


        //get Extras passed from InfoWindow of marker
        txtV.setText("Address : " + getIntent().getStringExtra("infoWinTitle") /*+ "\n\n\nAddress: " + getIntent().getStringExtra("infoWinAddress")*/);

        //region >>>DatePicker Set Up
        dateTxtV = (TextView)findViewById(R.id.txtDate);
        cal = Calendar.getInstance();
        //Create Listener for DatePicker
        final DatePickerDialog.OnDateSetListener dateChangeListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int yr, int mo, int day)
            {
                cal.set(Calendar.YEAR, yr);
                cal.set(Calendar.MONTH, mo);
                cal.set(Calendar.DAY_OF_MONTH, day);
                updateDateTxtV();//Update txtDateV
            }
        };
        //Assign Listener to dateTxtV
        dateTxtV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog
                        (
                                BookingDetails.this,
                                dateChangeListener,
                                cal.get(Calendar.YEAR),
                                cal.get(Calendar.MONTH),
                                cal.get(Calendar.DAY_OF_MONTH)
                        ).show();
            }
        });
        updateDateTxtV();//set current date as soon as activity loads
        //endregion

        //region >>>TimePicker Set Up
        timeTxtV = (TextView)findViewById(R.id.txtTime);
        //Create Listener for DatePicker
        final TimePickerDialog.OnTimeSetListener timeChangeListener = new TimePickerDialog.OnTimeSetListener()
        //final CustomTimePickerDialog.OnTimeSetListener timeChangeListener = new CustomTimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int _24hr, int min)
            {
                cal.set(Calendar.HOUR_OF_DAY, _24hr);
                cal.set(Calendar.MINUTE, min);

                //https://developer.android.com/reference/java/text/SimpleDateFormat.html
                //https://stackoverflow.com/questions/2659954/timepickerdialog-and-am-or-pm
                //https://stackoverflow.com/questions/7527138/timepicker-how-to-get-am-or-pm
                /*String AM_PM ;
                if(_24hr < 12) {
                    AM_PM = "AM";
                } else {
                    AM_PM = "PM";
                }*/

                updateTimeTxtV();
            }
        };
        //Assign Listener to dateTxtV
        timeTxtV.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                //new TimePickerDialog
                new CustomTimePickerDialog
                        (
                                BookingDetails.this,
                                timeChangeListener,
                                cal.get(Calendar.HOUR),  //auto converts 24hr  time into 12hr time. But we need to set AM/PM
                                cal.get(Calendar.MINUTE),
                                false
                        ).show();
            }
        });
        //endregion

        //
        //region if editing existing booking sent from Booking_All
        String jsonAppointment = getIntent().getStringExtra("appointment");
        app = gson.fromJson(jsonAppointment, Model_Booking.class);
        if(app != null)//i.e. activity launched from clicking on List of all appointments
        {
            btnCancelApp.setVisibility(View.VISIBLE);

            getSharedPreferences("prefs", 0).edit().putInt("Id_Appointment", app.Id_Appointment).commit();
            txtV.setText(app.Clinic);

            SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy hh:mm aaa");
            Date dt = null;
            try
            {
                dt = sdf.parse(app.AppointmentTime);
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
            sdf = new SimpleDateFormat("EEE, d MMM yyyy");
            dateTxtV.setText(sdf.format(dt));
            sdf = new SimpleDateFormat("hh:mm aaa");
            timeTxtV.setText(sdf.format(dt));
            //spinDrList.setSelection(Arrays.asList(DrNamesArray).indexOf(app.Doctor));
            spinDrList.setSelection(VariablesGlobal.DrNamesList.indexOf(app.Doctor));//GetDrArray() is slower so this line of code fixed selected index to '0' unless return bk to list of appoints & come bk

            //region >>> trying to get correct dr selected on spinner - doesn't work
            /*spinDrList.addOnLayoutChangeListener(new View.OnLayoutChangeListener()
            {
                @Override
                public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7)
                {
                    spinDrList.setSelection(VariablesGlobal.DrNamesList.indexOf(app.Doctor));
                }
            });*/
            //endregion

            cal.setTimeInMillis(dt.getTime());
        }
        //endregion

        //set min to 00 so that TimePicker never launches to show any other min
        cal.set(Calendar.MINUTE, 0);
        updateDateTxtV();//set current date as soon as activity loads
        updateTimeTxtV();

    }


    private void GetDrArray()
    {
        //List<String> arrDrNames = new ArrayList<String>();
        dbAdapter = new DbAdapter(this, "GetDrNamesArray", this);
        paramsApiUri[0] = VariablesGlobal.API_URI + "/api/values/doctors";
        paramsApiUri[1] = formData = "";
        paramsApiUri[2] = "GET";
        dbAdapter.execute(paramsApiUri);
    }

    //Update Date in text field
    private void updateDateTxtV()
    {
        String dateFormat ="EEE, d MMM yyyy";//"MM-dd-yy";
        //String dateFormat ="yyyyy.MMMMM.dd GGG";//"MM-dd-yy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.CANADA);
        dateStr = sdf.format(cal.getTime());
        dateTxtV.setText(dateStr);

        //unix datetime for saving in db
        dateTimeUnix = cal.getTimeInMillis() / 1000L;
    }
    //Update Time in text field
    private void updateTimeTxtV()
    {
        String timeFormat ="hh:mm aaa";//12:08 PM
        SimpleDateFormat stf = new SimpleDateFormat(timeFormat, Locale.CANADA);
        //cal.set(Calendar.MINUTE, 0);//round off minutes
        timeStr = stf.format(cal.getTime());
        timeTxtV.setText(timeStr);

        //unix datetime for saving in db
        dateTimeUnix = cal.getTimeInMillis() / 1000L;
    }

    public void clk_SaveAppoint(View view)
    {
        //chk if user is logged in:
        String userIdStr = getSharedPreferences("prefs",0).getString("Id_User", "");
        if(userIdStr.equals(""))
        {
            Intent i = new Intent(this, Login.class);
            startActivity(i);
            finish();
            return;
        }

        dbAdapter = new DbAdapter(this);

        //covert unix-datetime (in seconds) to string
        Date appointTime = new Date(dateTimeUnix * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy hh:mm aaa");
        AppointmentTime = sdf.format(appointTime);

        //bind model
        bModel = new Model_Booking();
        userIdStr = getSharedPreferences("prefs", 0).getString("Id_User", "1");
        int userIdInt = Integer.parseInt(userIdStr);//default appointments go to user # 1
        bModel.Id_User = userIdInt;
        bModel.AppointmentTime = this.AppointmentTime;
        bModel.Clinic = txtV.getText().toString();
        bModel.CreationTime = sdf.format( Calendar.getInstance().getTime() );
        if(spinDrList.getSelectedItemPosition() == 0)//chk if a doc was selected
        {
            Snackbar.make(view, "Select a doctor", Snackbar.LENGTH_LONG).show();
            return;
        }
        bModel.Doctor = (String) spinDrList.getSelectedItem();
        bModel.DRAVAILABLE = "1";

        //make json from model
        formData = gson.toJson(bModel);

        //chk if u r coming from List of appoints or from Map
        if (app == null) //i.e. Coming from Map, hence a non-existing booking, so create a new one
        {
        /*//Sample JSON appointment sent to server
        {"AppointmentTime":"Sun, 20 May 2018 10:27 PM","Clinic":"Address : 940 progress Ave Toronto","CreationTime":"Sun, 20 May 2018 10:27 PM","Doctor":"Lady Doctor 1","Id_Appointment":0,"Id_User":1}
        */

            //At college WiFi, IP given by Conveyer extendion of VS is different than the one from ipconfig. Use latter ip but use port from Conveyer.
            //paramsApiUri[0] = "http://10.24.72.180:45455/api/values/newAppointment";
            //paramsApiUri[0] = "http://192.168.1.3:45455/api/values/newAppointment";//VS extension to allow access to localhost(10.0.2.2 in emulator)https://marketplace.visualstudio.com/items?itemName=vs-publisher-1448185.ConveyorbyKeyoti
            paramsApiUri[0] = VariablesGlobal.API_URI + "/api/values/newAppointment";
            paramsApiUri[1] = formData;
            paramsApiUri[2] = "POST";
        }
        else //existing booking being displayed, hence SAVE btn should NOT create a duplicate rather save any changes to existing booking
        {
            int appointId = getSharedPreferences("prefs", 0).getInt("Id_Appointment", 0);
            //bModel.Id_Appointment = appointId;
            //formData = gson.toJson(bModel);

            //paramsApiUri[0] = "http://192.168.1.3:45455/api/values/UpdateAppoint/" + appointId;
            paramsApiUri[0] = VariablesGlobal.API_URI + "/api/values/UpdateAppoint/" + appointId;
            paramsApiUri[1] = formData;
            paramsApiUri[2] = "POST";
        }

        //pass args to AsyncTask to read db
        dbAdapter.execute(paramsApiUri);
        Bookings_All.instance.finish();
    }


    public void clk_CancelAppoint(View view)
    {
        dbAdapter = new DbAdapter(this);
        int appointId = getSharedPreferences("prefs", 0).getInt("Id_Appointment", 0);
        //paramsApiUri[0] = "http://192.168.1.3:45455/api/values/DeleteAppointment/" + appointId;
        paramsApiUri[0] = VariablesGlobal.API_URI + "/api/values/DeleteAppointment/" + appointId;
        paramsApiUri[1] = formData = "";
        paramsApiUri[2] = "POST";
        dbAdapter.execute(paramsApiUri);
        Bookings_All.instance.finish();


    }

    @Override
    public void onResponseFromServer(String result, Context ctx)
    {
        if(app != null)
        {
            spinDrList.setSelection(VariablesGlobal.DrNamesList.indexOf(app.Doctor));
        }    }

    public void clk_drProfile(View view)
    {
        int DrSelectedIndex = spinDrList.getSelectedItemPosition();
        i = new Intent(this, DrProfile.class);
        i.putExtra("DrSelectedIndex", DrSelectedIndex);
        startActivity(i);
    }
}
