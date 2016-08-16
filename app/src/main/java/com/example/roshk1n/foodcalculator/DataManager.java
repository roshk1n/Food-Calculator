package com.example.roshk1n.foodcalculator;

import android.support.annotation.NonNull;

import com.example.roshk1n.foodcalculator.remoteDB.CallbackFirebase;
import com.example.roshk1n.foodcalculator.remoteDB.FirebaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by roshk1n on 8/15/2016.
 */
public class DataManager implements CallbackFirebase {
    private LocalDataBaseManager localDataBaseManager = new LocalDataBaseManager();
    private FirebaseHelper firebaseHelper = new FirebaseHelper();
    private CallbackData callbackData;


    public DataManager(CallbackData callbackData) {
        this.callbackData = callbackData;
    }

    public void checkLogin() {
        firebaseHelper.checkLogin();

    }

    @Override
    public void loginSuccessful() {
        Session.startSession();
        Session.getInstance().setEmail(firebaseHelper.getmFirebaseUser().getEmail());
        Session.getInstance().setFullname(firebaseHelper.getmFirebaseUser().getDisplayName());
        Session.getInstance().setUrlPhoto(firebaseHelper.getmFirebaseUser().getPhotoUrl().toString());
        loginVew.navigateToHome();
    }

    @Override
    public void showToast(String text) {
        callbackData.showToast("Authentication failed. Try again please!");
    }
}
