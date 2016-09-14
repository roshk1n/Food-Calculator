package com.example.roshk1n.foodcalculator.manageres;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Patterns;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.roshk1n.foodcalculator.Session;
import com.example.roshk1n.foodcalculator.interfaces.CreateUserFirebaseCallback;
import com.example.roshk1n.foodcalculator.interfaces.DataAddFoodCallback;
import com.example.roshk1n.foodcalculator.interfaces.DataFavoriteCallback;
import com.example.roshk1n.foodcalculator.interfaces.FirebaseCallback;
import com.example.roshk1n.foodcalculator.interfaces.LoadDaysCallback;
import com.example.roshk1n.foodcalculator.interfaces.StateItemCallback;
import com.example.roshk1n.foodcalculator.interfaces.LoginCallback;
import com.example.roshk1n.foodcalculator.interfaces.DataSingUpCallback;
import com.example.roshk1n.foodcalculator.interfaces.LoadDayCallback;
import com.example.roshk1n.foodcalculator.interfaces.ResponseListenerUpload;
import com.example.roshk1n.foodcalculator.interfaces.OnCompleteCallback;
import com.example.roshk1n.foodcalculator.interfaces.UserProfileCallback;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Day;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.User;
import com.example.roshk1n.foodcalculator.utils.Utils;
import com.facebook.AccessToken;

import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class DataManager implements FirebaseCallback {
    private FirebaseManager firebaseManager = FirebaseManager.getInstance(this);

    private LoginCallback loginCallback;
    private DataSingUpCallback dataSingUpCallback;

    public DataManager() {
    }

    public DataManager(LoginCallback loginCallback) {
        this.loginCallback = loginCallback;
    }

    public DataManager(DataSingUpCallback dataSingUpCallback) {
        this.dataSingUpCallback = dataSingUpCallback;
    }

    public void createUser(final String email, final String password, final String fullname, final Bitmap imageUser) {
        firebaseManager.uploadImage(imageUser, email, new ResponseListenerUpload() {
            @Override
            public void onSuccess(final String url) {
                firebaseManager.createUser(email, password, fullname, url, new CreateUserFirebaseCallback() {
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
        if (Utils.isConnectNetwork(loginCallback.getContext())) {
            firebaseManager.logInWithEmail(email, password);
        } else {
            loginCallback.loginError("Error: No internet connection.");
        }
    }

    public void checkLogin() {
        firebaseManager.checkLogin(loginCallback);
    }

    @Override
    public void loginSuccessful() {
        LocalDataBaseManager.checkLocalUser(new OnCompleteCallback() {
            @Override
            public void success() {
                loginCallback.loginSuccess();
            }
        });
    }

    @Override
    public void loginError(String text) {
        loginCallback.loginError(text);
    }

    public void loadDayData(final Calendar date, final LoadDayCallback callback) {
        Day day = LocalDataBaseManager.loadDayData(date);
        callback.loadComplete(day);

        firebaseManager.loadDay(date, new LoadDayCallback() { //TODO need new thread
            @Override
            public void loadComplete(Day dayFire) {
                LocalDataBaseManager.updateDays(dayFire);
                callback.loadComplete(dayFire);
            }
        });
    }

    public void removeFood(int index, long time) {
        LocalDataBaseManager.removeFood(index);
        firebaseManager.removeFood(time);
    }

    public void addFood(Food food) {
        firebaseManager.addFood(food);
    }

    public int loadGoalCalories() {
        //dataDiaryCallback.loadCaloriesSuccess(goal);
        return LocalDataBaseManager.loadGoalCalories();
    }

    public void updateCalories(int eat_calories, int remainingCalories, long date) {
        LocalDataBaseManager.updateCalories(eat_calories, remainingCalories);
        firebaseManager.updateCalories(eat_calories,remainingCalories,date);

    }

    public void addFavoriteFood(Food food, StateItemCallback callback) {
        firebaseManager.addFavoriteFood(food, callback);
    }

    public void isExistInFavorite(Food food, DataAddFoodCallback callback) {
        firebaseManager.isExistInFavorite(food, callback);
    }

    public void removeFavoriteFoodDB(String ndbno, StateItemCallback callback) {
        firebaseManager.removeFavoriteFood(ndbno, callback);
    }

    public void loadFavoriteList(final DataFavoriteCallback callback) {
        firebaseManager.loadFavoriteFood(new DataFavoriteCallback() {
            @Override
            public void setFavoriteList(ArrayList<Food> favFoods) {
                callback.setFavoriteList(favFoods);
            }
        });
    }

    public void loadUserProfile(final Context context, final UserProfileCallback callback) {
        callback.loadProfileSuccess(LocalDataBaseManager.loadUser());
        if (Utils.isConnectNetwork(context)) {
            firebaseManager.loadUserProfile(new UserProfileCallback() {
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
                                    callback.loadProfileSuccess(user);
                                    user.setPhotoUrl(bitmapToString(resource));
                                    LocalDataBaseManager.updateUserProfile(user);
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
        firebaseManager.updateUserProfile(user, image, callback);
    }

    public void updateInfoUser(final String imageString) {
        firebaseManager.loadUserProfile(new UserProfileCallback() {
            @Override
            public void loadProfileSuccess(User user) {
                user.setPhotoUrl(imageString);
                LocalDataBaseManager.updateUserProfile(user);
            }
        });
    }

    public void loginFacebook(AccessToken access, final JSONObject object, final OnCompleteCallback callback) {
        firebaseManager.loginFacebook(access, new CreateUserFirebaseCallback() {
            @Override
            public void createSuccess() {
                firebaseManager.createProfileFacebook(object, new UserProfileCallback() {
                    @Override
                    public void loadProfileSuccess(User user) {
                        LocalDataBaseManager.checkFacebookLocalUser(user,callback);
                    }
                });
            }

            @Override
            public void createError(String message) {
                loginCallback.loginError(message);
            }
        });
    }

    public void loadDataForChart(LoadDaysCallback callback) {
           firebaseManager.loadDataForChart(callback);
    }
}
