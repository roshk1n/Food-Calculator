package com.example.roshk1n.foodcalculator.rest.model.ndbApi.response;

import java.util.ArrayList;

/**
 * Created by roshk1n on 7/19/2016.
 */
public class ListInfoFoodResponse {
    private ArrayList<NutrientFoodResponse>  foodResponses = new ArrayList<NutrientFoodResponse>();

    public ListInfoFoodResponse() {
    }

    public ArrayList<NutrientFoodResponse> getFoodResponses() {
        return foodResponses;
    }

    public void setFoodResponses(ArrayList<NutrientFoodResponse> foodResponses) {
        this.foodResponses = foodResponses;
    }
}
