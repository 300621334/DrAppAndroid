package comp231.drbooking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

public class Settings extends BaseActivity implements ICallBackFromDbAdapter {

    //region Class Variables
    String userIdStr, roleStr, formData, uName, uPass,fName, lName, add;
    DbAdapter dbAdapter;
    Model_User uModel;
    SharedPreferences pref;
    Gson gson;
    Object[] paramsApiUri;
    EditText loginNameV, fNameV, lNameV, addressV, emailV, phoneV, pwV, Id_UserV;
    Button btnDeleteUserV;

    //endregion


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle("Update User Profile");//getActionBar() gives null err

        //get refs to EditText views
        loginNameV = (EditText)findViewById(R.id.txtEditUserName);
        fNameV = (EditText)findViewById(R.id.txtEditFName);
        //lNameV = (EditText)findViewById(R.id.txtEditLName);//NOT allowing changing of Last Name.
        addressV = (EditText)findViewById(R.id.txtEditAdd);
        emailV = (EditText)findViewById(R.id.txtEditEmail);
        phoneV = (EditText)findViewById(R.id.txtEditPhone);
        Id_UserV = (EditText)findViewById(R.id.txtEditId);
        //disable the update btn & make del btn invisi
        ((Button)findViewById(R.id.btnUpdateUser)).setEnabled(false);
        ((Button)findViewById(R.id.btnDeleteUser)).setVisibility(View.INVISIBLE);

        //get role of logedin person
        roleStr = getSharedPreferences("prefs", 0).getString("role", "");

        //init vars
        paramsApiUri = new Object[3];
        gson = new Gson();
        uModel = new Model_User();

        //Either logged-in user's id/role passed here -or- ADMIN passes id/role of the user he's searching for
        userIdStr = getIntent().getStringExtra("Id_User");//= getSharedPreferences("prefs", 0).getString("Id_User", "1");
        //roleStr = getIntent().getStringExtra("role");//= getSharedPreferences("prefs", 0).getString("role", "");

    }

    //Get & display details of a user from DB via API
    //http://drappapi.azurewebsites.net/api/values/getuserdetail/1
    public void btnClk_EditUserProfile(View view)
    {
        //chk if user is logged in:
        String logedinUserIdStr = getSharedPreferences("prefs",0).getString("Id_User", "");
        if(logedinUserIdStr.equals(""))
        {
            Intent i = new Intent(this, Login.class);
            startActivity(i);
            finish();
            return;
        }
        //
        dbAdapter = new DbAdapter(Settings.this, new Settings());//new Settings() just to give access to DbAdapter to onResponseFromServer() via implementing ICallBackFromDbAdapter
        paramsApiUri[0] = VariablesGlobal.API_URI + "/api/values/getuserdetail/" + userIdStr;
        paramsApiUri[1] = "";//formData not needed for this GET req since user_id is appended to URL
        paramsApiUri[2] = "GET";
        //pass args to AsyncTask to read db
        dbAdapter.execute(paramsApiUri);


    }

    //POST new user-details to db
    /* //http://drappapi.azurewebsites.net/api/values/UpdateUser/13
     {
                "Id_User": 13,
                "nameOfUser": "mani",
                "loginName": "mani",
                "pw": "w9pXfCD32mYyVmCQD3svUQ==",
                "address": "mani 123 456",
                "email": "mani@email.com",
                "phone": "1234",
                "role": "1"
     }
    */
    public void btnClk_UpdateUser(View view)
    {
        dbAdapter = new DbAdapter(this);

        //references to EditText & bind model
        uModel.loginName = uName  = (loginNameV).getText().toString();
        fName   = (fNameV).getText().toString();
        //lName   = (lNameV).getText().toString();
        uModel.nameOfUser = fName;
        uModel.address = add     = (addressV).getText().toString();
        uModel.email = (emailV).getText().toString();
        uModel.phone = (phoneV).getText().toString();
        //uModel.role = roleStr;//API does NOT update role & Id_User so those 2 can remain "0" in the obj being sent

        //encrypt pw
        try
        {
            uModel.pw = uPass   = AESCrypt.encrypt(((EditText)findViewById(R.id.txtEditPass)).getText().toString());
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
        paramsApiUri[0] = VariablesGlobal.API_URI + "/api/values/UpdateUser/" + userIdStr;
        paramsApiUri[1] = formData;
        paramsApiUri[2] = "POST";
        //pass args to AsyncTask to read db
        dbAdapter.execute(paramsApiUri);//if uName already exists, a TOAST will b displayed. If not then Dashboard launched & User_Id stored in Prefs
    }

    //populate detail of user in EditText boxes
    @Override
    public void onResponseFromServer(String result, Context ctx)
    {
        //global vars that were init in onCeate() r NOT available here as this fn is being called by DbAdapter
        // so have to create new Model_User & Gson
        Model_User u;
        Gson gson = new Gson();
        //need to get refs AGAIN via ctx
        loginNameV = ((Activity)ctx).findViewById(R.id.txtEditUserName);
        fNameV =((Activity)ctx).findViewById(R.id.txtEditFName);
        //lNameV = ((Activity)ctx).findViewById(R.id.txtEditLName);//NOT allowing changing of Last Name.
        addressV = ((Activity)ctx).findViewById(R.id.txtEditAdd);
        emailV = ((Activity)ctx).findViewById(R.id.txtEditEmail);
        phoneV = ((Activity)ctx).findViewById(R.id.txtEditPhone);
        pwV = ((Activity)ctx).findViewById(R.id.txtEditPass);
        //Id_UserV = ((Activity)ctx).findViewById(R.id.txtEditId);
        btnDeleteUserV = ((Activity)ctx).findViewById(R.id.btnDeleteUser);

        //map user-JSON to user-obj
        u = gson.fromJson(result, Model_User.class);

        //de-crypt pw
        try
        {
            uPass   = AESCrypt.decrypt(u.pw); //encrypt(((EditText)findViewById(R.id.txtEditPass)).getText().toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        //populate EditTexts w details of retrieved user
        loginNameV.setText(u.loginName);
        fNameV.setText(u.nameOfUser);
        addressV.setText(u.address);
        emailV.setText(u.email);
        phoneV.setText(u.phone);
        //Id_UserV.setText(u.Id_User);//cannot set txt of visibility.GONE ctrl. So use prefs instead
        ((Activity)ctx).getSharedPreferences("prefs", 0).edit().putString("Id_UserEditing",String.valueOf(u.Id_User)).commit();
        pwV.setText(uPass);

        //activate the btn
        ((Activity)ctx).findViewById(R.id.btnUpdateUser).setEnabled(true);
        //if ADMIN enable del btn too
        //get role of logedin person
        String roleStr = ((Activity)ctx).getSharedPreferences("prefs", 0).getString("role", "");
        if(roleStr.equals("3"))//admin is logged in
        {
            btnDeleteUserV.setVisibility(View.VISIBLE);
        }

    }

    //only ADMIN an delete a user
    public void btnClk_DeleteUser(View btn_v)
    {
        alert("", "Action_DeleteUser", btn_v);
        //DeleteUser(btn_v);
    }

    private void DeleteUser(View btn_v)
    {
        //get Id_User from hidden ctrl
        String Id_UserEditing = getSharedPreferences("prefs", 0).getString("Id_UserEditing", "");

        //send id to be deleted to API
        dbAdapter = new DbAdapter(this);
        //e.g. /api/values/DeleteUser/13
        paramsApiUri[0] = VariablesGlobal.API_URI + "/api/values/DeleteUser/" + Id_UserEditing;
        paramsApiUri[1] = formData = "";
        paramsApiUri[2] = "POST";
        dbAdapter.execute(paramsApiUri);
        finish();
    }

    public void alert(String txtMsg, final String action, final View btn_view)
    {
        //region (1) set custom view for dialog
        //https://stackoverflow.com/questions/4279787/how-can-i-pass-values-between-a-dialog-and-an-activity?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa

        LayoutInflater inflator = LayoutInflater.from(Settings.this);
        final View yourCustomView = inflator.inflate(R.layout.custom_dialog, null);
        //endregion

        //region (2) init dialogue
        final AlertDialog dialog = new AlertDialog.Builder(Settings.this)
                .setTitle("Do you want to proceed ?")//replace w "txtMsg"
                .setView(yourCustomView)
                /*.setPositiveButton("YES", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton)
                    {

                    }
                })
                .setNegativeButton("NO", null)*/
                .create();
        //endregion

        //region (3) set onClicks for custom dialog btns
        yourCustomView.findViewById(R.id.btn_yes).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                switch (action)
                {
                 /*   case "Action_UpdateUser":
                        SaveAppoint(btn_view);
                        break;*/
                    case "Action_DeleteUser":
                        DeleteUser(btn_view);
                        dialog.dismiss();
                        break;
                }
            }
        });
        yourCustomView.findViewById(R.id.btn_no).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });
        //endregion

        //region (4) Display dialogue
        dialog.show();
        //endregion
    }
}
