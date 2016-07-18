package com.example.roshk1n.foodcalculator.main.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roshk1n.foodcalculator.ManageNdbApi;
import com.example.roshk1n.foodcalculator.model.Food;
import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.main.adapters.RecyclerSearchAdapter;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.FoodSearch;

import java.util.ArrayList;


public class SearchFragment extends Fragment {

    private View view;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerSearchAdapter mAdapter;

    public SearchFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_search);
        ArrayList<Food> myDataset = getDataSet();

        mLayoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerSearchAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

        EditText editText = (EditText) view.findViewById(R.id.et_food_name);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()!=0)
                {
                    ManageNdbApi.searchFood("json","milk","1","MmHcNZ8WUfr29ekyImQB7zPfDJSeX3Qnvi7KDcTJ");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        return view;
    }

    private ArrayList<Food> getDataSet() {
        ArrayList<Food> foods = new ArrayList<>();
        foods.add(new Food("Картопля смажена",120,100.0f));
        foods.add(new Food("Картопля печена",100,100.0f));
        foods.add(new Food("Макарон",150,100.0f));
        foods.add(new Food("Гриби",60,100.0f));
        return foods;
    }
}
