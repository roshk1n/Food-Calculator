package com.example.roshk1n.foodcalculator.presenters;

import com.example.roshk1n.foodcalculator.manageres.DataManager;
import com.example.roshk1n.foodcalculator.interfaces.LoadDaysCallback;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Day;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.EntryEatChart;
import com.example.roshk1n.foodcalculator.views.ChartView;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class ChartPresenterImpl implements ChartPresenter {
    private ChartView chartView;
    private int amountCalories;
    private DataManager dataManager = new DataManager();

    @Override
    public void setView(ChartView view) {
        this.chartView = view;
    }

    @Override
    public void loadData(final int period) {
       dataManager.loadDataForChart(new LoadDaysCallback() {
            @Override
            public void loadDaysComplete(ArrayList<Day> days) {
                ArrayList<EntryEatChart> entryEatCharts;
                ArrayList<Entry> entriesChart = new ArrayList<>();
                amountCalories = 0;
                if (period == 0) {
                    entryEatCharts = getCurrentWeekEntry(days); // choose day for current week
                    for (int i = 0; i < entryEatCharts.size(); i++) { // convert to EntryChart
                        entriesChart.add(new Entry(entryEatCharts.get(i).getEatCalories(), entryEatCharts.get(i).getDate()));
                        amountCalories += entryEatCharts.get(i).getEatCalories();
                    }

                } else if (period == 1) {
                    entryEatCharts = getCurrentMonthEntry(days);
                    for (int i = 0; i < entryEatCharts.size(); i++) {
                        entriesChart.add(new Entry(entryEatCharts.get(i).getEatCalories(), entryEatCharts.get(i).getDate() - 1));
                        amountCalories += entryEatCharts.get(i).getEatCalories();
                    }

                } else if (period == 2) {
                    entryEatCharts = getCurrentYearEntry(days);
                    for (int i = 0; i < entryEatCharts.size(); i++) {
                        entriesChart.add(new Entry(entryEatCharts.get(i).getEatCalories(), entryEatCharts.get(i).getDate()));
                        amountCalories += entryEatCharts.get(i).getEatCalories();
                    }
                }
                chartView.setEntry(entriesChart, period);

            }
        });
    }

    @Override
    public ArrayList<String> formatLabelsMonth() {
        ArrayList<String> labels = new ArrayList<>();
        Calendar date = Calendar.getInstance();
        int month = date.get(Calendar.MONTH) + 1; // start with 0
        for (int i = 0; i < date.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            labels.add(i + 1 + "/" + month);
        }
        return labels;
    }

    @Override
    public int getLimitCalories() {
        return dataManager.loadGoalCalories();
    }

    @Override
    public void destroy() {
        chartView = null;
    }

    public int getAmountCalories() {
        return amountCalories;
    }

    private ArrayList<EntryEatChart> getCurrentYearEntry(ArrayList<Day> listDay) {
        ArrayList<EntryEatChart> entryEatCharts = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            entryEatCharts.add(new EntryEatChart(0, i));
        }

        for (Day day : listDay) {
            Calendar date = Calendar.getInstance();
            date.setTimeInMillis(day.getDate());
            int month = date.get(Calendar.MONTH);
            int eatCalories = entryEatCharts.get(month).getEatCalories();
            entryEatCharts.get(month).setEatCalories(eatCalories + day.getEatDailyCalories());
        }
        return entryEatCharts;
    }

    private ArrayList<EntryEatChart> getCurrentMonthEntry(ArrayList<Day> listDay) {
        ArrayList<EntryEatChart> entryEatCharts = new ArrayList<>();
        Calendar date = Calendar.getInstance();
        Calendar dateNow = Calendar.getInstance();
        int monthNow = dateNow.get(Calendar.MONTH);
        for (Day day : listDay) {
            date.setTimeInMillis(day.getDate());
            int month = date.get(Calendar.MONTH);
            if (month == monthNow) {
                EntryEatChart entryEatChart = new EntryEatChart();
                entryEatChart.setEatCalories(day.getEatDailyCalories());
                entryEatChart.setDate(date.get(Calendar.DAY_OF_MONTH));
                entryEatCharts.add(entryEatChart);
            }
        }

        for (int i = 1; i < date.getActualMaximum(Calendar.DAY_OF_MONTH); i++) { // fill empty day
            if (!contains(i, entryEatCharts)) {
                entryEatCharts.add(new EntryEatChart(0, i));
            }
        }

        Collections.sort(entryEatCharts);
        return entryEatCharts;
    }

    private ArrayList<EntryEatChart> getCurrentWeekEntry(ArrayList<Day> list) {
        ArrayList<EntryEatChart> entryEatCharts = new ArrayList<>();
        Calendar date = Calendar.getInstance();
        Calendar nowDate = Calendar.getInstance();
        int yearNow = nowDate.get(Calendar.YEAR);
        int weekOfYearNow = nowDate.get(Calendar.WEEK_OF_YEAR); // variables for check current week
        for (Day day : list) {
            date.setTimeInMillis(day.getDate());
            int weekOfYear = date.get(Calendar.WEEK_OF_YEAR);
            int year = date.get(Calendar.YEAR);
            if (year == yearNow && weekOfYear == weekOfYearNow) {
                EntryEatChart entryEatChart = new EntryEatChart();
                entryEatChart.setEatCalories(day.getEatDailyCalories());
                entryEatChart.setDate(date.get(Calendar.DAY_OF_WEEK) - 1);
                entryEatCharts.add(entryEatChart);
            }
        }
        for (int i = 0; i < 7; i++) { // fill empty day
            if (!contains(i, entryEatCharts)) {
                entryEatCharts.add(new EntryEatChart(0, i));
            }
        }
        Collections.sort(entryEatCharts);
        return entryEatCharts;
    }

    private boolean contains(int day, ArrayList<EntryEatChart> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getDate() == day)
                return true;
        }
        return false;
    }
}

