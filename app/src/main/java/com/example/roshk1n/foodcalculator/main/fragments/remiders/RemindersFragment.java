package com.example.roshk1n.foodcalculator.main.fragments.remiders;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.roshk1n.foodcalculator.R;

public class RemindersFragment extends Fragment {

    private View view;
    private CardView cvBreakfast;
    private CardView cvLunch;
    private CardView cvDinner;
    private CardView cvSnack;

    public RemindersFragment() {}

    public static Fragment newInstance() { return new RemindersFragment(); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reminders, container, false);

        initUI();

        cvBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cvLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cvDinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cvSnack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;

    }
    void initUI() {
        cvBreakfast = (CardView) view.findViewById(R.id.card_view_breakfast);
        cvLunch = (CardView) view.findViewById(R.id.card_view_lunch);
        cvDinner = (CardView) view.findViewById(R.id.card_view_dinner);
        cvSnack = (CardView) view.findViewById(R.id.card_view_skack);
    }



}
