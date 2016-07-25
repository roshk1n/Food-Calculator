package com.example.roshk1n.foodcalculator.singup;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by roshk1n on 7/16/2016.
 */
public interface SingUpPresenter {

    void setView(SingUpView view);

    void singUpFirebase(String surname, String email, String password, String congirmPassword);

    void setUserPhotoSD(Intent data, ContentResolver context);

    void setUserPhotoCamera(Intent data);

    void singUpRealm(String fullname, String email, String password, String confirmPassword);
}
