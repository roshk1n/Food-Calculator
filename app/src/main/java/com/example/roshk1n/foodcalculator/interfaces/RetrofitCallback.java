package com.example.roshk1n.foodcalculator.interfaces;

import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.FoodResponse;

public interface RetrofitCallback {

    void addFood(FoodResponse nutrientBasic);

    void error(String message);
}
