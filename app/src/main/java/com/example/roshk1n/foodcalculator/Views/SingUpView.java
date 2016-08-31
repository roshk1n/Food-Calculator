package com.example.roshk1n.foodcalculator.views;

import android.graphics.Bitmap;

/**
 * Created by roshk1n on 7/16/2016.
 */
public interface SingUpView {

    void setUserPhoto(Bitmap bitmap);

    Bitmap getBitmapIv();

    void showToast(String message);

    void navigateToLogin();
}
