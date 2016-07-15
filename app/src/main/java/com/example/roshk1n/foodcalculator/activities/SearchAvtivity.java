package com.example.roshk1n.foodcalculator.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.roshk1n.foodcalculator.Food;
import com.example.roshk1n.foodcalculator.MyApplication;
import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.RecyclerSearchAdapter;

import java.util.ArrayList;

public class SearchAvtivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        ArrayList<Food> myDataset = getDataSet();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_search);

        //recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerSearchAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);


    }
    private ArrayList<Food> getDataSet() {

       ArrayList<Food> foods = new ArrayList<>();
        foods.add(new Food("Картопля смажена",120,100.0f));
        foods.add(new Food("Картопля печена",100,100.0f));
        foods.add(new Food("Макарон",150,100.0f));
        foods.add(new Food("Гриби",60,100.0f));
        foods.add(new Food("Сир францкзький",200,100.0f));
        foods.add(new Food("Мясо свинина",150,100.0f));
        foods.add(new Food("Картопля смажена",120,100.0f));
        foods.add(new Food("Картопля печена",100,100.0f));
        foods.add(new Food("Макарон",150,100.0f));
        foods.add(new Food("Гриби",60,100.0f));
        foods.add(new Food("Сир францкзький",200,100.0f));
        foods.add(new Food("Мясо свинина",150,100.0f));
        foods.add(new Food("Картопля смажена",120,100.0f));
        foods.add(new Food("Картопля печена",100,100.0f));
        foods.add(new Food("Макарон",150,100.0f));
        foods.add(new Food("Гриби",60,100.0f));
        foods.add(new Food("Сир францкзький",200,100.0f));
        foods.add(new Food("Мясо свинина",150,100.0f));
        foods.add(new Food("Картопля смажена",120,100.0f));
        foods.add(new Food("Картопля печена",100,100.0f));
        foods.add(new Food("Макарон",150,100.0f));
        foods.add(new Food("Гриби",60,100.0f));
        foods.add(new Food("Сир францкзький",200,100.0f));
        foods.add(new Food("Мясо свинина",150,100.0f));

        return foods;
    }
}
