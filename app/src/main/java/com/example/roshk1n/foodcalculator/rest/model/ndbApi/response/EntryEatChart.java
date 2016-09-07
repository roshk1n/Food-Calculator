package com.example.roshk1n.foodcalculator.rest.model.ndbApi.response;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;

public class EntryEatChart implements Comparable<EntryEatChart> {
    private int eatCalories;
    private int date;

    public EntryEatChart() {
    }

    public EntryEatChart(int eatCalories, int date) {
        this.eatCalories = eatCalories;
        this.date = date;
    }

    public int getEatCalories() {
        return eatCalories;
    }

    public void setEatCalories(int eatCalories) {
        this.eatCalories = eatCalories;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public int compareTo(@NonNull EntryEatChart another) {
        return Integer.compare(this.date, another.date);
    }
}
