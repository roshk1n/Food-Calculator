package com.example.roshk1n.foodcalculator.singup;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.example.roshk1n.foodcalculator.User;
import com.example.roshk1n.foodcalculator.realm.FoodRealm;
import com.example.roshk1n.foodcalculator.realm.UserRealm;
import com.example.roshk1n.foodcalculator.remoteDB.FirebaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;

import io.realm.Realm;

/**
 * Created by roshk1n on 7/16/2016.
 */
public class SingUpPresenterImpl implements SingUpPresenter {
    private SingUpView singUpView;

    private User user;

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    @Override
    public void setView(SingUpView view) { singUpView = view; }

    @Override
    public void singUpFirebase(final String surname, final String email, final String password, final String congirmPassword) {
        boolean error = false;
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(surname)
                || TextUtils.isEmpty(password) || TextUtils.isEmpty(congirmPassword)) {
            singUpView.showToast("Enter all fields, please.");

        } else if(!password.equals(congirmPassword)) {
            singUpView.showToast("Password and confirm password don`t match.");
        } else {
            final Bitmap imageUser = singUpView.getBitmapIv();

// TODO: waiting for upload
            FirebaseHelper.uploadImage(imageUser, email);
            user = new User(surname, email, password, FirebaseHelper.getUrlUserPhto());
            if (FirebaseHelper.getUrlUserPhto() != null) {
                FirebaseHelper.createUser(user);
                singUpView.navigateToHome();
            }

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
    public void singUpRealm(String fullname, String email, String password, String confirmPassword) {
        boolean error = false;
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(fullname)
                || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            singUpView.showToast("Enter all fields, please.");

        } else if(!password.equals(confirmPassword)) {
            singUpView.showToast("Password and confirm password don`t match.");
        } else {
            Realm realm = Realm.getDefaultInstance();
            UserRealm userRealm = new UserRealm(fullname,email,password);
            realm.beginTransaction();
            realm.copyToRealm(userRealm);
            realm.commitTransaction();
            singUpView.navigateToLogin();
        }
    }
}
