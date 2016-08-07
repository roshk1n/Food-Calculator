package com.example.roshk1n.foodcalculator;

import android.app.Application;
import com.example.roshk1n.foodcalculator.rest.RestClient;
import com.firebase.client.Firebase;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {

    private static RestClient restClient;

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        restClient = new RestClient();
        RealmConfiguration realmConfig = new RealmConfiguration
                .Builder(getApplicationContext())
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfig);
    }

    public static RestClient getRestClient() { return restClient; }
}