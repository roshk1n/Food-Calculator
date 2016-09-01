package com.example.roshk1n.foodcalculator.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.presenters.ChartPresenterImpl;
import com.example.roshk1n.foodcalculator.views.ChartView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
//import com.github.mikephil.charting.utils.EntryXComparator;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;

public class ChartFragment extends Fragment implements ChartView {
    private ChartPresenterImpl presenter;
    private View view;
    private MaterialSpinner peridoSp;
    private TextView caloriesTv;
    private LineChart lineChart;
    private LineDataSet dataSet;
    private LineData data;
    private  ArrayList<Entry> entries;

    public static ChartFragment newInstance() {
        return new ChartFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ChartPresenterImpl();
        presenter.setView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chart, container, false);

        lineChart = (LineChart) view.findViewById(R.id.eat_chart);
        peridoSp = (MaterialSpinner) view.findViewById(R.id.period_sp);
        caloriesTv = (TextView) view.findViewById(R.id.calories_chart_tv);

        peridoSp.setItems("Week", "Month", "Year");

        entries = presenter.loadData(0);

        final ArrayList<String> labels = new ArrayList<>(); // list for week labels
        labels.add("Sun");
        labels.add("Mon");
        labels.add("Tue");
        labels.add("Wed");
        labels.add("Thu");
        labels.add("Fri");
        labels.add("Sat");
        // configuration chart
        dataSet = new LineDataSet(entries, "");
        data = new LineData(labels, dataSet);
        configureChart();
        lineChart.invalidate();

        peridoSp.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                entries = presenter.loadData(position);
                dataSet = new LineDataSet(entries, "");
                if (position == 0) {
                    data = new LineData(labels, dataSet);
                    configureChart();
                    lineChart.invalidate();

                } else if (position == 1) {
                    ArrayList<String> labelsMonth = presenter.formatLabelsMonth();
                    data = new LineData(labelsMonth, dataSet);
                    configureChart();
                    lineChart.zoom(2,0,0,0);
                    lineChart.invalidate();

                } else if (position == 2) {
                    ArrayList<String> labelsMonth = new ArrayList<>(); // list for week labels
                    labelsMonth.add("Jan");
                    labelsMonth.add("Feb");
                    labelsMonth.add("Mar");
                    labelsMonth.add("Apr");
                    labelsMonth.add("May");
                    labelsMonth.add("Jun");
                    labelsMonth.add("Jul");
                    labelsMonth.add("Aug");
                    labelsMonth.add("Sept");
                    labelsMonth.add("Oct");
                    labelsMonth.add("Nov");
                    labelsMonth.add("Dec");
                    data = new LineData(labelsMonth, dataSet);
                    configureChart();
                    lineChart.invalidate();
                }
            }
        });

        return view;
    }
    private void configureChart(){
        dataSet.setDrawCubic(true);
        dataSet.setDrawFilled(true);
        dataSet.setColor(getResources().getColor(R.color.colorPrimaryDark));
        dataSet.setCircleColor(getResources().getColor(R.color.colorPrimaryDark));
        dataSet.setCircleColorHole(getResources().getColor(R.color.colorPrimaryDark));
        dataSet.setFillColor(getResources().getColor(R.color.colorPrimaryDark));
        dataSet.setDrawFilled(true);
        dataSet.setValueTextSize(10);

        lineChart.setDescription("");
        lineChart.setGridBackgroundColor(getResources().getColor(R.color.mdtp_white));
        lineChart.getLegend().setEnabled(false);
        lineChart.animateY(1000);
        lineChart.setData(data);

        LimitLine ll1 = new LimitLine(500, "Limit");
        ll1.setLineWidth(2f);
        ll1.enableDashedLine(20f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(11f);
        ll1.setTextColor(getResources().getColor(R.color.colorPrimary));

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(ll1);
        leftAxis.setTextSize(11f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setStartAtZero(true);
        leftAxis.setXOffset(10f);

        YAxis yAxis = lineChart.getAxisRight();
        yAxis.setEnabled(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setDrawLabels(true);
        xAxis.setTextSize(11.5f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setYOffset(10f);
    }
}

