package com.example.roshk1n.foodcalculator.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;

import java.util.ArrayList;

/**
 * Created by roshk1n on 8/2/2016.
 */
public class RecyclerFavoriteAdapter extends RecyclerView.Adapter<RecyclerFavoriteAdapter.ViewHolder> {

    private ArrayList<Food> favoriteList;
    private View v;

    public RecyclerFavoriteAdapter(ArrayList<Food> favoriteList) {
        this.favoriteList = favoriteList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName;
        private TextView tvValuePor;
        private TextView tvAmountCal;
        private ArrayList<Food> favoriteList;

        public ViewHolder(View v, ArrayList<Food> favoriteList) {
            super(v);
            this.favoriteList = favoriteList;
            tvName = (TextView) v.findViewById(R.id.tvSearchName);
            tvValuePor = (TextView) v.findViewById(R.id.tv_value_por_search);
            tvAmountCal = (TextView) v.findViewById(R.id.tv_amout_cal_search);
        }

        public void setDate() {
            tvName.setText(favoriteList.get(getAdapterPosition()).getName());
            tvAmountCal.setText(favoriteList.get(getAdapterPosition()).getNutrients().get(1).getValue() + " cal.");
            tvValuePor.setText("100" + " g, ");
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item_of_search,parent,false);
        return  new ViewHolder(v,favoriteList);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setDate();
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }
}
