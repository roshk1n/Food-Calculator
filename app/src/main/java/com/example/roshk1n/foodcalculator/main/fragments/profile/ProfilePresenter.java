package com.example.roshk1n.foodcalculator.main.fragments.profile;

import android.graphics.Bitmap;

import com.example.roshk1n.foodcalculator.realm.UserRealm;

/**
 * Created by roshk1n on 8/5/2016.
 */

public interface ProfilePresenter {

    void setView (ProfileView view);

    void getUserProfile();

    UserRealm getCurrentUserRealm();

    Bitmap stringToBitmap(String photoUrl);

    void updateUserProfile(String fullname, String weight, String height, String age, String email);
}
