package com.example.roshk1n.foodcalculator.presenters;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.example.roshk1n.foodcalculator.DataManager;
import com.example.roshk1n.foodcalculator.Views.SingUpView;
import com.example.roshk1n.foodcalculator.interfaces.DataSingUpCallback;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class SingUpPresenterImpl implements SingUpPresenter, DataSingUpCallback {
    private SingUpView singUpView;
    private DataManager dataManager = new DataManager(this);

    @Override
    public void setView(SingUpView view) {
        singUpView = view;
    }

    @Override
    public void singUp(final String fullname, final String email, final String password, final String confirmPassword) {
        boolean error = false;

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(fullname)
                || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            singUpView.showToast("Enter all fields, please.");
            error = true;

        }   else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                error = true;
                singUpView.showToast("Your email is incorrect.");

        } else if (!password.equals(confirmPassword)) {
            singUpView.showToast("Password and confirm password don`t match.");
            error = true;

        } else if(password.length()<6) {
            singUpView.showToast("Your password must be at least 6 characters long.");
            error = true;

        }

        if (!error) {
            final Bitmap imageUser = singUpView.getBitmapIv();
            dataManager.createUser(email, password, fullname, imageUser);
        }
    }

    @Override
    public void setUserPhotoSD(Intent data, ContentResolver context) {
        InputStream inputStream = null;
        try {
            inputStream = context.openInputStream(data.getData());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap photo = BitmapFactory.decodeStream(inputStream);

        singUpView.setUserPhoto(photo);
    }

    @Override
    public void setUserPhotoCamera(Intent data) {
        Bitmap photo = (Bitmap) data.getExtras().get("data");
        singUpView.setUserPhoto(photo);
    }

    @Override
    public void createUserSuccess() {
        singUpView.navigateToLogin();
    }

    @Override
    public void createUserError(String message) {
        singUpView.showToast(message);
    }
}
