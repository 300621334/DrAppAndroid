package comp231.drbooking;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/*
 * By: SHAFIQ-UR-REHMAN
 * Purpose: To display custom_dialog dialogue box
 * ***NOT in use
 */
public class CustomDialogClass  extends Dialog implements android.view.View.OnClickListener
{
    //https://stackoverflow.com/questions/10285047/showdialog-deprecated-whats-the-alternative?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
    //https://stackoverflow.com/questions/13341560/how-to-create-a-custom-dialog-box-in-android?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
    //https://stackoverflow.com/questions/4279787/how-can-i-pass-values-between-a-dialog-and-an-activity?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
    //https://stackoverflow.com/questions/19666572/how-to-call-a-method-in-another-activity-from-activity?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
    //https://stackoverflow.com/questions/19666572/how-to-call-a-method-in-another-activity-from-activity?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa

    public Activity c;
    public Dialog d;
    public Button yes, no;
    boolean isYES = true;

    public CustomDialogClass(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btn_no);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                c.finish();
                break;
            case R.id.btn_no:
                dismiss();
                break;
            default:
                break;
        }
        //dismiss();
    }

}
