package com.example.roshk1n.foodcalculator.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.realm.FavoriteListRealm;

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
        public TextView tvAmountCal;

        public ViewHolder(View v)
        {
            super(v);
            tvName = (TextView) v.findViewById(R.id.tvSearchName);
            tvValuePor = (TextView) v.findViewById(R.id.tv_value_por_search);
            tvAmountCal = (TextView) v.findViewById(R.id.tv_amout_cal_search);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item_of_search,parent,false);
        return  new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvName.setText(favoriteListRealm.getFoods().get(position).getName());
        holder.tvAmountCal.setText(favoriteListRealm.getFoods().get(position).getNutrients().get(1).getValue() + " cal.");
        holder.tvValuePor.setText("100" + " g, ");
    }


    @Override
    public int getItemCount() {
        return favoriteListRealm.getFoods().size();
    }


}
