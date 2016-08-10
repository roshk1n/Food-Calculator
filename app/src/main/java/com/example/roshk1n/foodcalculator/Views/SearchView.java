package com.example.roshk1n.foodcalculator.Views;

import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.NutrientFoodResponse;

public interface SearchView {
    void updateUI(NutrientFoodResponse nutrientFoodResponses);
}
