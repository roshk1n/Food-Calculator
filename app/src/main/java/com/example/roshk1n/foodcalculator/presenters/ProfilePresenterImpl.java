package com.example.roshk1n.foodcalculator.presenters;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.manageres.DataManager;
import com.example.roshk1n.foodcalculator.views.ProfileView;
import com.example.roshk1n.foodcalculator.interfaces.OnCompleteCallback;
import com.example.roshk1n.foodcalculator.interfaces.UserProfileCallback;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.User;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ProfilePresenterImpl implements ProfilePresenter {
    private DataManager dataManager = new DataManager();
    private final String SEX_NONE = "None";
    private final String SEX_MALE = "Male";
    private final String SEX_FEMALE = "Female";

    private final String NONE__LEVEL = "None";
    private final String NOT_VERY_ACTIVE_LEVEL = "Not Very Active";
    private final String LIGHTLY_ACTIVE_LEVEL = "Lightly Active";
    private final String ACTIVE_LEVEL = "Active";
    private final String VERY_ACTIVE_LEVEL = "Very Active";
    private boolean checkLocal = true;

    private ProfileView profileView;

    @Override
    public void setView(ProfileView view) {
        profileView = view;
    }

    @Override
    public void loadUser() {
        dataManager.loadUserProfile(profileView.getContext(), new UserProfileCallback() {
            @Override
            public void loadProfileSuccess(final User user) {
                if(checkLocal) {
                    Bitmap image = stringToBitmap(user.getPhotoUrl());
                    profileView.setProfile(image, user.getEmail(), user.getFullname(), user.getAge(),
                            user.getHeight(), user.getWeight(), user.getSex(), user.getActiveLevel());
                    checkLocal = false;

                } else {
                    Glide
                        .with(profileView.getContext())
                        .load(user.getPhotoUrl())
                        .asBitmap()
                        .fitCenter()
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                profileView.setProfile(resource, user.getEmail(), user.getFullname(), user.getAge(),
                                        user.getHeight(), user.getWeight(), user.getSex(), user.getActiveLevel());
                            }
                        });
                }
            }
        });
    }

    @Override
    public Bitmap stringToBitmap(String photoUrl) {
        Bitmap imageUser = null;
        try {
            byte[] encodeByte = Base64.decode(photoUrl, Base64.DEFAULT);
            imageUser = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch (Exception e) {
            e.getMessage();
        }
        return imageUser;
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
        profileView.setUserPhoto(scaleBitmap(photo,500));
    }

    @Override
    public void setUserPhotoCamera(Intent data) {
        Bitmap photo = (Bitmap) data.getExtras().get("data");
        profileView.setUserPhoto(scaleBitmap(photo,500));
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

          if (!fullname.isEmpty() && !weight.isEmpty() && !height.isEmpty()
                && !age.isEmpty() && !email.isEmpty()) {
              if(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                  final String image_profile = bitmapToString(image);//convert to string
                  int goalCalories = updateLimitCalories(sex, active_level, weight, height, age);
                  User user = new User();
                  user.setFullname(fullname);
                  user.setWeight(Integer.parseInt(weight));
                  user.setHeight(Integer.parseInt(height));
                  user.setAge(Integer.parseInt(age));
                  user.setEmail(email);
                  user.setPhotoUrl(image_profile);
                  user.setSex(sex);
                  user.setActiveLevel(active_level);
                  user.setGoalCalories(goalCalories);
                  dataManager.updateUserProfile(user, image, new OnCompleteCallback() {
                      @Override
                      public void success() {
                          profileView.CompleteUpdateAndRefreshDrawer();
                      }
                  });
              } else {
                  profileView.showSnackBar(profileView.getContext().getString(R.string.email_incorrect));
              }
        } else {
            profileView.showSnackBar(profileView.getContext().getString(R.string.enter_all_field));
        }
    }

    @Override
    public int getPositionInArray(String active, String[] array) {
        int position = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(active)) {
                position = i;
            }
        }
        return position;
    }

    @Override
    public void destroy() {
        profileView = null;
    }

    public void getPermissions() {
        if (ContextCompat.checkSelfPermission(profileView.getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(profileView.getContext(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(profileView.getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            }
            if (ActivityCompat.shouldShowRequestPermissionRationale(profileView.getActivity(),
                    Manifest.permission.CAMERA)) {
            } else {
                ActivityCompat.requestPermissions(profileView.getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                ActivityCompat.requestPermissions(profileView.getActivity(),
                        new String[]{Manifest.permission.CAMERA}, 2);

            }
        }
    }

    private int updateLimitCalories(String sex, String active_level, String _weight, String _height, String _age) {
        short maleOrFemaleCof = 0;
        float activeLevelCof = 1;// none active
        int weight = Integer.parseInt(_weight);
        int height = Integer.parseInt(_height);
        int age = Integer.parseInt(_age);
        switch (sex) {
            case SEX_NONE: {
                maleOrFemaleCof = 0;
            }
            case SEX_MALE: {
                maleOrFemaleCof = 5;

            }
            break;
            case SEX_FEMALE: {
                maleOrFemaleCof = -161;
            }
        }

        switch (active_level) {
            case NONE__LEVEL: {
                activeLevelCof = 1f;
            }
            break;
            case NOT_VERY_ACTIVE_LEVEL: {
                activeLevelCof = 1.2f;
            }
            break;

            case LIGHTLY_ACTIVE_LEVEL: {
                activeLevelCof = 1.375f;
            }
            break;
            case ACTIVE_LEVEL: {
                activeLevelCof = 1.6375f;
            }
            break;
            case VERY_ACTIVE_LEVEL: {
                activeLevelCof = 1.9f;
            }
            break;

        }
        float goalCaloriesFloat = (10 * weight + 6.25f * height - 5 * age + maleOrFemaleCof) * activeLevelCof; // calculate limit

        return Math.round(goalCaloriesFloat);
    }

    private String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] b = outputStream.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    private Bitmap scaleBitmap(Bitmap bitmapToScale, float maxImageSize) {
        float ratio = Math.min(maxImageSize / bitmapToScale.getWidth(),
                maxImageSize / bitmapToScale.getHeight());
        int width = Math.round(ratio * bitmapToScale.getWidth());
        int height = Math.round(ratio * bitmapToScale.getHeight());

        return Bitmap.createScaledBitmap(bitmapToScale, width, height, true);
    }
}
