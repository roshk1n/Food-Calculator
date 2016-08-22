package com.example.roshk1n.foodcalculator.presenters;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;

import com.example.roshk1n.foodcalculator.Views.ProfileView;
import com.example.roshk1n.foodcalculator.interfaces.UserProfileCallback;
import com.example.roshk1n.foodcalculator.realm.UserRealm;

/**
 * Created by roshk1n on 8/5/2016.
 */

public interface ProfilePresenter {

    void setView (ProfileView view);

    void loadUser();

    Bitmap stringToBitmap(String photoUrl);

    void setUserPhotoSD(Intent data, ContentResolver context);

    void setUserPhotoCamera(Intent data);

    void updateUserProfile(String fullname, String weight, String height, String age, String email,Bitmap image, String sex, String active_level);

    int getPositionInArray(String active, String [] array);
}
