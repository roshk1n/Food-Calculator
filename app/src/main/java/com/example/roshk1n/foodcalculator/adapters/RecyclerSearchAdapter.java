package com.example.roshk1n.foodcalculator.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.interfaces.responseAdapter.CallbackSearchAdapter;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;

import java.util.ArrayList;

public class RecyclerSearchAdapter extends RecyclerView.Adapter<RecyclerSearchAdapter.ViewHolder> {

    private ArrayList<Food> foods;
    private CallbackSearchAdapter callbackSearchAdapter;
    private long date;
    private View v;

    public RecyclerSearchAdapter(ArrayList<Food> foods, long date, CallbackSearchAdapter callbackSearchAdapter) {
        this.foods = foods;
        this.date = date;
        this.callbackSearchAdapter = callbackSearchAdapter;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ArrayList<Food> foods;
        private CallbackSearchAdapter callbackSearchAdapter;
        private long date;
        private TextView nameTv;
        private TextView valuePorTv;
        private TextView amountCalTv;
        private CardView searchCardView;

        public ViewHolder(View v, ArrayList<Food> foods, long date, CallbackSearchAdapter callback) {
            super(v);
            this.foods = foods;
            this.callbackSearchAdapter = callback;
            this.date = date;
            nameTv = (TextView) v.findViewById(R.id.tvSearchName);
            valuePorTv = (TextView) v.findViewById(R.id.tv_value_por_search);
            amountCalTv = (TextView) v.findViewById(R.id.tv_amout_cal_search);
            searchCardView = (CardView) v.findViewById(R.id.item_search_card_view);
        }

        public void setDate() {
            nameTv.setText(foods.get(getAdapterPosition()).getName());
            amountCalTv.setText(String.valueOf(foods.get(getAdapterPosition())
                    .getNutrients()
                    .get(1)
                    .getValue() + " cal"));

            valuePorTv.setText("100" + " g, ");
        }

        public void setUpListener() {
            searchCardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v == searchCardView) {
                foods.get(getAdapterPosition()).setTime(date);
                Food food = foods.get(getAdapterPosition());
                callbackSearchAdapter.navigateToAddFood(food);
            }
        }
    }

    @Override
    public RecyclerSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item_of_search,parent,false);
        return  new ViewHolder(v,foods,date,callbackSearchAdapter);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.setDate();
        holder.setUpListener();
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

}
