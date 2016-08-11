package com.example.roshk1n.foodcalculator.rest.model.ndbApi.response;

import java.util.ArrayList;

/**
 * Created by roshk1n on 7/19/2016.
 */
public class ListInfoFoodResponse {
    private ArrayList<NutrientSpecialFoodResponse>  foodResponses = new ArrayList<NutrientSpecialFoodResponse>();

    public ListInfoFoodResponse() {
    }

    public ArrayList<NutrientSpecialFoodResponse> getFoodResponses() {
        return foodResponses;
    }

    public void setFoodResponses(ArrayList<NutrientSpecialFoodResponse> foodResponses) {
        this.foodResponses = foodResponses;
    }
}
