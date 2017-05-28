package com.example.roshk1n.foodcalculator;

import android.app.Application;
import com.example.roshk1n.foodcalculator.rest.RestClient;
import com.firebase.client.Firebase;
import com.google.firebase.database.FirebaseDatabase;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class FoodCalculatorApplication extends Application {
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
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        Localization.onCreate(getApplicationContext(),"en");
    }

    public static RestClient getRestClient() { return restClient; }
}