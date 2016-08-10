package com.example.roshk1n.foodcalculator.presenters;

import android.content.ContentResolver;
import android.content.Intent;

import com.example.roshk1n.foodcalculator.Views.SingUpView;

public interface SingUpPresenter {

    void setView(SingUpView view);

    void singUpFirebase(String surname, String email, String password, String congirmPassword);

    void setUserPhotoSD(Intent data, ContentResolver context);

    void setUserPhotoCamera(Intent data);

    void singUpRealm(String fullname, String email, String password, String confirmPassword);
}
