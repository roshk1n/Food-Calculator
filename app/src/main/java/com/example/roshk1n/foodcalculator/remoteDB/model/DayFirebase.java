package com.example.roshk1n.foodcalculator.remoteDB.model;

import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Day;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;

import java.util.ArrayList;
import java.util.List;

public class DayFirebase {
    private Long date;
    private List<FoodFirebase> foods = new ArrayList<>();

    public DayFirebase() {}

    public DayFirebase(Day day) {
        setDate(day.getDate());
        for (Food food : day.getFoods()) {
            foods.add(new FoodFirebase(food));
        }
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public List<FoodFirebase> getFoods() {
        return foods;
    }

    public void setFoods(List<FoodFirebase> foods) {
        this.foods = foods;
    }
}
