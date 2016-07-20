package com.example.roshk1n.foodcalculator.main.fragments.diary;

import android.app.Fragment;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roshk1n.foodcalculator.enums.MealOfDay;
import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.main.adapters.RecyclerDiaryAdapter;
import com.example.roshk1n.foodcalculator.main.adapters.RecyclerListOfMealAdapter;
import com.example.roshk1n.foodcalculator.model.Food;
import com.example.roshk1n.foodcalculator.model.Meal;

import java.util.ArrayList;

public class DiaryFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerListOfMealAdapter mAdapter;

    public DiaryFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary, container, false);
/*
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_list_of_meal);
        ArrayList<Food> myDataset = getDataSet();

        mLayoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerListOfMealAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);
*/

        mLayoutManager = new LinearLayoutManager(getActivity());
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_meal);
        recyclerView.setLayoutManager(mLayoutManager);

        RecyclerDiaryAdapter diaryAdapter = new RecyclerDiaryAdapter(getD());
        recyclerView.setAdapter(diaryAdapter);
        return view;
    }

    private ArrayList<Meal> getD() {
        ArrayList<Meal> meals = new ArrayList<Meal>();
        for (MealOfDay mealOfDat : MealOfDay.values()) {
            ArrayList<Food> foods = new ArrayList<>();
            foods.add(new Food("Картопля смажена",120,100.0f));
            foods.add(new Food("Картопля печена",100,100.0f));
            foods.add(new Food("Макарон",150,100.0f));
            foods.add(new Food("Гриби",60,100.0f));
            foods.add(new Food("Сир францкзький",200,100.0f));
            Meal meal = new Meal(mealOfDat,foods);
            meals.add(meal);
        }
        return meals;
    }

}
