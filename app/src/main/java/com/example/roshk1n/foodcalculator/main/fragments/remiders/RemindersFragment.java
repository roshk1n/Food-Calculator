package com.example.roshk1n.foodcalculator.main.fragments.remiders;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.roshk1n.foodcalculator.R;

public class RemindersFragment extends Fragment  implements RemindersView /*DatePickerDialog.OnDateSetListener,*/ /* DialogInterface.OnCancelListener,*/  {

    private View view;
    private CardView cvBreakfast;
    private CardView cvLunch;
    private CardView cvDinner;
    private CardView cvSnack;
    private TextView time_brakfast_tv;
    private TextView time_lunch_tv;
    private TextView time_dinner_tv;
    private TextView time_snacks_tv;

    public RemindersFragment() {}

    public static Fragment newInstance() { return new RemindersFragment(); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reminders, container, false);

        initUI();

        final RemindersPresenterImpl remindersPresenter = new RemindersPresenterImpl();
        remindersPresenter.setView(this);

        cvBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remindersPresenter.showTimePicker("breakfast",RemindersFragment.this);
              }
        });

        cvLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remindersPresenter.showTimePicker("lunch",RemindersFragment.this);
            }
        });

        cvDinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remindersPresenter.showTimePicker("dinner",RemindersFragment.this);
            }
        });

        cvSnack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remindersPresenter.showTimePicker("snacks",RemindersFragment.this);
            }
        });
        return view;

    }

    @Override
    public void SetTime(String tag,String time) {
        switch (tag) {
            case "breakfast" : {
                time_brakfast_tv.setText(time);
            } break;

            case "lunch" : {
                time_lunch_tv.setText(time);
            } break;

            case "dinner" : {
                time_dinner_tv.setText(time);
            } break;

            case "snacks" : {
                time_snacks_tv.setText(time);
            } break;
        }
    }

    void initUI() {
        cvBreakfast = (CardView) view.findViewById(R.id.card_view_breakfast);
        cvLunch = (CardView) view.findViewById(R.id.card_view_lunch);
        cvDinner = (CardView) view.findViewById(R.id.card_view_dinner);
        cvSnack = (CardView) view.findViewById(R.id.card_view_skack);
        time_brakfast_tv = (TextView) view.findViewById(R.id.tv_time_breakfast);
        time_lunch_tv = (TextView) view.findViewById(R.id.tv_time_lunch);
        time_dinner_tv = (TextView) view.findViewById(R.id.tv_time_dinner);
        time_snacks_tv = (TextView) view.findViewById(R.id.tv_time_snack);
    }


}

