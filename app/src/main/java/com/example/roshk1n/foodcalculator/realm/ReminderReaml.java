package com.example.roshk1n.foodcalculator.realm;

import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Reminder;

import io.realm.RealmObject;

public class ReminderReaml extends RealmObject {

    private String name;
    private long time;
    private boolean state;

    public ReminderReaml() {
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

    public Reminder convertToBase() {
        Reminder reminder = new Reminder();
        reminder.setName(this.getName());
        reminder.setTime(this.getTime());
        reminder.setState(this.getState());
        return reminder;
    }
}
