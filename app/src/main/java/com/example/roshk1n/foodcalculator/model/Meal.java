package com.example.roshk1n.foodcalculator.model;

import com.example.roshk1n.foodcalculator.enums.MealOfDay;
import com.example.roshk1n.foodcalculator.model.Food;

import java.util.ArrayList;

/**
 * Created by roshk1n on 7/18/2016.
 */
public class Meal {
    private MealOfDay mealOfDay;
    private ArrayList<Food> foods;

    public Meal() {}

    public MealOfDay getMealOfDay() {
        return mealOfDay;
    }

    public void setMealOfDay(MealOfDay mealOfDay) {
        this.mealOfDay = mealOfDay;
    }

    public Meal(MealOfDay mealOfDay, ArrayList<Food> foods) {

        this.mealOfDay = mealOfDay;
        this.foods = foods;
    }

    public ArrayList<Food> getFoods() {
        return foods;
    }

    public void setFoods(ArrayList<Food> foods) {
        this.foods = foods;
    }
}
