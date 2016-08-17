package com.example.roshk1n.foodcalculator.interfaces;

import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Day;

/**
 * Created by roshk1n on 8/16/2016.
 */
public interface LoadDayCallback {
    void loadComplete(Day day);
}
