package com.example.roshk1n.foodcalculator;

import android.content.Context;

import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Day;

/**
 * Created by roshk1n on 8/16/2016.
 */
public interface CallbackData {
    void showToast(String s);

    void navigateToHome();

    Context getContext();
}
