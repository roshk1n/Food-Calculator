package com.example.roshk1n.foodcalculator.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.Views.InfoFoodView;
import com.example.roshk1n.foodcalculator.interfaces.OnFragmenеListener;
import com.example.roshk1n.foodcalculator.adapters.RecyclerInfoAdapter;
import com.example.roshk1n.foodcalculator.presenters.InfoFoodPresenterImpl;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;

public class InfoFoodFragment extends Fragment implements InfoFoodView, View.OnClickListener {

    private final static String FOOD_KEY = "food";
    private InfoFoodPresenterImpl presenter;
    private Food food;
    private OnFragmenеListener mFragmentListener;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerInfoAdapter mAdapter;

    private View view;
    private CoordinatorLayout coordinatorLayout;
    private TextView name_food_tv;
    private TextView calories_food_tv;
    private TextView cabs_food_tv;
    private TextView protein_food_tv;
    private TextView fat_food_tv;
    private ImageView favorites_iv;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new InfoFoodPresenterImpl();
        presenter.setView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_info_food, container, false);

        initUI();

        if(mFragmentListener != null) {
            mFragmentListener.setArrowToolbar();
            mFragmentListener.disabledMenuSwipe();
        }

        if(getArguments() != null) {
            food = getArguments().getParcelable(FOOD_KEY);
            presenter.isExistFavorite(food);
            setNutrients(); //set filed from parcelable
        }

        favorites_iv.setOnClickListener(this);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerInfoAdapter(food.getNutrients());
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmenеListener) {
            mFragmentListener = (OnFragmenеListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentListener = null;
    }

    @Override
    public void onClick(View v) {
        if (v == favorites_iv) {
            if (favorites_iv.getDrawable().getConstantState() == getResources()
                    .getDrawable(R.drawable.ic_favorite_border_black_24dp)
                    .getConstantState()) {
                presenter.addToFavorite(food);
                favorites_iv.setImageResource(R.drawable.ic_favorite_black_24dp);
                Snackbar.make(coordinatorLayout, "Adding a food to favorites is complete.", Snackbar.LENGTH_SHORT).show();
            } else {
                presenter.removeFromFavorite(food.getNdbno());
                favorites_iv.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                Snackbar.make(coordinatorLayout, "Deleting a food from favorites is complete.", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void updateFavoriteImage(boolean existIn) {
        if (existIn) {
            favorites_iv.setImageResource(R.drawable.ic_favorite_black_24dp);

        } else {
            favorites_iv.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
    }

    private void initUI() {
        name_food_tv = (TextView) view.findViewById(R.id.name_food_info_tv);
        calories_food_tv = (TextView) view.findViewById(R.id.calories_food_info_tv);
        cabs_food_tv = (TextView) view.findViewById(R.id.cabs_food_info_tv);
        protein_food_tv = (TextView) view.findViewById(R.id.protein_food_info_tv);
        fat_food_tv = (TextView) view.findViewById(R.id.fat_food_info_tv);
        favorites_iv = (ImageView) view.findViewById(R.id.favorites_add_info_iv);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_info_food);
        coordinatorLayout = (CoordinatorLayout) getActivity().findViewById(R.id.coordinator_layout);
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
