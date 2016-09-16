package com.example.roshk1n.foodcalculator.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

public interface SingUpView {
    void setUserPhoto(Bitmap bitmap);

    Bitmap getBitmapIv();

    void showToast(String message);

    void navigateToLogin();

    Context getContext();

    Activity getActivity();

    void setEmailError(String message);

    void setPasswordError(String message);

    void setConfirmError(String message);

    void serFullNameError(String message);
}

