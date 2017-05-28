package com.example.roshk1n.foodcalculator.adapters;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.roshk1n.foodcalculator.Localization;
import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.interfaces.responseAdapter.CallbackDiaryAdapter;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;

import java.util.ArrayList;

public class RecyclerDiaryAdapter extends RecyclerView.Adapter<RecyclerDiaryAdapter.ViewHolder> {
    private ArrayList<Food> foods;
    private CallbackDiaryAdapter callbackDiaryAdapter;
    private View v;

    public RecyclerDiaryAdapter(ArrayList<Food> foods, CallbackDiaryAdapter callbackDiaryAdapter) {
        this.foods = foods;
        this.callbackDiaryAdapter = callbackDiaryAdapter;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ArrayList<Food> foods;
        private CallbackDiaryAdapter callbackDiaryAdapter;
        private TextView nameFoodTv;
        private TextView valuePorTv;
        private TextView amountCalTv;
        private CardView contentCv;

        public ViewHolder(View v, ArrayList<Food> foods, CallbackDiaryAdapter callbackDiaryAdapter ) {
            super(v);
            this.foods = foods;
            this.callbackDiaryAdapter = callbackDiaryAdapter;
            nameFoodTv = (TextView) v.findViewById(R.id.t_view_food_name);
            valuePorTv = (TextView) v.findViewById(R.id.tv_value_por_diary);
            amountCalTv = (TextView) v.findViewById(R.id.tv_amount_cal_diary);
            contentCv = (CardView) v.findViewById(R.id.item_diary_card_view);
        }

        public void setUpListeners() {
            contentCv.setOnClickListener(this);
        }

        public void setData() {
            if (Localization.getLanguage().equals("en")) {
                nameFoodTv.setText(foods.get(getAdapterPosition()).getNameEng());
            } else {
                nameFoodTv.setText(foods.get(getAdapterPosition()).getName());
            }
            amountCalTv.setText(String.valueOf(Math.round(Float.parseFloat(foods.get(getAdapterPosition()).getNutrients().get(1).getValue()))));
            valuePorTv.setText(String.valueOf(foods.get(getAdapterPosition()).getPortion()*100) + " g.");
        }

        @Override
        public void onClick(View v) {
            if (v == contentCv) {
                callbackDiaryAdapter.navigateToInfoFood(foods.get(getAdapterPosition()));
            }
        }
    }

    @Override
    public RecyclerDiaryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item_of_list_of_meal,parent,false);
        return  new ViewHolder(v,foods,callbackDiaryAdapter);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.setData();
        holder.setUpListeners();
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }
}
