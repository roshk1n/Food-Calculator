package com.example.roshk1n.foodcalculator.realm;

import io.realm.RealmObject;

public class NutrientRealm extends RealmObject {

    private String nutrient_id;
    private String nutrient;
    private String unit;
    private String value;

    public NutrientRealm() {
    }

    public String getNutrient_id() {
        return nutrient_id;
    }

    public void setNutrient_id(String nutrient_id) {
        this.nutrient_id = nutrient_id;
    }

    public String getNutrient() {
        return nutrient;
    }

    public void setNutrient(String nutrient) {
        this.nutrient = nutrient;
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
