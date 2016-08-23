package com.example.roshk1n.foodcalculator;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.roshk1n.foodcalculator.interfaces.CreateUserFirebaseCallback;
import com.example.roshk1n.foodcalculator.interfaces.DataAddFoodCallback;
import com.example.roshk1n.foodcalculator.interfaces.DataDiaryCallback;
import com.example.roshk1n.foodcalculator.interfaces.DataFavoriteCallback;
import com.example.roshk1n.foodcalculator.interfaces.StateItemCallback;
import com.example.roshk1n.foodcalculator.interfaces.DataLoginCallback;
import com.example.roshk1n.foodcalculator.interfaces.DataSingUpCallback;
import com.example.roshk1n.foodcalculator.interfaces.FirebaseCallback;
import com.example.roshk1n.foodcalculator.interfaces.LoadDayCallback;
import com.example.roshk1n.foodcalculator.interfaces.LocalManagerCallback;
import com.example.roshk1n.foodcalculator.interfaces.ResponseListentenerUpload;
import com.example.roshk1n.foodcalculator.interfaces.OnCompleteCallback;
import com.example.roshk1n.foodcalculator.interfaces.UserProfileCallback;
import com.example.roshk1n.foodcalculator.remoteDB.FirebaseHelper;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Day;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.User;
import com.example.roshk1n.foodcalculator.utils.Utils;
import com.facebook.AccessToken;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;

public class DataManager implements FirebaseCallback, LocalManagerCallback {
    private FirebaseHelper firebaseHelper = FirebaseHelper.getInstance(this);
    private DataLoginCallback dataLoginCallback;
    private DataSingUpCallback dataSingUpCallback;
    private DataDiaryCallback dataDiaryCallback;
    private DataAddFoodCallback dataAddFoodCallback;
    private DataFavoriteCallback dataFavoriteCallback;

    public DataManager() {
    }

    public DataManager(DataLoginCallback dataLoginCallback) {
        this.dataLoginCallback = dataLoginCallback;
    }

    public DataManager(DataSingUpCallback dataSingUpCallback) {
        this.dataSingUpCallback = dataSingUpCallback;
    }

    public DataManager(DataDiaryCallback dataDiaryCallback) {
        this.dataDiaryCallback = dataDiaryCallback;
    }

    public DataManager(DataAddFoodCallback dataAddFoodCallback) {
        this.dataAddFoodCallback = dataAddFoodCallback;
    }

    public DataManager(DataFavoriteCallback dataFavoriteCallback) {
        this.dataFavoriteCallback = dataFavoriteCallback;
    }


    public void createUser(final String email, final String password, final String fullname, final Bitmap imageUser) {
        firebaseHelper.uploadImage(imageUser, email, new ResponseListentenerUpload() {
            @Override
            public void onSuccess(final String url) {
                firebaseHelper.createUser(email, password, fullname, url, new CreateUserFirebaseCallback() {
                    @Override
                    public void createSuccess() {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream(); //Bitmap to Base64
                        imageUser.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] b = baos.toByteArray();
                        String image = Base64.encodeToString(b, Base64.DEFAULT);

                        LocalDataBaseManager.createUser(fullname, email, image);
                        dataSingUpCallback.createUserSuccess();
                    }

                    @Override
                    public void createError(String message) {
                        dataSingUpCallback.createUserError(message);
                    }
                });
            }
        });
    }

    public void login(String email, String password) {
        if (Utils.isConnectNetwork(dataLoginCallback.getContext())) {
            firebaseHelper.logInWithEmail(email, password);
        } else {
            dataLoginCallback.showToast("Error: No internet connection.");
        }
    }

    public void checkLogin() {
        firebaseHelper.checkLogin();
    }

    @Override
    public void loginSuccessful() {
        LocalDataBaseManager.checkLocalUser();
        dataLoginCallback.navigateToHome();
    }

    @Override
    public void showToast(String text) {
        dataLoginCallback.showToast(text);
    }

    @Override
    public void setExistFavorite(final boolean checkExistFood) {
        new DataAddFoodCallback() {
            @Override
            public void setExistFavorite(boolean existInFavotite) {
                dataAddFoodCallback.setExistFavorite(checkExistFood);
            }
        };
    }

    public void loadDayData(final Date date, final LoadDayCallback callback) {
        Day day = LocalDataBaseManager.loadDayData(date);
        callback.loadComplete(day);

        firebaseHelper.loadDay(date, new LoadDayCallback() { //TODO need new thread
            @Override
            public void loadComplete(Day dayFire) {
                callback.loadComplete(dayFire);
                LocalDataBaseManager.updateDays(dayFire);
            }
        });
    }

    public void removeFood(int index, long time) {
        LocalDataBaseManager.removeFood(index);
        firebaseHelper.removeFood(time);
    }

    public void addFood(Food food) {
        firebaseHelper.addFood(food);
    }

    public int loadGoalCalories() {
        int goal = LocalDataBaseManager.loadGoalCalories();
        //dataDiaryCallback.loadCaloriesSuccess(goal);
        return goal;
    }

    public void updateCalories(int eat_calories, int remainingCalories, long date) {
        LocalDataBaseManager.updateCalories(eat_calories, remainingCalories);
    }

    public void addFavoriteFood(Food food, StateItemCallback callback) {
        //callback.updateImageFavorite(LocalDataBaseManager.addFavoriteFood(food););
        firebaseHelper.addFavoriteFood(food, callback);
    }

    public void isExistInFavorite(Food food, DataAddFoodCallback callback) {
        //  dataAddFoodCallback.setExistFavorite(LocalDataBaseManager.isExistInFavotite(food)); //null intargace for firebase
        firebaseHelper.isExistInFavorite(food, callback);
    }

    public void removeFavoriteFoodDB(String ndbno, StateItemCallback callback) {
        // callback.updateImageFavorite(LocalDataBaseManager.removeFavoriteFoodDB(ndbno));
        firebaseHelper.removeFavoriteFood(ndbno, callback);
    }

    public void removeFavoriteFoodDB(int position, String ndbno) {
        // LocalDataBaseManager.removeFavoriteFoodDB(position);
        firebaseHelper.removeFavoriteFood(ndbno);
    }

    public void loadFavoriteList(final DataFavoriteCallback callback) {
        //callback.setFavoriteList(LocalDataBaseManager.loadFavoriteFood());
        firebaseHelper.loadFavoriteFood(new DataFavoriteCallback() {
            @Override
            public void setFavoriteList(ArrayList<Food> favFoods) {
                callback.setFavoriteList(favFoods);
            }
        });
    }

    public void loadUserProfile(final Context context, final UserProfileCallback callback) {
        callback.loadProfileSuccess(LocalDataBaseManager.loadUser());
        if (Utils.isConnectNetwork(context)) {
            firebaseHelper.loadUserProfile(new UserProfileCallback() {
                @Override
                public void loadProfileSuccess(final User user) { // load remote user and update local
                    Glide
                            .with(context)
                            .load(user.getPhotoUrl())
                            .asBitmap()
                            .centerCrop()
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                    user.setPhotoUrl(bitmapToString(resource));
                                    LocalDataBaseManager.updateUserProfile(user);
                                    callback.loadProfileSuccess(user);
                                }
                            });
                }
            });
        }
    }

    private String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] b = outputStream.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public void updateUserProfile(User user, Bitmap image, OnCompleteCallback callback) {
        LocalDataBaseManager.updateUserProfile(user);

        firebaseHelper.updateUserProfile(user, image, callback);
    }

    public void updateInfoUser(final String image) {
        firebaseHelper.loadUserProfile(new UserProfileCallback() {
            @Override
            public void loadProfileSuccess(User user) {
                user.setPhotoUrl(image);
                LocalDataBaseManager.updateUserProfile(user);
            }
        });
    }

    public void loginFacebook(AccessToken access, final JSONObject object, final OnCompleteCallback callback) {
        firebaseHelper.loginFacebook(access, new CreateUserFirebaseCallback() {
            @Override
            public void createSuccess() {
                firebaseHelper.createProfileFacebook(object,callback);
            }

            @Override
            public void createError(String message) {
                dataLoginCallback.showToast(message);
            }
        });
    }
}
