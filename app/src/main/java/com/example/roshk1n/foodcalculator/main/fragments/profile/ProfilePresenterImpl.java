package com.example.roshk1n.foodcalculator.main.fragments.profile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.roshk1n.foodcalculator.Session;
import com.example.roshk1n.foodcalculator.realm.UserRealm;

import io.realm.Realm;

/**
 * Created by roshk1n on 8/5/2016.
 */

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
    public void updateUserProfile(final String fullname,
                                  final String weight,
                                  final String height,
                                  final String age,
                                  final String email) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                getCurrentUserRealm().setFullname(fullname);
                getCurrentUserRealm().setWeight(Integer.parseInt(weight));
                getCurrentUserRealm().setHeight(Integer.parseInt(height));
                getCurrentUserRealm().setAge(Integer.parseInt(age));
                getCurrentUserRealm().setEmail(email);
            }
        });
        Session.getInstance().setEmail(email);
        Session.getInstance().setFullname(fullname);
    }
//TODO return message

}
