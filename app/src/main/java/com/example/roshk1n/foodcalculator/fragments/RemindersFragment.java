package com.example.roshk1n.foodcalculator.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.adapters.RecyclerReminderAdapter;
import com.example.roshk1n.foodcalculator.broadcastReceivers.ReceiverNotification;
import com.example.roshk1n.foodcalculator.presenters.RemindersPresenterImpl;
import com.example.roshk1n.foodcalculator.Views.RemindersView;
import com.example.roshk1n.foodcalculator.responseAdapter.CallbackReminderAdapter;
import com.example.roshk1n.foodcalculator.realm.ReminderReaml;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Reminder;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

import io.realm.RealmList;

public class RemindersFragment extends Fragment  implements RemindersView, CallbackReminderAdapter, TimePickerDialog.OnTimeSetListener/* , DialogInterface.OnCancelListener,*/  {

    private RemindersPresenterImpl remindersPresenter;
    private ArrayList<Reminder> reminders;

    private ReceiverNotification receiverNotification;
    private int positionAdapter;
    private int hour, minute;

    private TimePickerDialog timePickerDialog;
    private View view;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerReminderAdapter mAdapter;

    public RemindersFragment() {}

    public static Fragment newInstance() { return new RemindersFragment(); }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        remindersPresenter = new RemindersPresenterImpl();
        remindersPresenter.setView(this);

        receiverNotification = new ReceiverNotification();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reminders, container, false);

        initUI();

        reminders = remindersPresenter.loadReminder();

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerReminderAdapter(reminders,this);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute1, int second) {
        this.hour = hourOfDay;
        this.minute = minute1;
        String time = (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute);

        remindersPresenter.updateTime(positionAdapter,time);
    }

    @Override
    public void createPicker(int position, String title) {
        initDateTimeData();
        positionAdapter = position;
        Calendar calendar = Calendar.getInstance();
        calendar.set(0, 0,0,hour,minute);

        timePickerDialog = TimePickerDialog.newInstance(
                this,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.setAccentColor(getActivity().getResources().getColor(R.color.colorPrimary));
        timePickerDialog.show(getActivity().getFragmentManager(), title);
    }

    @Override
    public void updateSwitch(Boolean check, int positionAdapter) {

        remindersPresenter.updateSwitchState(check,positionAdapter);

        this.positionAdapter = positionAdapter;
        if(!check) {
            receiverNotification.cancelNotification(getContext(),positionAdapter);
        } else {
            receiverNotification.createNotification(getContext(),positionAdapter);
        }
        reminders.get(positionAdapter).setState(check);
    }

    private void initDateTimeData(){
        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
    }

    private void initUI() {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_reminders);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Reminders");
    }

    @Override
    public void setTime(int positionAdapter, long time) {
        reminders.get(positionAdapter).setTime(time);
        mAdapter.notifyDataSetChanged();
        if(remindersPresenter.getStateSwitch(positionAdapter)) {
            receiverNotification.createNotification(getContext(),positionAdapter);
        }
    }
}

