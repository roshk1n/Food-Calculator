package com.example.roshk1n.foodcalculator.main.fragments.profile;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.roshk1n.foodcalculator.Session;
import com.example.roshk1n.foodcalculator.realm.UserRealm;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import io.realm.Realm;

public class ProfilePresenterImpl implements ProfilePresenter {

    private final Realm realm = Realm.getDefaultInstance();

    private ProfileView profileView;

    @Override
    public void setView(ProfileView view) {
        profileView = view;
    }

    @Override
    public void getUserProfile() {

        profileView.setProfile(getCurrentUserRealm().getPhotoUrl(),getCurrentUserRealm().getEmail(),
                getCurrentUserRealm().getFullname(),getCurrentUserRealm().getAge(),getCurrentUserRealm().getHeight(),
                getCurrentUserRealm().getWeight(),getCurrentUserRealm().getActiveLevel());
    }

    @Override
    public UserRealm getCurrentUserRealm() {
        return realm.where(UserRealm.class)
                .equalTo("email", Session.getInstance().getEmail())
                .findFirst();
    }

    @Override
    public Bitmap stringToBitmap(String photoUrl) {
        Bitmap image = null;
        try {
            byte [] encodeByte= Base64.decode(Session.getInstance().getUrlPhoto(), Base64.DEFAULT);
            image = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch(Exception e) {
            e.getMessage();
        }
        return image;
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
        profileView.setUserPhoto(photo);
    }

    @Override
    public void setUserPhotoCamera(Intent data) {
        Bitmap photo = (Bitmap) data.getExtras().get("data");
        profileView.setUserPhoto(photo);
    }

    @Override
    public void updateUserProfile(final String fullname,
                                  final String weight,
                                  final String height,
                                  final String age,
                                  final String email,
                                  final Bitmap image) {

        String image_profile = bitmapToString(image);//convert to string
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                getCurrentUserRealm().setFullname(fullname);
                getCurrentUserRealm().setWeight(Integer.parseInt(weight));
                getCurrentUserRealm().setHeight(Integer.parseInt(height));
                getCurrentUserRealm().setAge(Integer.parseInt(age));
                getCurrentUserRealm().setEmail(email);
                getCurrentUserRealm().setPhotoUrl(bitmapToString(image));
            }
        });
        Session.getInstance().setEmail(email);
        Session.getInstance().setFullname(fullname);
        Session.getInstance().setUrlPhoto(image_profile);

        profileView.CompleteUpdateAndRefreshDrawer();
    }

    private String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, outputStream);
        byte [] b = outputStream.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
}
