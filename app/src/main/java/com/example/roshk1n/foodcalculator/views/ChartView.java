package com.example.roshk1n.foodcalculator.views;

import android.content.Context;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

public interface ChartView {
    Context getContext();

    void setEntry(ArrayList<Entry> entriesChart, int period);
}
