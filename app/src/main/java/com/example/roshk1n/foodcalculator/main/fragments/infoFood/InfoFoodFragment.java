package com.example.roshk1n.foodcalculator.main.fragments.infoFood;


import android.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roshk1n.foodcalculator.R;

public class InfoFoodFragment extends Fragment {


    public InfoFoodFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info_food, container, false);
    }
}
