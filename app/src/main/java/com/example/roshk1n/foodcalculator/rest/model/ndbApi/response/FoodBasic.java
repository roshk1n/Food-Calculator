package com.example.roshk1n.foodcalculator.rest.model.ndbApi.response;

import java.util.ArrayList;

/**
 * Created by roshk1n on 8/11/2016.
 */
public class FoodBasic {
    private String ndbno;
    private String name;

    private ArrayList<NutrientBasic> nutrients = new ArrayList<>();

    public String getNdbno() {
        return ndbno;
    }

    public void setNdbno(String ndbno) {
        this.ndbno = ndbno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<NutrientBasic> getNutrients() {
        return nutrients;
    }

    public void setNutrients(ArrayList<NutrientBasic> nutrients) {
        this.nutrients = nutrients;
    }
}
