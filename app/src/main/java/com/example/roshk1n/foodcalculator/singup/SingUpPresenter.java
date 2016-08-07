package com.example.roshk1n.foodcalculator.singup;

import android.content.ContentResolver;
import android.content.Intent;

public interface SingUpPresenter {

    void setView(SingUpView view);

    void singUpFirebase(String surname, String email, String password, String congirmPassword);

    void setUserPhotoSD(Intent data, ContentResolver context);

    void setUserPhotoCamera(Intent data);

    void singUpRealm(String fullname, String email, String password, String confirmPassword);
}
