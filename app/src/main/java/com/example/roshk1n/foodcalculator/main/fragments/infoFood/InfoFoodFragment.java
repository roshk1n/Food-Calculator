package com.example.roshk1n.foodcalculator.main.fragments.infoFood;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.roshk1n.foodcalculator.main.MainActivity;
import com.example.roshk1n.foodcalculator.main.fragments.diary.DiaryFragment;
import com.example.roshk1n.foodcalculator.main.fragments.search.SearchFragment;
import com.example.roshk1n.foodcalculator.realm.FoodRealm;
import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class InfoFoodFragment extends Fragment {

    private Food food;

    private View view;
    private Button mAddFoodBtn;
    private TextView nameFoodtv;
    private TextView cabsFoodtv;
    private TextView fatFoodtv;
    private TextView proteinFoodtv;
    private TextView caloriesFoodtv;

    public static InfoFoodFragment newInstance(Food food) {
        InfoFoodFragment infoFoodFragment = new InfoFoodFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("food", food);
        infoFoodFragment.setArguments(bundle);
        return infoFoodFragment;
    }

    public static InfoFoodFragment newInstance() {
        return new InfoFoodFragment();
    }

    public InfoFoodFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_info_food, container, false);

        initUI();

        Bundle bundle = getArguments();

        if(bundle != null) {
            food = bundle.getParcelable("food");
            proteinFoodtv.setText(food.getNutrients().get(0).getValue());
            caloriesFoodtv.setText(food.getNutrients().get(1).getValue());
            fatFoodtv.setText(food.getNutrients().get(2).getValue());
            cabsFoodtv.setText(food.getNutrients().get(3).getValue());
            nameFoodtv.setText(food.getName());
        }
        mAddFoodBtn = (Button) view.findViewById(R.id.add_food_btn);

        mAddFoodBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (food !=null) {
                    Realm realm = Realm.getDefaultInstance();
                    FoodRealm foodRealm = food.converToRealm();
                    realm.beginTransaction();
                    realm.copyToRealm(foodRealm);
                    realm.commitTransaction();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .replace(R.id.fragment_conteiner, SearchFragment.newInstance())
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        return view;
    }

    public void initUI() {
        cabsFoodtv = (TextView) view.findViewById(R.id.tv_cabs_food_info);
        fatFoodtv = (TextView) view.findViewById(R.id.tv_fat_food_info);
        proteinFoodtv = (TextView) view.findViewById(R.id.tv_protein_food_info);
        caloriesFoodtv = (TextView) view.findViewById(R.id.tv_calories_food_info);
        nameFoodtv = (TextView) view.findViewById(R.id.tv_name_food_info);
    }
}
