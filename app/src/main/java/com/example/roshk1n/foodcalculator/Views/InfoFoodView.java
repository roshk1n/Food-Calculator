package com.example.roshk1n.foodcalculator.Views;

import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.NutrientBasic;

import java.util.ArrayList;

/**
 * Created by roshk1n on 8/11/2016.
 */
public interface InfoFoodView {

    void addNutrients(ArrayList<NutrientBasic> nutrients);
}
