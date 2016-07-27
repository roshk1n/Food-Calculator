package com.example.roshk1n.foodcalculator.main.fragments.infoFood;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.roshk1n.foodcalculator.main.fragments.diary.DiaryFragment;
import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.realm.FoodRealm;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;

import java.text.DecimalFormat;

public class InfoFoodFragment extends Fragment implements InfoFoodView {

    private InfoFoodPresenterImpl infoFoodPresenter;
    private Food food;

    private View view;
    private Button mAddFoodBtn;
    private TextView nameFoodtv;
    private TextView cabsFoodtv;
    private TextView fatFoodtv;
    private TextView proteinFoodtv;
    private TextView caloriesFoodtv;
    private TextView numberOfServingsEt;

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

        infoFoodPresenter = new InfoFoodPresenterImpl();
        infoFoodPresenter.setView(this);

        Bundle bundle = getArguments();

        if(bundle != null) {
            food = bundle.getParcelable("food");
            setNutrients(food.getNutrients().get(0).getValue(),food.getNutrients().get(1).getValue()
                    ,food.getNutrients().get(2).getValue(),food.getNutrients().get(3).getValue()
                    ,food.getName());
        }

        mAddFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (food !=null) {
                    if (!numberOfServingsEt.getText().toString().equals("")) {
                       food = infoFoodPresenter.updateFood(food,proteinFoodtv.getText().toString()
                                ,caloriesFoodtv.getText().toString()
                                ,fatFoodtv.getText().toString()
                                ,cabsFoodtv.getText().toString()
                                ,nameFoodtv.getText().toString());

                        infoFoodPresenter.addNewFood(food);
                    } else {
                        numberOfServingsEt.setError("Enter number of servings please.");
                    }
                }
            }
        });

        numberOfServingsEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0) {
                    infoFoodPresenter.updateUI(food,Integer.valueOf(s.toString()));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    @Override
    public void navigateToDiary() {
        getActivity().getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragment_conteiner, DiaryFragment.newInstance())
                .commit();
    }

    @Override
    public void setNutrients(String protein, String calories, String fat, String cabs,String name) {
        proteinFoodtv.setText(protein);
        caloriesFoodtv.setText(calories);
        fatFoodtv.setText(fat);
        cabsFoodtv.setText(cabs);
        nameFoodtv.setText(name);
    }

    public void initUI() {
        cabsFoodtv = (TextView) view.findViewById(R.id.tv_cabs_food_info);
        fatFoodtv = (TextView) view.findViewById(R.id.tv_fat_food_info);
        proteinFoodtv = (TextView) view.findViewById(R.id.tv_protein_food_info);
        caloriesFoodtv = (TextView) view.findViewById(R.id.tv_calories_food_info);
        nameFoodtv = (TextView) view.findViewById(R.id.tv_name_food_info);
        numberOfServingsEt = (EditText) view.findViewById(R.id.number_of_servings_et);
        mAddFoodBtn = (Button) view.findViewById(R.id.add_food_btn);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Add Food");
    }
}
