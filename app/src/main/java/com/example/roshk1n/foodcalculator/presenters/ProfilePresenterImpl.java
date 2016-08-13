package com.example.roshk1n.foodcalculator.presenters;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.roshk1n.foodcalculator.LocalDataBaseManager;
import com.example.roshk1n.foodcalculator.Session;
import com.example.roshk1n.foodcalculator.Views.ProfileView;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.User;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ProfilePresenterImpl implements ProfilePresenter {

    private LocalDataBaseManager localDataBaseManager = new LocalDataBaseManager();
    private User user;
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
    public void loadUser() {
        user = localDataBaseManager.loadUser();
        profileView.setProfile(user.getPhotoUrl(), user.getEmail(), user.getFullname(), user.getAge(),
                user.getHeight(), user.getWeight(), user.getSex(), user.getActiveLevel());
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
        int goalCalories = updateLimitCalories(sex,active_level,weight,height,age);
        user.setFullname(fullname);
        user.setWeight(Integer.parseInt(weight));
        user.setHeight(Integer.parseInt(height));
        user.setAge(Integer.parseInt(age));
        user.setEmail(email);
        user.setPhotoUrl(image_profile);
        user.setSex(sex);
        user.setActiveLevel(active_level);
        user.setGoalCalories(goalCalories);
        localDataBaseManager.updateUserProfile(user);

        Session.getInstance().setEmail(email);
        Session.getInstance().setFullname(fullname);
        Session.getInstance().setUrlPhoto(image_profile);

        profileView.CompleteUpdateAndRefreshDrawer();
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

    private int updateLimitCalories(String sex, String active_level, String _weight, String _height, String _age ) {
        short maleOrFemaleCof = 0;
        float activeLevelCof = 1;// none active
        int weight = Integer.parseInt(_weight);
        int height = Integer.parseInt(_height);
        int age = Integer.parseInt(_age);
        switch (sex) {
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

        switch (active_level) {
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
        float goalCaloriesFloat = (10 * weight + 6.25f * height - 5 * age + maleOrFemaleCof)*activeLevelCof; // calculate limit

        return Math.round(goalCaloriesFloat);
    }

    private String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, outputStream);
        byte [] b = outputStream.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
}
