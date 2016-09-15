package com.example.roshk1n.foodcalculator;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.example.roshk1n.foodcalculator.rest.RestClient;
import com.facebook.FacebookSdk;
import com.firebase.client.Firebase;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        Localization.onCreate(getApplicationContext(),"en");

    /*  Realm realm = Realm.getDefaultInstance();

          realm.executeTransaction(new Realm.Transaction() {
                                     @Override
                                     public void execute(Realm realm) {
                                         realm.deleteAll();
                                     }
                                 }
        );

        RealmResults r = realm.where(UserRealm.class).findAll();
        Log.d("fasfsas",r.size()+"safas");*/
    }

    public static RestClient getRestClient() { return restClient; }
}