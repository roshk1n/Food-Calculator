package com.example.roshk1n.foodcalculator.rest.model.ndbApi.response;

import com.example.roshk1n.foodcalculator.realm.ReminderReaml;

/**
 * Created by roshk1n on 8/12/2016.
 */
public class Reminder {
    private String name;
    private long time;
    private boolean state;

    public Reminder() {
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

    public ReminderReaml convertToRealm() {
        ReminderReaml reminder = new ReminderReaml();
        reminder.setName(this.getName());
        reminder.setTime(this.getTime());
        reminder.setState(this.getState());
        return reminder;
    }
}
