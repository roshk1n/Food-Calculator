package com.example.roshk1n.foodcalculator.views;

import android.content.Context;
import android.graphics.Bitmap;

public interface ProfileView {
    void setProfile(Bitmap bitImage,
                    String email,
                    String fullname,
                    int age,
                    int height,
                    int weight,
                    String sex,
                    String activeLevel);

    void setUserPhoto(Bitmap bitmap);

    void CompleteUpdateAndRefreshDrawer();

    Context getContext();

    void showToast(String s);
}
