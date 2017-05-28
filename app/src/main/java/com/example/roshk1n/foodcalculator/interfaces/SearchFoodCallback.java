package com.example.roshk1n.foodcalculator.interfaces;

import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;

public interface SearchFoodCallback {
    void setFood(Food foodResult);

    void error(String message);
}
