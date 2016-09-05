package com.example.roshk1n.foodcalculator.presenters;

import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.EntryEatChart;
import com.example.roshk1n.foodcalculator.views.ChartView;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

/**
 * Created by roshk1n on 8/31/2016.
 */
public interface ChartPresenter {
    void setView(ChartView view);

    void loadData(int period);

    ArrayList<String> formatLabelsMonth();

    int getLimitCalories();
}
