package com.example.roshk1n.foodcalculator;

import android.graphics.Bitmap;
import android.net.NetworkInfo;
import android.util.Base64;

import com.example.roshk1n.foodcalculator.interfaces.CreateUserFirebaseCallback;
import com.example.roshk1n.foodcalculator.interfaces.DataAddFoodCallback;
import com.example.roshk1n.foodcalculator.interfaces.DataDiaryCallback;
import com.example.roshk1n.foodcalculator.interfaces.DataFavoriteCalback;
import com.example.roshk1n.foodcalculator.interfaces.StateItemCallback;
import com.example.roshk1n.foodcalculator.interfaces.DataLoginCallback;
import com.example.roshk1n.foodcalculator.interfaces.DataSingUpCallback;
import com.example.roshk1n.foodcalculator.interfaces.FirebaseCallback;
import com.example.roshk1n.foodcalculator.interfaces.LoadDayCallback;
import com.example.roshk1n.foodcalculator.interfaces.LocalManagerCallback;
import com.example.roshk1n.foodcalculator.interfaces.ResponseListentenerUpload;
import com.example.roshk1n.foodcalculator.remoteDB.FirebaseHelper;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Day;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;
import com.example.roshk1n.foodcalculator.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;

public class DataManager implements FirebaseCallback, LocalManagerCallback {
    private FirebaseHelper firebaseHelper = FirebaseHelper.getInstance(this);
    private DataLoginCallback dataLoginCallback;
    private DataSingUpCallback dataSingUpCallback;
    private DataDiaryCallback dataDiaryCallback;
    private DataAddFoodCallback dataAddFoodCallback;
    private DataFavoriteCalback dataFavoriteCallback;

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

    public DataManager(DataFavoriteCalback dataFavoriteCallback) {
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
        dataLoginCallback.showToast("Authentication failed. Try again please!");
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
        firebaseHelper.loadDay(date, new LoadDayCallback() {
            @Override
            public void loadComplete(Day dayFire) {
                callback.loadComplete(dayFire);
                LocalDataBaseManager.updateDays(dayFire);
            }
        });
    }

    public void removeFood(int indexRemove) {
        LocalDataBaseManager.removeFood(indexRemove);
        firebaseHelper.removeFood(indexRemove);
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
        LocalDataBaseManager.updateCalories(eat_calories,remainingCalories);
        firebaseHelper.updateCalories(eat_calories,remainingCalories,date);
    }

    public void addFavoriteFood(Food food, StateItemCallback callback) {
       //callback.updateImageFavorite(LocalDataBaseManager.addFavoriteFood(food););
        firebaseHelper.addFavoriteFood(food,callback);
    }

    public void isExistInFavorite(Food food, DataAddFoodCallback callback) {
      //  dataAddFoodCallback.setExistFavorite(LocalDataBaseManager.isExistInFavotite(food)); //null intargace for firebase
        firebaseHelper.isExistInFavorite(food, callback);
    }

    public void removeFavoriteFoodDB(String ndbno, StateItemCallback callback) {
      // callback.updateImageFavorite(LocalDataBaseManager.removeFavoriteFoodDB(ndbno));
        firebaseHelper.removeFavoriteFood(ndbno, callback);
    }

    public void removeFavoriteFoodDB(int position) {
       // LocalDataBaseManager.removeFavoriteFoodDB(position);
        firebaseHelper.removeFavoriteFood(position);
    }

    public void loadFavoriteList(final DataFavoriteCalback callback) {
        //callback.setFavoriteList(LocalDataBaseManager.loadFavoriteFood());
        firebaseHelper.loadFavoriteFood(new DataFavoriteCalback() {
            @Override
            public void setFavoriteList(ArrayList<Food> favFoods) {
                callback.setFavoriteList(favFoods);
            }
        });
    }
}
