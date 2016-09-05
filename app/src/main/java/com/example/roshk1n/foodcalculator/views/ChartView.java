package com.example.roshk1n.foodcalculator.views;

import android.content.Context;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

/**
 * Created by roshk1n on 8/31/2016.
 */
public interface ChartView {
    Context getContext();

    void setEntry(ArrayList<Entry> entriesChart, int period);
}
