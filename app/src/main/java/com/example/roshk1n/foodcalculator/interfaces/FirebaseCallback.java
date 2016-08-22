package com.example.roshk1n.foodcalculator.interfaces;

import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.User;

import java.util.ArrayList;

/**
 * Created by roshk1n on 8/15/2016.
 */
public interface FirebaseCallback {
    void loginSuccessful();

    void showToast(String text);

    void setExistFavorite(boolean checkExistFood);
}
