package com.example.roshk1n.foodcalculator.views;

import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;

public interface SearchView {
    void setFoodNutrients(Food foods);

    void setErrorNetwork();
}
