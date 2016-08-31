package com.example.roshk1n.foodcalculator.fragments;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.presenters.ChartPresenterImpl;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Day;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.EntryEatChar;
import com.example.roshk1n.foodcalculator.views.ChartView;
import com.facebook.stetho.common.ArrayListAccumulator;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
//import com.github.mikephil.charting.utils.EntryXComparator;
import com.github.mikephil.charting.utils.FileUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

public class ChartFragment extends Fragment implements ChartView {
    private ChartPresenterImpl presenter;
    private View view;

    public static ChartFragment newInstance() {
        return new ChartFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ChartPresenterImpl();
        presenter.setView(this);
    }

    private boolean contains(int day, ArrayList<EntryEatChar> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getDate() == day)
                return true;
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chart, container, false);

        LineChart lineChart = (LineChart) view.findViewById(R.id.eat_chart);

        ArrayList<EntryEatChar> entries = presenter.loadData();

        for (int i = 0; i < 7; i++) {
            if (!contains(i, entries)) {
                entries.add(new EntryEatChar(0, i));
            }
        }
        Collections.sort(entries);
        ArrayList<Entry> entriesChart = new ArrayList<>();
        for (int i = 0; i < entries.size(); i++)
            entriesChart.add(new Entry(entries.get(i).getEatCalories(), entries.get(i).getDate()));

        //   Collections.sort(entriesChart, new EntryXComparator());

        LineDataSet dataSet = new LineDataSet(entriesChart, "Calories");

        //dataSet.setDrawCubic(true);
        dataSet.setDrawFilled(true);
        dataSet.setColor(Color.RED);
        dataSet.setCircleColor(getResources().getColor(R.color.colorPrimaryDark));
        dataSet.setCircleColorHole(getResources().getColor(R.color.colorPrimaryDark));
        lineChart.setDescription("");

        ArrayList<String> labels = new ArrayList<>();
        labels.add("Sun");
        labels.add("Mon");
        labels.add("Tue");
        labels.add("Wed");
        labels.add("Thu");
        labels.add("Fri");
        labels.add("Sat");


        LineData data = new LineData(labels, dataSet);
        lineChart.setData(data);
        dataSet.setDrawFilled(true);

        LimitLine ll1 = new LimitLine(1800f, "Limit");
        ll1.setLineWidth(3f);
        ll1.enableDashedLine(15f, 20f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(12f);
        ll1.setTextColor(getResources().getColor(R.color.colorPrimary));

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(ll1);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        //leftAxis.setDrawZeroLine(true);
        leftAxis.setXOffset(10f);


        YAxis yAxis = lineChart.getAxisRight();
        yAxis.setEnabled(false);

        XAxis xAxis = lineChart.getXAxis();
        //  xAxis.setGranularity(1f);
        xAxis.setDrawLabels(true);
//        xAxis.setLabelRotationAngle(60);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setXOffset(20f);
        lineChart.animateY(1000);

        return view;
    }
}
