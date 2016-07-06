package com.example.roshk1n.foodcalculator;

/**
 * Created by roshk1n on 6/27/2016.
 */

import com.firebase.client.Firebase;

/**
 * Created by AndroidBash on 11/05/16
 */
public class CustomApplication extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}