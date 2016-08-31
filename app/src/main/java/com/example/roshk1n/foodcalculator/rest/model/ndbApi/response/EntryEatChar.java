package com.example.roshk1n.foodcalculator.rest.model.ndbApi.response;

import android.annotation.TargetApi;
import android.os.Build;

public class EntryEatChar implements Comparable<EntryEatChar> {
    private int eatCalories;
    private int date;

    public EntryEatChar() {
    }

    public EntryEatChar(int eatCalories, int date) {
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
    public int compareTo(EntryEatChar another) {
        return Integer.compare(this.date, another.date);
    }
}
