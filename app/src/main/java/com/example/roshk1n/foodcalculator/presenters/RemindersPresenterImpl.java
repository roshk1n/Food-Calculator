package com.example.roshk1n.foodcalculator.presenters;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.manageres.LocalDataBaseManager;
import com.example.roshk1n.foodcalculator.views.RemindersView;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Reminder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RemindersPresenterImpl implements RemindersPresenter {

    private LocalDataBaseManager localDataBaseManager = new LocalDataBaseManager();

    private ArrayList<Reminder> reminders = new ArrayList<>();

    private RemindersView remindersView;

    @Override
    public void setView(RemindersView view) {
        this.remindersView = view;
    }


    private Reminder createReminder(boolean isChecked,String time, String tag) {
        Date date = stringToTime(time);
        Reminder reminder = new Reminder();
        reminder.setName(tag);
        reminder.setTime(date.getTime());
        reminder.setState(isChecked);
        return reminder;
    }

    private void setDefaultReminders() {
        reminders.add(createReminder(false,"8:45",remindersView.getContext().getString(R.string.breakfast)));
        reminders.add(createReminder(false,"14:00",remindersView.getContext().getString(R.string.lunch)));
        reminders.add(createReminder(false,"18:00",remindersView.getContext().getString(R.string.dinner)));
        reminders.add(createReminder(false,"17:00",remindersView.getContext().getString(R.string.snack)));
    }

    @Override
    public ArrayList<Reminder> loadReminder() {
        reminders = localDataBaseManager.loadReminders();
        if (reminders.size() == 0) {
            setDefaultReminders();
            localDataBaseManager.saveReminders(reminders);
        }
        return reminders;
    }

    @Override
    public void updateTime(final int positionAdapter, String time) {
        localDataBaseManager.updateReminderTime(positionAdapter,stringToTime(time).getTime());
        remindersView.setTime(positionAdapter,stringToTime(time).getTime());
    }

    @Override
    public boolean getStateSwitch(int positionAdapter) {
        return localDataBaseManager.getRemindersState(positionAdapter);
    }

    @Override
    public void updateSwitchState(boolean check, int position) {
        localDataBaseManager.updateReminderState(check,position);
    }

    private Date stringToTime(String time) {
        SimpleDateFormat format = new SimpleDateFormat("H:m");
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
