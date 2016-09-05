package com.example.roshk1n.foodcalculator.interfaces;

import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Day;

import java.util.ArrayList;

public interface LoadDaysCallback {
    void loadDaysComplete(ArrayList<Day> days);
}
