package com.example.roshk1n.foodcalculator.main.adapters;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.model.Meal;

import java.util.ArrayList;

/**
 * Created by roshk1n on 7/17/2016.
 */
public class RecyclerDiaryAdapter extends RecyclerView.Adapter<RecyclerDiaryAdapter.ViewHolder> {

    private ArrayList<Meal> meals;

    private RecyclerView recyclerView;

    public RecyclerDiaryAdapter(ArrayList<Meal> meals) {
        this.meals = meals;

    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvMealName;
        public TextView tvAmoutCalMeal;

        public ViewHolder(View v)
        {
            super(v);
            tvMealName = (TextView) v.findViewById(R.id.t_view_meal);
            tvAmoutCalMeal = (TextView) v.findViewById(R.id.tv_amout_cal_meal);
        }
    }

    @Override
    public RecyclerDiaryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item_of_diary,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return  vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvMealName.setText(meals.get(position).getMealOfDay().toString());
        holder.tvAmoutCalMeal.setText("300");
        /*TODO: Тут потрібно заповнити внутрішній
              recyclerview(recycle_item_of_list_of_meal.xml------RecyclerListOfMealAdapter.java)
              я немаю доступу до view тим більше цей recycler який я хочу засунути в середу
               знаходить не на DiaryFragment а на (recycle_item_of_diary.xml)*/
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }
}
