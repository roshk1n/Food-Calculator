package com.example.roshk1n.foodcalculator.Views;

import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;

/**
 * Created by roshk1n on 8/2/2016.
 */
public interface FavoriteView {

    void makeSnackBar(int position, Food deleteFood);
}
