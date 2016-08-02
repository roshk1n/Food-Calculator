package com.example.roshk1n.foodcalculator.main.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.realm.FavoriteListRealm;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.NutrientFoodResponse;

import java.util.ArrayList;

/**
 * Created by roshk1n on 8/2/2016.
 */
public class RecyclerFavoriteAdapter extends RecyclerView.Adapter<RecyclerFavoriteAdapter.ViewHolder> {

    private FavoriteListRealm favoriteListRealm;

    private View v;

    public RecyclerFavoriteAdapter(FavoriteListRealm favoriteListRealm) {
        this.favoriteListRealm = favoriteListRealm;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvName;
        public TextView tvValuePor;
        public TextView tvAmoutCal;

        public ViewHolder(View v)
        {
            super(v);
            tvName = (TextView) v.findViewById(R.id.tvSearchName);
            tvValuePor = (TextView) v.findViewById(R.id.tv_value_por_search);
            tvAmoutCal = (TextView) v.findViewById(R.id.tv_amout_cal_search);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item_of_search,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return  vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvName.setText(favoriteListRealm.getFoods().get(position).getName());
        holder.tvAmoutCal.setText(favoriteListRealm.getFoods().get(position).getNutrients().get(1).getValue() + " cal.");
        holder.tvValuePor.setText("100" + " g, ");
    }


    @Override
    public int getItemCount() {
        return favoriteListRealm.getFoods().size();
    }


}
