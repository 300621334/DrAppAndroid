package comp231.drbooking;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CustomTimePickerDialog extends TimePickerDialog
{
//http://chandelashwini.blogspot.com/2013/01/timepickerdialog-15-minutes-interval.html
    public CustomTimePickerDialog(Context arg0, OnTimeSetListener callBack, int hourOfDay, int minute, boolean is24HourView)
    {
        super(arg0, callBack, hourOfDay, minute, is24HourView);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute)
    {
        // TODO Auto-generated method stub
        //super.onTimeChanged(arg0, arg1, arg2);


        if (mIgnoreEvent)//ignore any change made while this code is setting the time : view.setCurrentMinute(minute);
            return;

        if (minute%TIME_PICKER_INTERVAL!=0)
        {
            int minuteFloor=minute-(minute%TIME_PICKER_INTERVAL);//31 floors to 30 <> 29 floors to 00 => so picker will always show either 00 or 30 & none else => thus on ea side of 00 will be 59 & 01 => ea side of 30 is 29 & 31
            minute=minuteFloor + (minute==minuteFloor+1 ? TIME_PICKER_INTERVAL : 0);//if i spin-UPWARDS to 01 or 31, then add 30 min to newly set floor <-> if I spin-DOWN to 29 or 59, then add 0 to newly set floor

            if (minute==60)
                minute=0;

            //ignore any change made while TimePicker is being set
            mIgnoreEvent=true;
            view.setCurrentMinute(minute);
            mIgnoreEvent=false;
        }
    }

    private final int TIME_PICKER_INTERVAL=30;
    private boolean mIgnoreEvent=false;

}

/*
public class CustomTimePickerDialog extends TimePickerDialog
{
    //https://stackoverflow.com/questions/20214547/show-timepicker-with-minutes-intervals-in-android
    private final static int TIME_PICKER_INTERVAL = 30;
    private TimePicker mTimePicker;
    private final OnTimeSetListener mTimeSetListener;

    //constructor
    public CustomTimePickerDialog(Context context, OnTimeSetListener listener,int hourOfDay, int minute, boolean is24HourView)
    {
        super(context, TimePickerDialog.THEME_HOLO_LIGHT, null, hourOfDay,minute / TIME_PICKER_INTERVAL, is24HourView);
        mTimeSetListener = listener;
    }//constructor end

    @Override
    public void updateTime(int hourOfDay, int minuteOfHour)
    {
        mTimePicker.setCurrentHour(hourOfDay);
        mTimePicker.setCurrentMinute(minuteOfHour / TIME_PICKER_INTERVAL);
    }

    @Override
    public void onClick(DialogInterface dialog, int which)
    {
        switch (which)
        {
            case BUTTON_POSITIVE:
                if (mTimeSetListener != null)
                {
                    mTimeSetListener.onTimeSet(mTimePicker, mTimePicker.getCurrentHour(),
                            mTimePicker.getCurrentMinute() * TIME_PICKER_INTERVAL);
                }
                break;
            case BUTTON_NEGATIVE:
                cancel();//TimePickerDialog.cancel()
                break;
        }
    }

    @Override
    public void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        try {
            Class<?> classForid = Class.forName("com.android.internal.R$id");
            Field timePickerField = classForid.getField("timePicker");
            mTimePicker = (TimePicker) findViewById(timePickerField.getInt(null));
            Field field = classForid.getField("minute");

            NumberPicker minuteSpinner = (NumberPicker) mTimePicker.findViewById(field.getInt(null));
            minuteSpinner.setMinValue(0);
            minuteSpinner.setMaxValue((60 / TIME_PICKER_INTERVAL) - 1);
            List<String> displayedValues = new ArrayList<>();
            for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL)
            {
                displayedValues.add(String.format("%02d", i));
            }
            minuteSpinner.setDisplayedValues(displayedValues.toArray(new String[displayedValues.size()]));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}//class end
*/
