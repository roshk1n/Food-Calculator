package com.example.roshk1n.foodcalculator.presenters;

import com.example.roshk1n.foodcalculator.Session;
import com.example.roshk1n.foodcalculator.Views.RemindersView;
import com.example.roshk1n.foodcalculator.presenters.RemindersPresenter;
import com.example.roshk1n.foodcalculator.realm.ReminderReaml;
import com.example.roshk1n.foodcalculator.realm.UserRealm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by roshk1n on 7/24/2016.
 */
public class RemindersPresenterImpl implements RemindersPresenter {

    private final Realm realm = Realm.getDefaultInstance();

    private RemindersView remindersView;

    @Override
    public void setView(RemindersView view) {
        this.remindersView = view;
    }

    @Override
    public UserRealm getCurrentUserRealm() {
        return realm.where(UserRealm.class)
                .equalTo("email", Session.getInstance().getEmail())
                .findFirst();
    }

    @Override
    public void createReminder(boolean isChecked,String time, String tag) {
        Date date = stringToTime(time);
        realm.beginTransaction();
        ReminderReaml reminderReaml = new ReminderReaml();
        reminderReaml.setName(tag);
        reminderReaml.setTime(date.getTime());
        reminderReaml.setState(isChecked);
        getCurrentUserRealm().getReminders().add(reminderReaml);
        realm.commitTransaction();
    }

    @Override
    public void setDefaultReminders() {
        if(getCurrentUserRealm().getReminders().size() == 0) {
            createReminder(false,"8:45","Breakfast");
            createReminder(false,"14:00","Lunch");
            createReminder(false,"18:00","Dinner");
            createReminder(false,"17:00","Snack");
        }
    }

    @Override
    public RealmList<ReminderReaml> getReminderList() {
        return getCurrentUserRealm().getReminders();
    }

    @Override
    public void updateTime(final int positionAdapter, String time) {
        final Date date = new Date(stringToTime(time).getTime());
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                getCurrentUserRealm().getReminders().get(positionAdapter).setTime(date.getTime());
                remindersView.updateTime();
            }
        });
    }

    @Override
    public ReminderReaml getNotification(int positionAdapter) {
        return getCurrentUserRealm().getReminders().get(positionAdapter);
    }

    @Override
    public boolean getStateSwitch(int positionAdapter) {
        return getCurrentUserRealm().getReminders().get(positionAdapter).getState();
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
