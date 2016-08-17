package com.example.roshk1n.foodcalculator;

import android.util.Log;

import com.example.roshk1n.foodcalculator.interfaces.DataCallback;
import com.example.roshk1n.foodcalculator.interfaces.FirebaseCallback;
import com.example.roshk1n.foodcalculator.interfaces.LoadDayCallback;
import com.example.roshk1n.foodcalculator.interfaces.LoadDayFirebaseCallback;
import com.example.roshk1n.foodcalculator.interfaces.LocalManagerCallback;
import com.example.roshk1n.foodcalculator.remoteDB.FirebaseHelper;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Day;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;
import com.example.roshk1n.foodcalculator.utils.Utils;

import java.util.Date;

/**
 * Created by roshk1n on 8/15/2016.
 */
public class DataManaget implements FirebaseCallback, LocalManagerCallback {
    private LocalDataBaseManager localDataBaseManager;
    private FirebaseHelper firebaseHelper = new FirebaseHelper(this);
    private DataCallback dataCallback;

    public DataManaget() {}

    public DataManaget(DataCallback dataCallback) {
        this.dataCallback = dataCallback;
    }



    public void loginWithLogin(String email, String password) {
        if(Utils.isConnectNetwork(dataCallback.getContext())) {
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
        dataCallback.navigateToHome();
    }

    @Override
    public void showToast(String text) {
        dataCallback.showToast("Authentication failed. Try again please!");
    }

    public void loadDayData(final Date date, final LoadDayCallback callback) {
        Day day = LocalDataBaseManager.loadDayData(date);
        callback.loadComplete(day);
        firebaseHelper.loadDay(date, new LoadDayCallback() {
            @Override
            public void loadComplete(Day dayFire) {
                callback.loadComplete(dayFire);
                dayFire.setDate(date.getTime());
                LocalDataBaseManager.updateDays(dayFire);
            }
        });
    }

    public void removeFood(int indexRemove) {
        LocalDataBaseManager.removeFood(indexRemove);
        firebaseHelper.removeFood(indexRemove);
    }

    public void addFood(Food food) {
        firebaseHelper.addFood(food);
    }
}
