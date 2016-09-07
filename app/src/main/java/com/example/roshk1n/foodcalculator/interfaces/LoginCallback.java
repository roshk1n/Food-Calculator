package com.example.roshk1n.foodcalculator.interfaces;

import android.content.Context;

public interface LoginCallback {
    void loginError(String s);

    void loginSuccess();

    Context getContext();
}
