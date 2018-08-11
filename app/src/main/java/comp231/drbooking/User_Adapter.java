package comp231.drbooking;

/*
 * By: David Tyler
 * Purpose: custom adapter to display result-set when admin searches for users by a part of username
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

//https://stackoverflow.com/questions/16676913/json-to-listview-in-android-with-gson?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa

//custom ArrayAdapter to display list of Model_Booking class
public class User_Adapter extends ArrayAdapter<Model_User>
{

    private List<Model_User> list;
    private Activity context;
    Gson gson;

    //constructor
    public User_Adapter(@NonNull Activity ctx, int layoutId, @NonNull List<Model_User> list)
    {
        super(ctx, layoutId, list);
        this.context = ctx;
        this.list = list;
        gson = new Gson();
    }

    static class ViewHolder
    {
        protected TextView rowLogInName;
        protected TextView rowNameOfUser;
        protected TextView userId_From;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable final View convertView, @NonNull ViewGroup parent)
    {
        View view = null;

        LayoutInflater inflater = context.getLayoutInflater();
        view = inflater.inflate(R.layout.eachuser, null);

        final ViewHolder viewHolder = new ViewHolder();
        viewHolder.rowLogInName = (TextView) view.findViewById(R.id.rowUserName);
        viewHolder.rowNameOfUser = (TextView) view.findViewById(R.id.rowFullName);

        //viewHolder.rowTime.setText(list.get(position).);
        view.setTag(viewHolder);

        Model_User app = list.get(position);

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.rowLogInName.setText(app.loginName);
        holder.rowNameOfUser.setText(app.nameOfUser);
        //holder.userId_From.setText(app.Id_User);



        //click Listener
        myClkListener listener = new myClkListener(context, app);
        view.setOnClickListener(listener);
        /*       //err = all rows return LAST row's info : https://stackoverflow.com/questions/32284979/onclicklistener-in-listview-item-always-takes-the-last-row?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
        //solution = use Tags:

        //but in my case it was re-using app obj, i think. Need to create a new app ea time & pass to custom onClk listener!!
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(context, BookingDetails.class);
                i.putExtra("appointment", gson.toJson(app));
              *//*   i.putExtra();
                i.putExtra();
                i.putExtra();
                i.putExtra();*//*
                context.startActivity(i);
            }
        });
        */

        return view;
        //return super.getView(position, convertView, parent);
    }

    class myClkListener implements View.OnClickListener
    {
        Context ctx;
        Model_User app;

        public myClkListener(Context ctx, Model_User app)
        {
            this.ctx = ctx;
            this.app = app;
        }

        @Override
        public void onClick(View view)
        {
            Intent i = new Intent(context, Settings.class);
            //i.putExtra("user", gson.toJson(app));
            i.putExtra("Id_User", String.valueOf(app.Id_User));
            context.startActivity(i);
            //context.finish();
        }
    }
}