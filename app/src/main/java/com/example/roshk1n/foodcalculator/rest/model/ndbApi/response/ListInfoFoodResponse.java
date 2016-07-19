package com.example.roshk1n.foodcalculator.rest.model.ndbApi.response;

import java.util.ArrayList;

/**
 * Created by roshk1n on 7/19/2016.
 */
public class ListInfoFoodResponse {
    private ArrayList<InfoFoodResponse>  foodResponses = new ArrayList<InfoFoodResponse>();

    public ListInfoFoodResponse() {
    }

    public ArrayList<InfoFoodResponse> getFoodResponses() {
        return foodResponses;
    }

    public void setFoodResponses(ArrayList<InfoFoodResponse> foodResponses) {
        this.foodResponses = foodResponses;
    }
}
