package com.example.roshk1n.foodcalculator.presenters;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;

import com.example.roshk1n.foodcalculator.User;
import com.example.roshk1n.foodcalculator.Views.SingUpView;
import com.example.roshk1n.foodcalculator.realm.UserRealm;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import io.realm.Realm;

public class SingUpPresenterImpl implements SingUpPresenter {
    private SingUpView singUpView;

    private User user;

    @Override
    public void setView(SingUpView view) {
        singUpView = view;
    }

    @Override
    public void singUpFirebase(final String fullname, final String email, final String password, final String congirmPassword) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(fullname)
                || TextUtils.isEmpty(password) || TextUtils.isEmpty(congirmPassword)) {
            singUpView.showToast("Enter all fields, please.");

        } else if (!password.equals(congirmPassword)) {
            singUpView.showToast("Password and confirm password don`t match.");
        } else {
            final Bitmap imageUser = singUpView.getBitmapIv();
/*
            FirebaseHelper.uploadImage(imageUser, email, new ResponseListentenerUpload() {
                @Override
                public void onSuccess(String urlPhoto) {
                    user = new User(fullname, email, password, urlPhoto);
                    FirebaseHelper.createUser(user);

                    Realm realm = Realm.getDefaultInstance();

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imageUser.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] b = baos.toByteArray();
                    String image = Base64.encodeToString(b, Base64.DEFAULT);

                    UserRealm userRealm = new UserRealm(fullname, email, password, image, "none", "none");
                    realm.beginTransaction();
                    realm.copyToRealm(userRealm);
                    realm.commitTransaction();
                    singUpView.navigateToLogin();
                }
            });*/
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
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(fullname)
                || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            singUpView.showToast("Enter all fields, please.");

        } else if (!password.equals(confirmPassword)) {
            singUpView.showToast("Password and confirm password don`t match.");
        } else {
            Realm realm = Realm.getDefaultInstance();

            Bitmap userIco = singUpView.getBitmapIv();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            userIco.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            String image = Base64.encodeToString(b, Base64.DEFAULT);

            UserRealm userRealm = new UserRealm(fullname, email, password, image, "none", "none");
            realm.beginTransaction();
            realm.copyToRealm(userRealm);
            realm.commitTransaction();
            singUpView.navigateToLogin();
        }
    }
}
