package comp231.drbooking;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;

public class Settings extends BaseActivity {

    //region Class Variables
    String userIdStr, roleStr, formData, uName, uPass,fName, lName, add;
    DbAdapter dbAdapter;
    Model_User uModel;
    SharedPreferences pref;
    Gson gson;
    Object[] paramsApiUri;


    //endregion


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle("Update User Profile");//getActionBar() gives null err

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
        uModel.loginName = uName  = ((EditText)findViewById(R.id.txtEditUserName)).getText().toString();
        fName   = ((EditText)findViewById(R.id.txtEditFName)).getText().toString();
        lName   = ((EditText)findViewById(R.id.txtEditLName)).getText().toString();
        uModel.nameOfUser = fName;
        uModel.address = add     = ((EditText)findViewById(R.id.txtEditAdd)).getText().toString();
        uModel.email = ((EditText)findViewById(R.id.txtEditEmail)).getText().toString();
        uModel.phone = ((EditText)findViewById(R.id.txtEditPhone)).getText().toString();
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


}
