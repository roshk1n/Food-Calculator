package com.example.roshk1n.foodcalculator.model;

import java.util.ArrayList;

/**
 * Created by roshk1n on 7/18/2016.
 */
public class Meal {

    private ArrayList<Food> foods;

    public Meal() {}


    public ArrayList<Food> getFoods() {
        return foods;
    }

    public void setFoods(ArrayList<Food> foods) {
        this.foods = foods;
    }
}
