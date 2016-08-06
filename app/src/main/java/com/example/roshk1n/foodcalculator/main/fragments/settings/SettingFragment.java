package com.example.roshk1n.foodcalculator.main.fragments.settings;


import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roshk1n.foodcalculator.R;

public class SettingFragment extends Fragment implements SettingView {

    private SettingPresenterImpl settingPresenter;

    private View view;

    public SettingFragment() { }

    public static SettingFragment newInstance() { return new SettingFragment(); }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingPresenter = new SettingPresenterImpl();
        settingPresenter.setView(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.fragment_settings, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Settings");
        return view;
    }

}
