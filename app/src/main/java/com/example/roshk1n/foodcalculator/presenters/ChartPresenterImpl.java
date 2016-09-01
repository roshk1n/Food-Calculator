package com.example.roshk1n.foodcalculator.presenters;

import android.util.Log;

import com.example.roshk1n.foodcalculator.DataManager;
import com.example.roshk1n.foodcalculator.LocalDataBaseManager;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Day;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.EntryEatChart;
import com.example.roshk1n.foodcalculator.views.ChartView;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class ChartPresenterImpl implements ChartPresenter {
    private ChartView chartView;
    private DataManager dataManager = new DataManager();

    @Override
    public void setView(ChartView view) {
        this.chartView = view;
    }

    @Override
    public ArrayList<Entry> loadData(int period) {
        ArrayList<Day> listDay = dataManager.loadDataForChart(); //load day user
        ArrayList<EntryEatChart> entryEatCharts;
        ArrayList<Entry> entriesChart = new ArrayList<>();
        if (period == 0) {
            entryEatCharts = getCurrentWeekEntry(listDay); // choose day for current week
            for (int i = 0; i < entryEatCharts.size(); i++) // convert to EntryChart
                entriesChart.add(new Entry(entryEatCharts.get(i).getEatCalories(), entryEatCharts.get(i).getDate()));

        } else if (period == 1) {
            entryEatCharts = getCurrentMonthEntry(listDay);
            for (int i = 0; i < entryEatCharts.size(); i++)
                entriesChart.add(new Entry(entryEatCharts.get(i).getEatCalories(), entryEatCharts.get(i).getDate()-1));
        }
        return entriesChart;
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

    private ArrayList<EntryEatChart> getCurrentMonthEntry(ArrayList<Day> listDay) {
        ArrayList<EntryEatChart> entryEatCharts = new ArrayList<>();
        Calendar date = Calendar.getInstance();
        Calendar dateNow = Calendar.getInstance();
        int monthNow = dateNow.get(Calendar.MONTH);
        Log.d("Calendar", date.getActualMaximum(Calendar.DAY_OF_MONTH) + "");
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
                entryEatChart.setDate(date.get(Calendar.DAY_OF_WEEK)-1);
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

