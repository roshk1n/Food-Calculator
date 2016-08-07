package com.example.roshk1n.foodcalculator.main.fragments.diary;

import android.os.Bundle;
import android.support.annotation.Nullable;

public class LimitDialogFragment extends DiaryFragment {

    private static final String NUM_OF_CALORIES_KEY = "num_of_cal";

    private int numOfCalories;

    public static LimitDialogFragment newInstance(int numOfCal) {
        LimitDialogFragment limitDialogFragment = new LimitDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(NUM_OF_CALORIES_KEY, numOfCal);
        limitDialogFragment.setArguments(bundle);
        return limitDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if(bundle != null) {
            numOfCalories = bundle.getInt(NUM_OF_CALORIES_KEY);
        }
    }
}
