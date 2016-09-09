package com.example.roshk1n.foodcalculator.views;

import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;

import java.util.ArrayList;

public interface FavoriteView {
    void setFavoriteList(ArrayList<Food> favFoods);
}
