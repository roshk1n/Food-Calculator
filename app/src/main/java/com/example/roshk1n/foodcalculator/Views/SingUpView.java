package com.example.roshk1n.foodcalculator.views;

import android.content.Context;
import android.graphics.Bitmap;

public interface SingUpView {

    void setUserPhoto(Bitmap bitmap);

    Bitmap getBitmapIv();

    void showToast(String message);

    void navigateToLogin();

    Context getContext();
}
