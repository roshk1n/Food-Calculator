package com.example.roshk1n.foodcalculator.adapters;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.responseAdapter.CallbackDiaryAdapter;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;

import java.util.ArrayList;

public class RecyclerDiaryAdapter extends RecyclerView.Adapter<RecyclerDiaryAdapter.ViewHolder> {

    private ArrayList<Food> foods;

    private CallbackDiaryAdapter callbackDiaryAdapter;

    public RecyclerDiaryAdapter(ArrayList<Food> foods, CallbackDiaryAdapter callbackDiaryAdapter) {
        this.foods = foods;
        this.callbackDiaryAdapter = callbackDiaryAdapter;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameFoodTv;
        public TextView valuePorTv;
        public TextView amountCalTv;
        public CardView contentCv;

        public ViewHolder(View v) {
            super(v);
            nameFoodTv = (TextView) v.findViewById(R.id.t_view_food_name);
            valuePorTv = (TextView) v.findViewById(R.id.tv_value_por_diary);
            amountCalTv = (TextView) v.findViewById(R.id.tv_amout_cal_diary);
            contentCv = (CardView) v.findViewById(R.id.item_diary_card_view);
        }
    }

    @Override
    public RecyclerDiaryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item_of_list_of_meal,parent,false);
        return  new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.nameFoodTv.setText(foods.get(position).getName());
        holder.amountCalTv.setText(String.valueOf(Math.round(Float.parseFloat(foods.get(position).getNutrients().get(1).getValue()))));
        holder.valuePorTv.setText(foods.get(position).getPortion()*100 + " g.");

        holder.contentCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackDiaryAdapter.navigateToInfoFood(foods.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }
}
