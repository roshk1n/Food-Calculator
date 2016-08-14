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
        private TextView title_nutrients_tv;
        private TextView value_nutrients_tv;
        private ArrayList<Nutrient> nutrients;

        public ViewHolder(View view, ArrayList<Nutrient> nutrients) {
            super(view);
            this.nutrients = nutrients;
            title_nutrients_tv = (TextView) view.findViewById(R.id.title_nutrients_tv);
            value_nutrients_tv = (TextView) view.findViewById(R.id.value_nutrients_tv);
        }

        public void setDate() {
            Nutrient nutrient = nutrients.get(getAdapterPosition());
            title_nutrients_tv.setText(nutrient.getName());
            value_nutrients_tv.setText(nutrient.getValue() + " " + nutrient.getUnit());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item_of_info,parent,false);
        return new ViewHolder(v,nutrients);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setDate();
    }

    @Override
    public int getItemCount() {
        return nutrients.size();
    }
}
