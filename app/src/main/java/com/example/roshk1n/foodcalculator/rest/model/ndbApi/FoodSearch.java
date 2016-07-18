package com.example.roshk1n.foodcalculator.rest.model.ndbApi;

/**
 * Created by roshk1n on 7/18/2016.
 */
public class FoodSearch {
    private String api_key;
    private String q ;
    private String format;

    public FoodSearch(String api_key, String q, String format) {
        this.api_key = api_key;
        this.q = q;
        this.format = format;
    }
}
