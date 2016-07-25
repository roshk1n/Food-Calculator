package com.example.roshk1n.foodcalculator.main.fragments.remiders;

import android.support.v4.app.Fragment;
import android.view.View;

import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

/**
 * Created by roshk1n on 7/24/2016.
 */
public class RemindersPresenterImpl implements RemindersPresenter,TimePickerDialog.OnTimeSetListener {

    private RemindersView remindersView;
    private TimePickerDialog timePickerDialog;
    private int hour, minute;

    @Override
    public void setView(RemindersView view) {
        this.remindersView = view;
    }

    @Override
    public void showTimePicker(String tag, Fragment fragment){
        initDateTimeData();
        Calendar calendar = Calendar.getInstance();
        calendar.set(0, 0,0,hour,minute);

        timePickerDialog = TimePickerDialog.newInstance(
                this,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show(fragment.getActivity().getFragmentManager(), tag);
        timePickerDialog.setTitle("Choose time");
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int min, int second) {
        hour = hourOfDay;
        minute = min;
        String time = (hour < 10 ? "0"+hour : hour)+" : "+(minute < 10 ? "0"+minute : minute);
        remindersView.SetTime(timePickerDialog.getTag(),time);
    }
    private void initDateTimeData(){
        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
    }
}
