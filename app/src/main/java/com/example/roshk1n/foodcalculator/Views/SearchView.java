package com.example.roshk1n.foodcalculator.Views;

import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.NutrientSpecialFoodResponse;

public interface SearchView {
    void setFoodNutrients(NutrientSpecialFoodResponse nutrientSpecialFoodResponses);

    void setErrorNetwork();
}
