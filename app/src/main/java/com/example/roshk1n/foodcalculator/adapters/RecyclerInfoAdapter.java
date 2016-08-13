package com.example.roshk1n.foodcalculator.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Nutrient;

import java.util.ArrayList;

public class RecyclerInfoAdapter extends RecyclerView.Adapter<RecyclerInfoAdapter.ViewHolder> {
    private ArrayList<Nutrient> nutrients;
    private View v;


    public RecyclerInfoAdapter(ArrayList<Nutrient> nutrients) {
        this.nutrients = nutrients;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title_nutrients_tv;
        public TextView value_nutrients_tv;

        public ViewHolder(View view) {
            super(view);
            title_nutrients_tv = (TextView) view.findViewById(R.id.title_nutrients_tv);
            value_nutrients_tv = (TextView) view.findViewById(R.id.value_nutrients_tv);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item_of_info,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Nutrient nutrient = nutrients.get(position);
        holder.title_nutrients_tv.setText(nutrient.getName());
        holder.value_nutrients_tv.setText(nutrient.getValue() + " " + nutrient.getUnit());
    }

    @Override
    public int getItemCount() {
        return nutrients.size();
    }
}
