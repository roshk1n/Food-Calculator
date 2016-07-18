package com.example.roshk1n.foodcalculator.rest.model.ndbApi.response;

/**
 * Created by roshk1n on 7/18/2016.
 */
public class Report {
    private String sr;
    private String type;
    private Food food;

    public Report() {}

    public String getSr() {
        return sr;
    }

    public void setSr(String sr) {
        this.sr = sr;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }
}
