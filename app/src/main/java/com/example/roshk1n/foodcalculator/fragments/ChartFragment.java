package com.example.roshk1n.foodcalculator.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    private ImageView calendarIv;
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
            mFragmentListener.setTitle(getString(R.string.chart));
        }

        userLimit = presenter.getLimitCalories();
        periodSp.setItems(getString(R.string.week), getString(R.string.month), getString(R.string.year));

        presenter.loadData(0);
        amountCalTv.setText(String.valueOf(presenter.getAmountCalories())+getContext().getString(R.string.cal));

        periodSp.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                presenter.loadData(position);
            }
        });

        calendarIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                periodSp.expand();
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

    @Override
    public void setEntry(ArrayList<Entry> entriesChart, int period) {
        ArrayList<Entry> entries = entriesChart;
        amountCalTv.setText(String.valueOf(presenter.getAmountCalories())+getContext().getString(R.string.cal));
        dataSet = new LineDataSet(entries, "");
        if (period == 0) {
            ArrayList<String> labels = new ArrayList<>(); // list for week labels
            labels.add(getString(R.string.sunday));
            labels.add(getString(R.string.monday));
            labels.add(getString(R.string.tuesday));
            labels.add(getString(R.string.wednesday));
            labels.add(getString(R.string.thursday));
            labels.add(getString(R.string.friday));
            labels.add(getString(R.string.saturday));
            data = new LineData(labels, dataSet);
            dayTv.setText(getString(R.string.days));
            configureChart(true, 0, 0, 0, 0);

        } else if (period == 1) {
            ArrayList<String> labelsMonth = presenter.formatLabelsMonth();
            data = new LineData(labelsMonth, dataSet);
            dayTv.setText(getString(R.string.days));
            configureChart(true, 5, 0, 0, 0);

        } else if (period == 2) {
            dayTv.setText(R.string.months);
            ArrayList<String> labelsMonth = new ArrayList<>(); // list for week labels
            labelsMonth.add(getString(R.string.jan));
            labelsMonth.add(getString(R.string.feb));
            labelsMonth.add(getString(R.string.mar));
            labelsMonth.add(getString(R.string.apr));
            labelsMonth.add(getString(R.string.may));
            labelsMonth.add(getString(R.string.jun));
            labelsMonth.add(getString(R.string.jul));
            labelsMonth.add(getString(R.string.aug));
            labelsMonth.add(getString(R.string.sept));
            labelsMonth.add(getString(R.string.Oct));
            labelsMonth.add(getString(R.string.Nov));
            labelsMonth.add(getString(R.string.Dec));
            data = new LineData(labelsMonth, dataSet);
            configureChart(false, 0, 0, 0, 0);
        }
    }

    private void initUI() {
        lineChart = (LineChart) view.findViewById(R.id.eat_chart);
        periodSp = (MaterialSpinner) view.findViewById(R.id.period_sp);
        dayTv = (TextView) view.findViewById(R.id.day_chart_tv);
        amountCalTv = (TextView) view.findViewById(R.id.amount_calories_chart_tv);
        calendarIv = (ImageView) view.findViewById(R.id.calendar_iv);
    }

    private void configureChart(boolean enableLimitLine, float scaleX, float scaleY, float x, float y) {
        dataSet.setDrawFilled(true);
        dataSet.setColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));
        dataSet.setCircleColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));
        dataSet.setCircleColorHole(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));
        dataSet.setFillColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));
        dataSet.setValueTextSize(10);

        lineChart.setDescription("");
        lineChart.setGridBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorWhite));
        lineChart.getLegend().setEnabled(false);
        lineChart.animateY(1000);
        lineChart.setData(data);
        lineChart.zoom(scaleX, scaleY, x, y);

        LimitLine ll1 = new LimitLine(userLimit, getString(R.string.limit)+" ("+userLimit+" "+getContext().getString(R.string.cal)+")");
        ll1.setLineWidth(1.7f);
        ll1.enableDashedLine(20f, 20f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(11f);
        ll1.setLineColor(ContextCompat.getColor(getContext(),R.color.colorError));
        ll1.setTextColor(ContextCompat.getColor(getContext(),R.color.colorError));

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
}

