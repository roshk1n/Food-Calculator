package com.example.roshk1n.foodcalculator.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.views.InfoFoodView;
import com.example.roshk1n.foodcalculator.interfaces.OnFragmentListener;
import com.example.roshk1n.foodcalculator.adapters.RecyclerInfoAdapter;
import com.example.roshk1n.foodcalculator.presenters.InfoFoodPresenterImpl;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;

public class InfoFoodFragment extends Fragment implements InfoFoodView, View.OnClickListener {
    private final static String FOOD_KEY = "food";
    private InfoFoodPresenterImpl presenter;
    private Food food;
    private OnFragmentListener mFragmentListener;
    private boolean isExistFavorite;
    private RecyclerView mRecyclerView;

    private View view;
    private CoordinatorLayout coordinatorLayout;
    private TextView nameFoodTv;
    private TextView caloriesFoodTv;
    private TextView cabsFoodTv;
    private TextView proteinFoodTv;
    private TextView fatFoodTv;
    private ImageView favoritesIv;

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
        isExistFavorite = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_info_food, container, false);

        initUI();

        if(mFragmentListener != null) {
            mFragmentListener.setTitle(getString(R.string.info_food));
            mFragmentListener.setArrowToolbar();
            mFragmentListener.disabledMenuSwipe();
        }

        if(getArguments() != null) {
            food = getArguments().getParcelable(FOOD_KEY);
            presenter.isExistFavorite(food);
            setNutrients(); //set filed from parcelable
        }

        favoritesIv.setOnClickListener(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerInfoAdapter mAdapter = new RecyclerInfoAdapter(food.getNutrients());
        mRecyclerView.setAdapter(mAdapter);

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
        mFragmentListener = null;
        presenter.destroy();
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        if (v == favoritesIv) {
            if (!isExistFavorite) {
                favoritesIv.setClickable(false);
                presenter.addToFavorite(food);
                Snackbar.make(coordinatorLayout, getString(R.string.add_food_favorite), Snackbar.LENGTH_SHORT).show();
            } else {
                favoritesIv.setClickable(false);
                presenter.removeFromFavorite(food.getNdbno());
                Snackbar.make(coordinatorLayout, getString(R.string.delete_food_favorite), Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void updateFavoriteImage(boolean existIn) {
        favoritesIv.setClickable(true);
        if (existIn) {
            isExistFavorite = true;
            favoritesIv.setImageResource(R.drawable.ic_favorite_black_24dp);
        } else {
            isExistFavorite = false;
            favoritesIv.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
    }

    private void setNutrients() {
        nameFoodTv.setText(food.getName());
        caloriesFoodTv.setText(food.getNutrients().get(1).getValue());
        proteinFoodTv.setText(food.getNutrients().get(2).getValue());
        fatFoodTv.setText(food.getNutrients().get(3).getValue());
        cabsFoodTv.setText(food.getNutrients().get(4).getValue());
    }

    private void initUI() {
        nameFoodTv = (TextView) view.findViewById(R.id.name_food_info_tv);
        caloriesFoodTv = (TextView) view.findViewById(R.id.calories_food_info_tv);
        cabsFoodTv = (TextView) view.findViewById(R.id.cabs_food_info_tv);
        proteinFoodTv = (TextView) view.findViewById(R.id.protein_food_info_tv);
        fatFoodTv = (TextView) view.findViewById(R.id.fat_food_info_tv);
        favoritesIv = (ImageView) view.findViewById(R.id.favorites_add_info_iv);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_info_food);
        coordinatorLayout = (CoordinatorLayout) getActivity().findViewById(R.id.coordinator_layout);
    }
}
