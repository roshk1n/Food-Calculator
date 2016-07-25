package com.example.roshk1n.foodcalculator.main.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.main.MainActivity;
import com.example.roshk1n.foodcalculator.main.fragments.infoFood.InfoFoodFragment;
import com.example.roshk1n.foodcalculator.main.fragments.search.SearchFragment;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.NutrientFoodResponse;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by roshk1n on 7/12/2016.
 */
public class RecyclerSearchAdapter extends RecyclerView.Adapter<RecyclerSearchAdapter.ViewHolder> {

    private ArrayList<NutrientFoodResponse> foods;
    private View v;

    public RecyclerSearchAdapter(ArrayList<NutrientFoodResponse> foods) {
        this.foods = foods;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvName;
        public TextView tvValuePor;
        public TextView tvAmoutCal;
        private CardView searchCardView;

        public ViewHolder(View v)
        {
            super(v);
            tvName = (TextView) v.findViewById(R.id.tvSearchName);
            tvValuePor = (TextView) v.findViewById(R.id.tv_value_por_search);
            tvAmoutCal = (TextView) v.findViewById(R.id.tv_amout_cal_search);
            searchCardView = (CardView) v.findViewById(R.id.item_search_card_view);

        }

    }

    @Override
    public RecyclerSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item_of_search,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return  vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

            holder.tvName.setText(foods.get(position).getReport().getFoods().get(0).getName());
            holder.tvAmoutCal.setText(String.valueOf(foods.get(position).getReport().getFoods().get(0).getNutrients().get(1).getValue() + " cal"));
            holder.tvValuePor.setText("100" + " g, ");

            holder.searchCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Mst",SearchFragment.DATE);
                    SimpleDateFormat format1=new SimpleDateFormat("EEEE/dd/MMMM/yyyy");
                    Date finalDate= null;
                    try {
                        finalDate = format1.parse(SearchFragment.DATE);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    foods.get(holder.getAdapterPosition()).getReport().getFoods().get(0).setDate(finalDate);
                    switchFragment(InfoFoodFragment.newInstance(foods.get(holder.getAdapterPosition()).getReport().getFoods().get(0)));
                }
            });


/*        holder.tvAmoutCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //InfoFoodFragment.newInstance(foods.get(holder.getAdapterPosition()));
//                getS
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    private void switchFragment(InfoFoodFragment infoFood) {
        if (v.getContext() == null)
            return;
        if (v.getContext() instanceof MainActivity) {
            MainActivity feeds = (MainActivity) v.getContext();
            feeds.getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.fragment_conteiner, infoFood)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
