package com.example.roshk1n.foodcalculator.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.responseAdapter.ResponseSearchAdapter;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.NutrientFoodResponse;

import java.util.ArrayList;

public class RecyclerSearchAdapter extends RecyclerView.Adapter<RecyclerSearchAdapter.ViewHolder> {

    private ArrayList<NutrientFoodResponse> foods;
    private ResponseSearchAdapter responseSearchAdapter;
    private long date;
    private View v;

    public RecyclerSearchAdapter(ArrayList<NutrientFoodResponse> foods, long date, ResponseSearchAdapter responseSearchAdapter) {
        this.foods = foods;
        this.date = date;
        this.responseSearchAdapter = responseSearchAdapter;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvName;
        public TextView tvValuePor;
        public TextView tvAmountCal;
        private CardView searchCardView;

        public ViewHolder(View v)
        {
            super(v);
            tvName = (TextView) v.findViewById(R.id.tvSearchName);
            tvValuePor = (TextView) v.findViewById(R.id.tv_value_por_search);
            tvAmountCal = (TextView) v.findViewById(R.id.tv_amout_cal_search);
            searchCardView = (CardView) v.findViewById(R.id.item_search_card_view);
        }
    }

    @Override
    public RecyclerSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item_of_search,parent,false);
        return  new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

            holder.tvName.setText(foods.get(position).getReport().getFoods().get(0).getName());
            holder.tvAmountCal.setText(String.valueOf(foods.get(position).getReport().getFoods().get(0).getNutrients().get(1).getValue() + " cal"));
            holder.tvValuePor.setText("100" + " g, ");

            holder.searchCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    foods.get(holder.getAdapterPosition()).getReport().getFoods().get(0).setDate(date);
                    Food food = foods.get(holder.getAdapterPosition()).getReport().getFoods().get(0);
                    responseSearchAdapter.navigateToInfoFood(food);
                }
            });
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

}
