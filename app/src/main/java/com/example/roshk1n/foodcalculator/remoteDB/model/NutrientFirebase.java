package com.example.roshk1n.foodcalculator.remoteDB.model;

import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Nutrient;


public class NutrientFirebase {

    private String nutrient_id;
    private String name;
    private String unit;
    private String value;

    public NutrientFirebase() {
    }

    public NutrientFirebase(Nutrient name) {
        setNutrient_id(name.getNutrient_id());
        setName(name.getName());
        setUnit(name.getUnit());
        setValue(name.getValue());
    }

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
