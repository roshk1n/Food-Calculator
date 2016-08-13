package com.example.roshk1n.foodcalculator.Views;

import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.FoodResponse;

public interface SearchView {
    void setFoodNutrients(FoodResponse foodResponse);

    void setErrorNetwork();
}
