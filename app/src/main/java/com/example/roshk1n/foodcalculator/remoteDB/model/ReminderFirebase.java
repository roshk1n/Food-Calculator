package com.example.roshk1n.foodcalculator.remoteDB.model;

import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Reminder;

public class ReminderFirebase {
    private String name;
    private Long time;
    private boolean state;

    public ReminderFirebase() {
    }

    public ReminderFirebase(Reminder reminder) {
        setName(reminder.getName());
        setState(reminder.getState());
        setTime(reminder.getTime());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
