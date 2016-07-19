package com.example.roshk1n.foodcalculator.main.fragments.remiders;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.roshk1n.foodcalculator.R;

public class RemindersFragment extends Fragment implements View.OnClickListener {

    private View view;
    private CardView cvBreakfast;
    private CardView cvLunch;
    private CardView cvDinner;
    private CardView cvSnack;

    public RemindersFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reminders, container, false);

        cvBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MYY","sf");
            }
        });
        return view;

    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.card_view_breakfast : {

            }
            break;

            case R.id.card_view_lunch : {

            }
            break;
            case R.id.card_view_dinner : {

            }
            break;
            case R.id.card_view_skack : {

            }
            break;
        }

    }
    void initUI() {
        cvBreakfast = (CardView) view.findViewById(R.id.card_view_breakfast);
        cvLunch = (CardView) view.findViewById(R.id.card_view_lunch);
        cvDinner = (CardView) view.findViewById(R.id.card_view_dinner);
        cvSnack = (CardView) view.findViewById(R.id.card_view_skack);
    }


}
