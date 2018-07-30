package comp231.drbooking;
/*
 * By: SHAFIQ-UR-REHMAN
 * Purpose: Activity for user to register as new user
 */
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.UnicodeSetSpanner;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.support.design.widget.Snackbar;//implementation  'com.android.support:design:26.1.0'

import com.google.gson.Gson;

import java.sql.SQLNonTransientConnectionException;
import java.util.Map;
import java.util.Random;

public class NewUserRegister extends BaseActivity {

    //region Class Variables
    String ROLE_CODE = "0", roleStr, txtVeriCode,formData, uName, uPass,fName, lName, add, city, postC, key_uName, key_uPass;
    SharedPreferences pref;
    Map<String, ?> allPrefs;
    int numOfPrefs;
    long rowID;
    TextView uNameV;
    EditText txtVeriCodeV, txtVerifyEmailV;
    RadioButton radRoleDr, radRoleAdmin;
    RadioGroup radGrpRole;
    View lay;
    DbAdapter dbAdapter;
    Model_User uModel;
    Object[] paramsApiUri;
    Button btnCreateNewUser, btnVerifyEmail;
    Gson gson;
    int emailCode = 999;
    Intent i;
    boolean isAdmin;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_register);
        getSupportActionBar().setTitle("Create New Account");
        //chk if admin is creating a new user
        roleStr = getSharedPreferences("prefs", 0).getString("role", "");
        isAdmin = roleStr.equals("3")?true:false;
        //
        txtVeriCodeV = (EditText)findViewById(R.id.txtVerifyEmail);
        lay = findViewById(R.id.layNewUser);
        btnVerifyEmail = (Button)findViewById(R.id.btnVerifyEmail);
        txtVerifyEmailV = (EditText)findViewById(R.id.txtVerifyEmail);
        radGrpRole = (RadioGroup)findViewById(R.id.radGrpRole);

        //listener for RadioGroup
        radGrpRole.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id_of_radBtn)
            {
                switch (id_of_radBtn)
                {
                    case R.id.radRolePt:
                        ROLE_CODE = "1";//Patient
                        break;
                    case R.id.radRoleDr:
                        ROLE_CODE = "2";//Care Provider
                        break;
                    case R.id.radRoleAdmin:
                    ROLE_CODE = "3";//Admin
                        break;
                        default:
                            ROLE_CODE = "0";//guest
                            break;
                }
            }
        });

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

        //for admin hide verify email etc
        if(isAdmin)
        {
            btnVerifyEmail.setVisibility(View.INVISIBLE);
            txtVerifyEmailV.setVisibility(View.INVISIBLE);
        }
    }

    public void btnClk_CreateNewUser(View view)
    {
        //for admin by-pass email verification - assume admin enters correct email
        if(isAdmin)
        {
            disableTextBoxes(lay);
            clk_verifyEmail(null);//bypass email verifi for admin
            //btnVerifyEmail.setVisibility(View.INVISIBLE);
            //txtVerifyEmailV.setVisibility(View.INVISIBLE);
            return;
        }
        //for non-admin
        disableTextBoxes(lay);//disable all TextViews & send email with verification code
        sendCodeByEmail(getRandomCode());
        btnVerifyEmail.setEnabled(true);
        txtVerifyEmailV.setEnabled(true);

    }

    private void sendCodeByEmail(int code)
    {
        //https://stackoverflow.com/questions/2197741/how-can-i-send-emails-from-my-android-application?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
        i = new Intent(Intent.ACTION_SEND);//returns all apps who support .ACTION_SEND
        i.setType("message/rfc822");//filter apps for email only
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"mani66@hotmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "DrApp Verification Code");
        i.putExtra(Intent.EXTRA_TEXT   , "your DrApp verification code is: " + emailCode);

        try
        {
            startActivity(Intent.createChooser(i, "Send mail..."));
        }
        catch (android.content.ActivityNotFoundException ex)
        {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private int getRandomCode()
    {
        //https://stackoverflow.com/questions/5887709/getting-random-numbers-in-java/5887745?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
        Random random = new Random();
        emailCode = random.nextInt(900) + 100;//min = 0 + 100 <> max = 899 + 100
        return emailCode;
    }

    private void disableTextBoxes(View layout)
    {
        //https://stackoverflow.com/questions/28778813/android-using-context-to-get-all-textview?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
        if(layout instanceof ViewGroup)
        {
            ViewGroup vg = (ViewGroup)layout;
            int allViews = vg.getChildCount();
            for (int j = 0; j < allViews; j++)
            {
                View v = vg.getChildAt(j);
                disableTextBoxes(v);//recursively call itself on Layouts
            }
        }
        else if (layout instanceof EditText)
        {
            layout.setEnabled(false);//layout here NOT actually layout, rather EditText
        }

    }

    //If need to call sth in this class from AsyncTask, put that here:
    static void AfterAsyncTask(String jsonResponse, Context ctx)
    {
        //Toast.makeText(ctx, jsonResponse + " Login-Name already exists!", Toast.LENGTH_LONG).show();

        //import android.support.design.widget.Snackbar;//implementation  'com.android.support:design:26.1.0'
    }

    public void clk_verifyEmail(View view)
    {
        //for non-admin, read verifi code
        if(!isAdmin)
        {
            txtVeriCode = txtVeriCodeV.getText().toString();
        }

        //if admin, bypass matching of verifi code
        if(isAdmin || String.valueOf(emailCode).equals(txtVeriCode))//verification code matches
        {

            dbAdapter = new DbAdapter(this);

            //references to EditText & bind model
            uModel.loginName = uName  = ((EditText)findViewById(R.id.txtUserName)).getText().toString();
            fName   = ((EditText)findViewById(R.id.txtFName)).getText().toString();
            lName   = ((EditText)findViewById(R.id.txtLName)).getText().toString();
            uModel.nameOfUser = fName;
            uModel.address = add     = ((EditText)findViewById(R.id.txtAdd)).getText().toString();
            uModel.email = ((EditText)findViewById(R.id.txtEmail)).getText().toString();
            uModel.phone = ((EditText)findViewById(R.id.txtPhone)).getText().toString();
            uModel.role = ROLE_CODE;
            //encrypt pw
            try
            {
                uModel.pw = uPass   = AESCrypt.encrypt(((EditText)findViewById(R.id.txtPass)).getText().toString());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

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
        else
        {
            Snackbar.make(view, "code mis-match", Snackbar.LENGTH_LONG).show();
            //Toast.makeText(getApplicationContext(),"code mis-match" , Toast.LENGTH_LONG);//not showing up!!!
        }

    }
}
