package com.example.roshk1n.foodcalculator.views;

import android.content.Context;

/**
 * Created by roshk1n on 7/14/2016.
 */
public interface LoginView {

    void setEmailError();

    void setPasswordError();

    void navigateToHome();

    void showToast(String message);

    Context getContext();

    void loginSuccessFacebook();
}
