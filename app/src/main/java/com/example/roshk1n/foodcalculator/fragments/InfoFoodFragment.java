package com.example.roshk1n.foodcalculator.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.Views.InfoFoodView;
import com.example.roshk1n.foodcalculator.adapters.RecyclerInfoAdapter;
import com.example.roshk1n.foodcalculator.presenters.InfoFoodPresenterImpl;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Nutrient;

import java.util.ArrayList;


public class InfoFoodFragment extends Fragment implements InfoFoodView {

    private final static String FOOD_KEY = "food";
    private InfoFoodPresenterImpl infoFoodPresenter;
    private Food food;
    private OnInfoFoodListener mInfoListener;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerInfoAdapter mAdapter;

    private View view;
    private TextView name_food_tv;
    private TextView calories_food_tv;
    private TextView cabs_food_tv;
    private TextView protein_food_tv;
    private TextView fat_food_tv;

    public InfoFoodFragment() {}

    public static InfoFoodFragment newInstance() {
        return new InfoFoodFragment();
    }

    public static InfoFoodFragment newInstance(Food food) {
        InfoFoodFragment infoFoodFragment = new InfoFoodFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(FOOD_KEY,food);
        infoFoodFragment.setArguments(bundle);
        return infoFoodFragment;
    }

    public interface OnInfoFoodListener {
        void setArrowToolbar();
        void disabledMenuSwipe();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        infoFoodPresenter = new InfoFoodPresenterImpl();
        infoFoodPresenter.setView(this);
        if(getArguments() != null) {
            food = getArguments().getParcelable(FOOD_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_info_food, container, false);

        initUI();

        if(mInfoListener != null) {
            mInfoListener.setArrowToolbar();
            mInfoListener.disabledMenuSwipe();
        }

        setNutrients(); //set filed from parcelable

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerInfoAdapter(food.getNutrients());
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnInfoFoodListener) {
            mInfoListener = (OnInfoFoodListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mInfoListener = null;
    }

    private void initUI() {
        name_food_tv = (TextView) view.findViewById(R.id.name_food_info_tv);
        calories_food_tv = (TextView) view.findViewById(R.id.calories_food_info_tv);
        cabs_food_tv = (TextView) view.findViewById(R.id.cabs_food_info_tv);
        protein_food_tv = (TextView) view.findViewById(R.id.protein_food_info_tv);
        fat_food_tv = (TextView) view.findViewById(R.id.fat_food_info_tv);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_info_food);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Info Food");
    }
    private void setNutrients() {
        name_food_tv.setText(food.getName());
        calories_food_tv.setText(food.getNutrients().get(1).getValue());
        protein_food_tv.setText(food.getNutrients().get(2).getValue());
        fat_food_tv.setText(food.getNutrients().get(3).getValue());
        cabs_food_tv.setText(food.getNutrients().get(4).getValue());
    }
}
