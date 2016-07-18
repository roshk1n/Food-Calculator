package com.example.roshk1n.foodcalculator.main.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.roshk1n.foodcalculator.model.Food;
import com.example.roshk1n.foodcalculator.R;

import java.util.ArrayList;

/**
 * Created by roshk1n on 7/17/2016.
 */
public class RecyclerListOfMealAdapter extends RecyclerView.Adapter<RecyclerListOfMealAdapter.ViewHolder> {

    private ArrayList<Food> foods;

    public RecyclerListOfMealAdapter(ArrayList<Food> foods)
    {
        this.foods = foods;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public TextView tvValuePor;
        public TextView tvAmoutCal;

        public ViewHolder(View v)
        {
            super(v);
            tvName = (TextView) v.findViewById(R.id.t_view_food_name);
            tvValuePor = (TextView) v.findViewById(R.id.tv_value_por_diary);
            tvAmoutCal = (TextView) v.findViewById(R.id.tv_amout_cal_diary);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item_of_list_of_meal,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return  vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvName.setText(foods.get(position).getName());
        holder.tvAmoutCal.setText(String.valueOf(foods.get(position).getEnergy()));
        holder.tvValuePor.setText(String.valueOf(foods.get(position).getValuePer())+" g");
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }


}
