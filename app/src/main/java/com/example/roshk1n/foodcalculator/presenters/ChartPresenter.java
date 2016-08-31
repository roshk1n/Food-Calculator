package com.example.roshk1n.foodcalculator.presenters;

import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.EntryEatChar;
import com.example.roshk1n.foodcalculator.views.ChartView;

import java.util.ArrayList;

/**
 * Created by roshk1n on 8/31/2016.
 */
public interface ChartPresenter {
    void setView(ChartView view);

    ArrayList<EntryEatChar> loadData();
}
