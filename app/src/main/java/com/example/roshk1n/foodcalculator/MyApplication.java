package com.example.roshk1n.foodcalculator;

import android.app.Application;
import android.util.Log;

import com.example.roshk1n.foodcalculator.realmModel.UserRealm;
import com.example.roshk1n.foodcalculator.rest.RestClient;
import com.firebase.client.Firebase;
import com.google.firebase.database.FirebaseDatabase;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

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
  /*       FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        Realm realm = Realm.getDefaultInstance();

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