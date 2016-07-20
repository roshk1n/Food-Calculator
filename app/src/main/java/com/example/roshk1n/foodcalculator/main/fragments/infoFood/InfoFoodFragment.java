package com.example.roshk1n.foodcalculator.main.fragments.infoFood;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.model.Food;

public class InfoFoodFragment extends Fragment {

    private View view;
    private Button mAddFoodBtn;


    public static InfoFoodFragment newInstance(Food food) {
        InfoFoodFragment infoFoodFragment = new InfoFoodFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("food", food);
        infoFoodFragment.setArguments(bundle);

        return infoFoodFragment;
    }

    public InfoFoodFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        TODO;
//        Bundle bundle = getArguments();
//        bundle.getParcelable("")

        view = inflater.inflate(R.layout.fragment_info_food, container, false);

        mAddFoodBtn = (Button) view.findViewById(R.id.add_food_btn);

        mAddFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }
}
