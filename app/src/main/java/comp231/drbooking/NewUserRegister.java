package comp231.drbooking;
/*
 * By: SHAFIQ-UR-REHMAN
 * Purpose: Activity for user to register as new user
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.design.widget.Snackbar;//implementation  'com.android.support:design:26.1.0'

import com.google.gson.Gson;

import java.util.Map;

public class NewUserRegister extends AppCompatActivity {

    //region Class Variables
    String formData, uName, uPass,fName, lName, add, city, postC, isAdmin, key_uName, key_uPass;
    SharedPreferences pref;
    Map<String, ?> allPrefs;
    int numOfPrefs;
    long rowID;
    TextView uNameV;
    DbAdapter dbAdapter;
    Model_User uModel;
    Object[] paramsApiUri;
    Button btnCreateNewUser;
    Gson gson;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_register);
        getSupportActionBar().setTitle("Create New Account");

        //db adapter
        //dbAdapter = new DbAdapter(this);////err (the task has already been executed (a task can be executed only once) => due to re-using same instance of AsyncTask so create a new instance on ea clk.
        paramsApiUri = new Object[3];
        gson = new Gson();//util to convert JSON

        //get form data into class
        uModel = new Model_User();

        //Chk if userName already exist
     /*   btnCreateNewUser = (Button)findViewById(R.id.btnCreateNewUser);
        btnCreateNewUser.setEnabled(false);//disable btn until non-existing uName is entered
        uNameV = (EditText)findViewById(R.id.txtUserName);
        uNameV.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View view, boolean b)
            {
                uName   = (uNameV).getText().toString();
                uModel.loginName = uName;
                formData = gson.toJson(uModel);
                //
                paramsApiUri[0] = "http://10.0.2.2:45455/api/values/userNameExists";//VS extension to allow access to localhost(10.0.2.2 in emulator)https://marketplace.visualstudio.com/items?itemName=vs-publisher-1448185.ConveyorbyKeyoti
                paramsApiUri[1] = formData;//only userName being sent for now
                paramsApiUri[2] = "POST";
                //pass args to AsyncTask to read db
                dbAdapter.execute(paramsApiUri);
            }
        });*/
    }


    public void btnClk_CreateNewUser(View view)
    {
        dbAdapter = new DbAdapter(this);

        //references to EditText & bind model
        uModel.loginName = uName  = ((EditText)findViewById(R.id.txtUserName)).getText().toString();
        uModel.pw = uPass   = ((EditText)findViewById(R.id.txtPass)).getText().toString();
        fName   = ((EditText)findViewById(R.id.txtFName)).getText().toString();
        lName   = ((EditText)findViewById(R.id.txtLName)).getText().toString();
        uModel.nameOfUser = fName;
        uModel.address = add     = ((EditText)findViewById(R.id.txtAdd)).getText().toString();
        uModel.email = ((EditText)findViewById(R.id.txtEmail)).getText().toString();
        uModel.phone = ((EditText)findViewById(R.id.txtPhone)).getText().toString();

        //city    = ((EditText)findViewById(R.id.txtCity)).getText().toString();
        //postC   = ((EditText)findViewById(R.id.txtPostC)).getText().toString();
        //isAdmin = getIntent().getStringExtra("isAdmin");

        //get shared preference
        pref = getSharedPreferences("prefs", 0);

        //make json from model
        formData = gson.toJson(uModel);
        //prep args
        //paramsApiUri[0] = "http://10.0.2.2:45455/api/values/newUser"; //emulator uses this
        //paramsApiUri[0] = "http://192.168.1.6:45455/api/values/newUser?login=xxx&pw=xxx";//VS extension to allow access to localhost(10.0.2.2 in emulator)https://marketplace.visualstudio.com/items?itemName=vs-publisher-1448185.ConveyorbyKeyoti
        paramsApiUri[0] = VariablesGlobal.API_URI + "/api/values/newUser";
        paramsApiUri[1] = formData;
        paramsApiUri[2] = "POST";
        //pass args to AsyncTask to read db
        dbAdapter.execute(paramsApiUri);//if uName already exists, a TOAST will b displayed. If not then Dashboard launched & User_Id stored in Prefs
        //err (the task has already been executed (a task can be executed only once) => due to re-using same instance of AsyncTask so create a new instance on ea clk.
        //err causing Login scrn to load was => TextView.getText()' on a null object reference

        //Snackbar.make(view, "Loading Weather", Snackbar.LENGTH_LONG).setAction("Action", null).show();

    }

    //If need to call sth in this class from AsyncTask, put that here:
    static void AfterAsyncTask(String jsonResponse, Context ctx)
    {
        //Toast.makeText(ctx, jsonResponse + " Login-Name already exists!", Toast.LENGTH_LONG).show();

        //import android.support.design.widget.Snackbar;//implementation  'com.android.support:design:26.1.0'
    }

}
