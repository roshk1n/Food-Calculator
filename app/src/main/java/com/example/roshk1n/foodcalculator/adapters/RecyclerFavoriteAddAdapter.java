package com.example.roshk1n.foodcalculator.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.interfaces.responseAdapter.CallbackFavoriteAdapter;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;

import java.util.ArrayList;

public class RecyclerFavoriteAddAdapter extends RecyclerView.Adapter<RecyclerFavoriteAddAdapter.ViewHolder> {

    private ArrayList<Food> favoriteList;
    private CallbackFavoriteAdapter callbackFavoriteAdapter;
    private View v;

    public RecyclerFavoriteAddAdapter(ArrayList<Food> favorite, CallbackFavoriteAdapter callback) {
        this.favoriteList = favorite;
        this.callbackFavoriteAdapter = callback;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CallbackFavoriteAdapter callbackFavoriteAdapter;
        private ArrayList<Food> favoriteList;
        private TextView nameTv;
        private TextView valuePorTv;
        private TextView amountCalTv;
        private CardView itemCv;


        public ViewHolder(View v, ArrayList<Food> favoriteList, CallbackFavoriteAdapter callback) {
            super(v);
            this.favoriteList = favoriteList;
            this.callbackFavoriteAdapter = callback;
            nameTv = (TextView) v.findViewById(R.id.name_favorite_tv);
            valuePorTv = (TextView) v.findViewById(R.id.value_por_favorite_tv);
            amountCalTv = (TextView) v.findViewById(R.id.amount_cal_favorite_tv);
            itemCv = (CardView) v.findViewById(R.id.favorite_card_view);
        }

        @Override
        public void onClick(View v) {
            if (v == itemCv) {
                callbackFavoriteAdapter.navigateToAddFood(favoriteList.get(getAdapterPosition()));
            }
        }

        public void setDate() {
            nameTv.setText(favoriteList.get(getAdapterPosition()).getName());
            amountCalTv.setText(favoriteList.get(getAdapterPosition()).getNutrients().get(1).getValue() + " cal.");
            valuePorTv.setText("100" + " g, ");
        }

        public void setUpListener() {
            itemCv.setOnClickListener(this);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item_of_favorite,parent,false);
        return  new ViewHolder(v,favoriteList,callbackFavoriteAdapter);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setDate();
        holder.setUpListener();
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }
}

