package com.example.roshk1n.foodcalculator.presenters;

import com.example.roshk1n.foodcalculator.views.RemindersView;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Reminder;

import java.util.ArrayList;

public interface RemindersPresenter {

    void setView(RemindersView view);

    ArrayList<Reminder> loadReminder();

    void updateTime(int positionAdapter, String time);

    boolean getStateSwitch(int positionAdapter);

    void updateSwitchState(boolean check, int position);
}
