package com.example.roshk1n.foodcalculator.presenters;

import com.example.roshk1n.foodcalculator.DataManager;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Day;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.EntryEatChar;
import com.example.roshk1n.foodcalculator.views.ChartView;

import java.util.ArrayList;
import java.util.Calendar;

public class ChartPresenterImpl implements ChartPresenter {
    private ChartView chartView;
    private DataManager dataManager = new DataManager();

    @Override
    public void setView(ChartView view) {
        this.chartView = view;
    }

    @Override
    public ArrayList<EntryEatChar> loadData() {
        ArrayList<Day> listDay = dataManager.loadDataForChart();





        ArrayList<EntryEatChar> entryEatChars = new ArrayList<>();
        Calendar date = Calendar.getInstance();
        for (Day day : listDay) {
            date.setTimeInMillis(day.getDate());
            EntryEatChar entryEatChar = new EntryEatChar();
            entryEatChar.setEatCalories(day.getEatDailyCalories());
            entryEatChar.setDate(date.get(Calendar.DAY_OF_WEEK)-1);
            entryEatChars.add(entryEatChar);
        }
        return entryEatChars;
    }
}

