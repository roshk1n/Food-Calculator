package com.example.roshk1n.foodcalculator.presenters;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.roshk1n.foodcalculator.Session;
import com.example.roshk1n.foodcalculator.Views.ProfileView;
import com.example.roshk1n.foodcalculator.presenters.ProfilePresenter;
import com.example.roshk1n.foodcalculator.realm.UserRealm;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import io.realm.Realm;

public class ProfilePresenterImpl implements ProfilePresenter {

    private final Realm realm = Realm.getDefaultInstance();

    private final String SEX_NONE = "None";
    private final String SEX_MALE = "Male";
    private final String SEX_FEMALE = "Female";

    private final String NONE__LEVEL = "None";
    private final String NOT_VERY_ACTIVE_LEVEL = "Not Very Active";
    private final String LIGHTLY_ACTIVE_LEVEL = "Lightly Active";
    private final String ACTIVE_LEVEL = "Active";
    private final String VERY_ACTIVE_LEVEL = "Very Active";


    private ProfileView profileView;

    @Override
    public void setView(ProfileView view) {
        profileView = view;
    }

    @Override
    public void getUserProfile() {

        profileView.setProfile(getCurrentUserRealm().getPhotoUrl(),getCurrentUserRealm().getEmail(),
                getCurrentUserRealm().getFullname(),getCurrentUserRealm().getAge(),getCurrentUserRealm().getHeight(),
                getCurrentUserRealm().getWeight(),getCurrentUserRealm().getSex(),getCurrentUserRealm().getActiveLevel());
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
                                  final Bitmap image,
                                  final String sex,
                                  final String active_level) {

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
                getCurrentUserRealm().setSex(sex);
                getCurrentUserRealm().setActiveLevel(active_level);
            }
        });
        Session.getInstance().setEmail(email);
        Session.getInstance().setFullname(fullname);
        Session.getInstance().setUrlPhoto(image_profile);

        profileView.CompleteUpdateAndRefreshDrawer();
    }

    @Override
    public void updateLimitCalories() {
        short maleOrFemaleCof = 0;
        float activeLevelCof = 1;// none active

        switch (getCurrentUserRealm().getSex()) {
            case SEX_NONE : {
                maleOrFemaleCof = 0;
            }
            case SEX_MALE : {
                maleOrFemaleCof = 5;

            } break;
            case SEX_FEMALE : {
                maleOrFemaleCof = -161;
            }
        }

        switch (getCurrentUserRealm().getActiveLevel()) {
            case NONE__LEVEL : {
                activeLevelCof = 1f;
            } break;
            case NOT_VERY_ACTIVE_LEVEL : {
                activeLevelCof = 1.2f;
            } break;

            case LIGHTLY_ACTIVE_LEVEL : {
                activeLevelCof = 1.375f;
            }break;
            case  ACTIVE_LEVEL :{
                activeLevelCof = 1.6375f;
            } break;
            case VERY_ACTIVE_LEVEL: {
                activeLevelCof = 1.9f;
            }break;

        }
        float goalCaloriesFloat = (10*getCurrentUserRealm().getWeight() +
                6.25f*getCurrentUserRealm().getHeight() -
                5*getCurrentUserRealm().getAge() + maleOrFemaleCof)*activeLevelCof ; // calculate limit

        final int goalCalories = Math.round(goalCaloriesFloat);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                getCurrentUserRealm().setGoalCalories(goalCalories);
            }
        });
    }

    @Override
    public int getPositionInArray(String active, String [] array) {
        int position = 0;
        for (int i= 0; i<array.length;i++) {
            if(array[i].equals(active)) {
                position = i;
            }
        }
        return position;
    }

    private String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, outputStream);
        byte [] b = outputStream.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
}
