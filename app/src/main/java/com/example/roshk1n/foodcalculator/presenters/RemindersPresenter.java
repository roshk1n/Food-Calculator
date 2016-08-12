package com.example.roshk1n.foodcalculator.presenters;

import com.example.roshk1n.foodcalculator.Views.RemindersView;
import com.example.roshk1n.foodcalculator.realm.ReminderReaml;
import com.example.roshk1n.foodcalculator.realm.UserRealm;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Reminder;

import java.util.ArrayList;

import io.realm.RealmList;

public interface RemindersPresenter {

    void setView(RemindersView view);

    ArrayList<Reminder> loadReminder();

    void updateTime(int positionAdapter, String time);

    boolean getStateSwitch(int positionAdapter);

    void updateSwitchState(boolean check, int position);
}
