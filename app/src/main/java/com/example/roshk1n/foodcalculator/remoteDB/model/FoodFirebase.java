package com.example.roshk1n.foodcalculator.remoteDB.model;

import java.util.ArrayList;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Nutrient;

import java.util.List;

public class FoodFirebase {
    private String ndbno;
    private String name;
    private String nameEng;
    private Long time;
    private Long portion = 1L;
    private List<NutrientFirebase> nutrients = new ArrayList<>();

    public FoodFirebase() {}

    public FoodFirebase(Food food) {
        setNdbno(food.getNdbno());
        setName(food.getName());
        setNameEng(food.getNameEng());
        setTime(food.getTime());
        setPortion((long)food.getPortion());
        for (Nutrient nutrient : food.getNutrients()) {
            nutrients.add(new NutrientFirebase(nutrient));
        }
    }

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

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getPortion() {
        return portion;
    }

    public void setPortion(Long portion) {
        this.portion = portion;
    }

    public List<NutrientFirebase> getNutrients() {
        return nutrients;
    }

    public void setNutrients(List<NutrientFirebase> nutrients) {
        this.nutrients = nutrients;
    }

    public String getNameEng() {
        return nameEng;
    }

    public void setNameEng(String nameEng) {
        this.nameEng = nameEng;
    }
}
