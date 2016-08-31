package com.example.roshk1n.foodcalculator.views;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by roshk1n on 8/5/2016.
 */

public interface ProfileView {

    void setProfile(String photoUrl,
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
}
