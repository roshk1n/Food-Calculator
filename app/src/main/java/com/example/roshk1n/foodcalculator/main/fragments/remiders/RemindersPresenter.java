package com.example.roshk1n.foodcalculator.main.fragments.remiders;

import android.support.v4.app.Fragment;

import com.example.roshk1n.foodcalculator.realm.ReminderReaml;
import com.example.roshk1n.foodcalculator.realm.UserRealm;

import io.realm.RealmList;

/**
 * Created by roshk1n on 7/24/2016.
 */
public interface RemindersPresenter {

    void setView(RemindersView view);

    UserRealm getCurrentUserRealm();

    void createReminder(boolean isChecked,String time,String tag);

    void setDufaultReminders();

    RealmList<ReminderReaml> getReminderList();

    void updateTime(int positionAdapter, String time);

    ReminderReaml getNotification(int positionAdapter);

    boolean getStateSwitch(int positionAdapter);
}
