package com.example.roshk1n.foodcalculator.realm;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by roshk1n on 7/23/2016.
 */
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
}
