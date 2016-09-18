package com.example.roshk1n.foodcalculator.interfaces.responseAdapter;

import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;

public interface CallbackFavoriteAdapter {
    void navigateToInfoFood(Food food);

    void navigateToAddFood(Food food);
}
