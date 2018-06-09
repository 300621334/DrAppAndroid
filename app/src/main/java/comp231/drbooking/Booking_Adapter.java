package comp231.drbooking;
/*
* By: SHAFIQ-UR-REHMAN
* Purpose: ListView will use this adapter to display customized view for each item in the list of bookings
*/
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

//https://stackoverflow.com/questions/16676913/json-to-listview-in-android-with-gson?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa

//custom ArrayAdapter to display list of Model_Booking class
public class Booking_Adapter extends ArrayAdapter<Model_Booking>
{

    private List<Model_Booking> list;
    private Activity context;
    Gson gson;

    //constructor
    public Booking_Adapter(@NonNull Activity ctx, int layoutId, @NonNull List<Model_Booking> list)
    {
        super(ctx, layoutId, list);
        this.context = ctx;
        this.list = list;

        gson = new Gson();
    }

    static class ViewHolder
    {
        protected TextView rowTime;
        protected TextView rowDrName;
        protected TextView rowClinic;

    }



    @NonNull
    @Override
    public View getView(int position, @Nullable final View convertView, @NonNull ViewGroup parent)
    {
        View view = null;

        LayoutInflater inflater = context.getLayoutInflater();
        view = inflater.inflate(R.layout.eachbooking, null);

        final ViewHolder viewHolder = new ViewHolder();
        viewHolder.rowTime = (TextView) view.findViewById(R.id.rowTime);
        viewHolder.rowDrName = (TextView) view.findViewById(R.id.rowDrName);
        viewHolder.rowClinic = (TextView) view.findViewById(R.id.rowClinic);

        //viewHolder.rowTime.setText(list.get(position).);
        view.setTag(viewHolder);

        Model_Booking app = list.get(position);

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.rowTime.setText(app.AppointmentTime);
        holder.rowDrName.setText(app.Doctor);
        holder.rowClinic.setText(app.Clinic);

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
        Model_Booking app;

        public myClkListener(Context ctx, Model_Booking app)
        {
            this.ctx = ctx;
            this.app = app;
        }

        @Override
        public void onClick(View view)
        {
            Intent i = new Intent(context, BookingDetails.class);
            i.putExtra("appointment", gson.toJson(app));
            context.startActivity(i);
            //context.finish();
        }
    }
}

