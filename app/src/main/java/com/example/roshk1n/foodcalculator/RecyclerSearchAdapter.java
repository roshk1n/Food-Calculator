package com.example.roshk1n.foodcalculator;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by roshk1n on 7/12/2016.
 */
public class RecyclerSearchAdapter extends RecyclerView.Adapter<RecyclerSearchAdapter.ViewHolder> {

    private ArrayList<Food> foods;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public TextView tvValuePor;
        public TextView tvAmoutCal;

        public ViewHolder(View v)
        {
            super(v);
            tvName = (TextView) v.findViewById(R.id.tvSearchName);
            tvValuePor = (TextView) v.findViewById(R.id.tv_value_por);
            tvAmoutCal = (TextView) v.findViewById(R.id.tv_amout_cal);
        }
    }

    public RecyclerSearchAdapter(ArrayList<Food> foods)
    {
        this.foods = foods;
    }
    @Override

    public RecyclerSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item_of_search,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return  vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvName.setText(foods.get(position).getName());
        holder.tvAmoutCal.setText(String.valueOf(foods.get(position).getEnergy())+" cal");
        holder.tvValuePor.setText(String.valueOf(foods.get(position).getValuePer())+" g, ");
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }
}
