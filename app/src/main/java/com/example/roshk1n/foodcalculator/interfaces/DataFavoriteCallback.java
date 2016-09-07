package com.example.roshk1n.foodcalculator.interfaces;

import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;

import java.util.ArrayList;

public interface DataFavoriteCallback {

    void setFavoriteList(ArrayList<Food> favFoods);

}
