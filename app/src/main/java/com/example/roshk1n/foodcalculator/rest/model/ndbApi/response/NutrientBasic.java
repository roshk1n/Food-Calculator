package com.example.roshk1n.foodcalculator.rest.model.ndbApi.response;

/**
 * Created by roshk1n on 8/11/2016.
 */
public class NutrientBasic {
    private String nutrient_id;
    private String name;
    private String unit;
    private String value;

    public String getNutrient_id() {
        return nutrient_id;
    }

    public void setNutrient_id(String nutrient_id) {
        this.nutrient_id = nutrient_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
