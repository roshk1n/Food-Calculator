package com.example.roshk1n.foodcalculator.responseAdapter;

import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;

public interface CallbackFavoriteAdapter {
    void navigateToInfoFood(Food food);

    void navigateToAddFood(Food food);
}
