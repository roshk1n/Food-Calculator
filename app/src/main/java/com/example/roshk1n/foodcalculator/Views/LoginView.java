package com.example.roshk1n.foodcalculator.views;

import android.content.Context;

public interface LoginView {
    void setEmailError();

    void setPasswordError();

    void navigateToHome();

    void showMessage(String message);

    Context getContext();
}
