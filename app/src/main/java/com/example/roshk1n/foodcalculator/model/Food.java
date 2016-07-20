package com.example.roshk1n.foodcalculator.model;


/**
 * Created by roshk1n on 7/12/2016.
 */
public class Food  {
    private String name;
    private String group;
    private String description;
    private int energy;
    private float protein;
    private float fat;
    private float sugar;
    private float carbohydrate;
    private float valuePer = 100;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Food(String name, int energy, float valuePer) {
        this.name = name;
        this.energy = energy;
        this.valuePer = valuePer;
    }

    public Food(String name, String group, String description, int energy, float protein, float fat, float sugar, float carbohydrate, float valuePer) {

        this.name = name;
        this.group = group;
        this.description = description;
        this.energy = energy;
        this.protein = protein;
        this.fat = fat;
        this.sugar = sugar;
        this.carbohydrate = carbohydrate;
        this.valuePer = valuePer;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public float getProtein() {
        return protein;
    }

    public void setProtein(float protein) {
        this.protein = protein;
    }

    public float getFat() {
        return fat;
    }

    public void setFat(float fat) {
        this.fat = fat;
    }

    public float getSugar() {
        return sugar;
    }

    public void setSugar(float sugar) {
        this.sugar = sugar;
    }

    public float getCarbohydrate() {
        return carbohydrate;
    }

    public void setCarbohydrate(float carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public float getValuePer() {
        return valuePer;
    }

    public void setValuePer(float valuePer) {
        this.valuePer = valuePer;
    }

}