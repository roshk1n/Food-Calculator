package com.example.roshk1n.foodcalculator.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.interfaces.OnFragmentListener;
import com.example.roshk1n.foodcalculator.presenters.ChartPresenterImpl;
import com.example.roshk1n.foodcalculator.views.ChartView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;

public class ChartFragment extends Fragment implements ChartView {
    private ChartPresenterImpl presenter;
    private LineDataSet dataSet;
    private LineData data;
    private int userLimit;

    private OnFragmentListener mFragmentListener;

    private View view;
    private MaterialSpinner periodSp;
    private TextView dayTv;
    private TextView amountCalTv;
    private LineChart lineChart;

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

        initUI();

        if (mFragmentListener != null) {
            mFragmentListener.setTitle("Chart");
        }

        userLimit = presenter.getLimitCalories();
        periodSp.setItems("Week", "Month", "Year");


        presenter.loadData(0);
        amountCalTv.setText(String.valueOf(presenter.getAmountCalories()));


        periodSp.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                presenter.loadData(position);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentListener) {
            mFragmentListener = (OnFragmentListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentListener = null;
    }

    private void initUI() {
        lineChart = (LineChart) view.findViewById(R.id.eat_chart);
        periodSp = (MaterialSpinner) view.findViewById(R.id.period_sp);
        dayTv = (TextView) view.findViewById(R.id.day_chart_tv);
        amountCalTv = (TextView) view.findViewById(R.id.amount_calories_chart_tv);
    }

    private void configureChart(boolean enableLimitLine, float scaleX, float scaleY, float x, float y) {
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
        lineChart.zoom(scaleX, scaleY, x, y);

        LimitLine ll1 = new LimitLine(userLimit, "Limit");
        ll1.setLineWidth(2f);
        ll1.enableDashedLine(20f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(11f);
        ll1.setTextColor(getResources().getColor(R.color.colorPrimary));

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        if (enableLimitLine) {
            leftAxis.addLimitLine(ll1);
        }

        leftAxis.setTextSize(11f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setStartAtZero(true);
        leftAxis.setXOffset(10f);
        leftAxis.setDrawTopYLabelEntry(true);

        YAxis yAxis = lineChart.getAxisRight();
        yAxis.setEnabled(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setDrawLabels(true);
        xAxis.setTextSize(11.5f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setYOffset(10f);
        lineChart.invalidate();
    }

    @Override
    public void setEntry(ArrayList<Entry> entriesChart, int period) {
        ArrayList<Entry> entries = entriesChart;
        amountCalTv.setText(String.valueOf(presenter.getAmountCalories()));
        dataSet = new LineDataSet(entries, "");
        if (period == 0) {
            ArrayList<String> labels = new ArrayList<>(); // list for week labels
            labels.add("Sun");
            labels.add("Mon");
            labels.add("Tue");
            labels.add("Wed");
            labels.add("Thu");
            labels.add("Fri");
            labels.add("Sat");
            data = new LineData(labels, dataSet);
            dayTv.setText("Days");
            configureChart(true, 0, 0, 0, 0);

        } else if (period == 1) {
            ArrayList<String> labelsMonth = presenter.formatLabelsMonth();
            data = new LineData(labelsMonth, dataSet);
            dayTv.setText("Days");
            configureChart(true, 5, 0, 0, 0);

        } else if (period == 2) {
            dayTv.setText("Months");
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
            configureChart(false, 0, 0, 0, 0);
        }
    }
}

