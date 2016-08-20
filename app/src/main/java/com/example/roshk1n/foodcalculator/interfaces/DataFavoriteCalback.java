package com.example.roshk1n.foodcalculator.interfaces;

import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;

import java.util.ArrayList;

/**
 * Created by roshk1n on 8/19/2016.
 */
public interface DataFavoriteCalback {

    void setFavoriteList(ArrayList<Food> favFoods);

}
