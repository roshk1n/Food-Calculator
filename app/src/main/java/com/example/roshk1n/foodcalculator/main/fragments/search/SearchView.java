package com.example.roshk1n.foodcalculator.main.fragments.search;

import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.NutrientFoodResponse;

public interface SearchView {
    void updateUI(NutrientFoodResponse nutrientFoodResponses);
}
