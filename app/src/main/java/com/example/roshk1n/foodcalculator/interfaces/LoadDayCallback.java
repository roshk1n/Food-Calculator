package com.example.roshk1n.foodcalculator.interfaces;

import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Day;

public interface LoadDayCallback {
    void loadComplete(Day day);
}
