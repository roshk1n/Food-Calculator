package com.example.roshk1n.foodcalculator.presenters;

import com.example.roshk1n.foodcalculator.views.ChartView;
import java.util.ArrayList;

public interface ChartPresenter {
    void setView(ChartView view);

    void loadData(int period);

    ArrayList<String> formatLabelsMonth();

    int getLimitCalories();
}
