package com.example.roshk1n.foodcalculator;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

import com.example.roshk1n.foodcalculator.rest.RestClient;
import com.firebase.client.Firebase;

public class MyApplication extends Application {

    private static RestClient restClient;
    private int count;

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        restClient = new RestClient();
    }

    public static RestClient getRestClient() { return restClient; }

    public void setRestClient(RestClient restClient) { this.restClient = restClient; }

    public int getCount() { return count; }

    public void setCount(int count) {
        this.count = count;
    }


}