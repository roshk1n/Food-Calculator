package com.example.roshk1n.foodcalculator.rest.model.ndbApi.response;

import com.example.roshk1n.foodcalculator.realmModel.ReminderReaml;
import com.example.roshk1n.foodcalculator.remoteDB.model.ReminderFirebase;

public class Reminder {
    private String name;
    private long time;
    private boolean state;

    public Reminder() {
    }

    public Reminder(ReminderReaml reminderReaml) {
        setName(reminderReaml.getName());
        setState(reminderReaml.getState());
        setTime(reminderReaml.getTime());
    }

    public Reminder(ReminderFirebase reminderFirebase) {
        setName(reminderFirebase.getName());
        setTime(reminderFirebase.getTime());
        setState(reminderFirebase.getState());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
