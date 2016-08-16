package com.example.roshk1n.foodcalculator;

import android.support.annotation.NonNull;

import com.example.roshk1n.foodcalculator.Views.CallbackLoadDayFirebase;
import com.example.roshk1n.foodcalculator.remoteDB.CallbackFirebase;
import com.example.roshk1n.foodcalculator.remoteDB.FirebaseHelper;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Day;
import com.example.roshk1n.foodcalculator.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Date;

/**
 * Created by roshk1n on 8/15/2016.
 */
public class DataManager implements CallbackFirebase, CallbackLocalManager {
    private LocalDataBaseManager localDataBaseManager = new LocalDataBaseManager(this);
    private FirebaseHelper firebaseHelper = new FirebaseHelper(this);
    private CallbackData callbackData;

    public DataManager() {}

    public DataManager(CallbackData callbackData) {
        this.callbackData = callbackData;
    }



    public void loginWithLogin(String email, String password) {
        if(Utils.isConnectNetwork(callbackData.getContext())) {
            firebaseHelper.logInWithEmail(email, password);

        } else {
            localDataBaseManager.loginUser(email,password);
        }
    }

    public void checkLogin() {
        firebaseHelper.checkLogin();
    }

    @Override
    public void loginSuccessful() {
        callbackData.navigateToHome();
    }

    @Override
    public void showToast(String text) {
        callbackData.showToast("Authentication failed. Try again please!");
    }

    public void loadDayData(Date date, final CallbackLoadDay callback) {
        Day day = localDataBaseManager.loadDayData(date);
        callback.setDay(day);
        firebaseHelper.loadDay(date, new CallbackLoadDayFirebase() {
            @Override
            public void loadComplete(Day day) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                callback.setDay(day);
            }
        });
    }

    public void removeFood(int indexRemove) {
        localDataBaseManager.removeFood(indexRemove);
    }
}
