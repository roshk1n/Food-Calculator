package com.example.roshk1n.foodcalculator;

import com.firebase.client.Firebase;

public class MyApplication extends android.app.Application {

    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}