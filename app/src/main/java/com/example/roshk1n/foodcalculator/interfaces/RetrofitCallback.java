package com.example.roshk1n.foodcalculator.interfaces;

import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.FoodResponse;

/**
 * Created by roshk1n on 8/12/2016.
 */
public interface RetrofitCallback {

    void addFood(FoodResponse nutrientBasic);

    void errorNetwork();
}
