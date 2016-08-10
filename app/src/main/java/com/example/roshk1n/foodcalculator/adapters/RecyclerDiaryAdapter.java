package com.example.roshk1n.foodcalculator.adapters;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.realm.FoodRealm;

import io.realm.RealmList;

/**
 * Created by roshk1n on 7/17/2016.
 */

public class RecyclerDiaryAdapter extends RecyclerView.Adapter<RecyclerDiaryAdapter.ViewHolder> {

    private RealmList<FoodRealm> foods;

    public RealmList<FoodRealm> getFoods() { return foods; }

    public RecyclerDiaryAdapter(RealmList<FoodRealm> foods) {
        this.foods = foods;

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
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.nameFoodTv.setText(foods.get(position).getName());
        holder.amountCalTv.setText(foods.get(position).getNutrients().get(1).getValue());
        holder.valuePorTv.setText(foods.get(position).getPortion()*100 + " g.");
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }
}
