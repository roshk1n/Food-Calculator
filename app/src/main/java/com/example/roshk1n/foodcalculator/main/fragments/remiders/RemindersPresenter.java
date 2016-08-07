package com.example.roshk1n.foodcalculator.main.fragments.remiders;

import com.example.roshk1n.foodcalculator.realm.ReminderReaml;
import com.example.roshk1n.foodcalculator.realm.UserRealm;
import io.realm.RealmList;

public interface RemindersPresenter {

    void setView(RemindersView view);

    UserRealm getCurrentUserRealm();

    void createReminder(boolean isChecked,String time,String tag);

    void setDefaultReminders();

    RealmList<ReminderReaml> getReminderList();

    void updateTime(int positionAdapter, String time);

    ReminderReaml getNotification(int positionAdapter);

    boolean getStateSwitch(int positionAdapter);
}
