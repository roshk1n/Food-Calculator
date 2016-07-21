package com.example.roshk1n.foodcalculator.main.adapters;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.model.Meal;
import com.example.roshk1n.foodcalculator.realm.FoodRealm;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;

import java.util.ArrayList;
import java.util.Date;

import io.realm.RealmResults;

/**
 * Created by roshk1n on 7/17/2016.
 */
public class RecyclerDiaryAdapter extends RecyclerView.Adapter<RecyclerDiaryAdapter.ViewHolder> {

    private RealmResults<FoodRealm>foods;

    public RealmResults<FoodRealm> getFoods() { return foods; }

    public void setFoods(RealmResults<FoodRealm> foods) { this.foods = foods; }

    public RecyclerDiaryAdapter(RealmResults<FoodRealm> foods) {
        this.foods = foods;

    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameFoodtv;
        public TextView valuePortv;
        public TextView amoutCaltv;
        public CardView contentcv;
      //  public TextView datetv;

        public ViewHolder(View v) {
            super(v);
            nameFoodtv = (TextView) v.findViewById(R.id.t_view_food_name);
            valuePortv = (TextView) v.findViewById(R.id.tv_value_por_diary);
            amoutCaltv = (TextView) v.findViewById(R.id.tv_amout_cal_diary);
            contentcv = (CardView) v.findViewById(R.id.item_diary_card_view);
            //datetv = (TextView) v.findViewById(R.id.date_tv);
        }
    }

    @Override
    public RecyclerDiaryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item_of_list_of_meal,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return  vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       // Date date = foods.get(position).getDate();
        holder.nameFoodtv.setText(foods.get(position).getName().toString());
        holder.amoutCaltv.setText(foods.get(position).getNutrients().get(1).getValue().toString());
        holder.valuePortv.setText("100 g.");
       // holder.datetv.setText(date.getHours()+" : "+date.getMinutes() );



    }

    @Override
    public int getItemCount() {
        return foods.size();
    }
}
