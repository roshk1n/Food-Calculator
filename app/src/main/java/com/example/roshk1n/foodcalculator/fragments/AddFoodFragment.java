package com.example.roshk1n.foodcalculator.fragments;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.activities.MainActivity;
import com.example.roshk1n.foodcalculator.presenters.AddFoodPresenterImpl;
import com.example.roshk1n.foodcalculator.Views.AddFoodView;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;

public class AddFoodFragment extends Fragment implements AddFoodView {

    private AddFoodPresenterImpl infoFoodPresenter;
    private Food food;

    private View view;
    private CoordinatorLayout coordinatorLayout;
    private Button mAddFoodBtn;
    private TextView nameFoodTv;
    private TextView cabsFoodTv;
    private TextView fatFoodTv;
    private TextView proteinFoodTv;
    private TextView caloriesFoodTv;
    private TextView numberOfServingsEt;
    private ImageView addFavoriteIv;

    public static AddFoodFragment newInstance(Food food) {
        AddFoodFragment addFoodFragment = new AddFoodFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("food", food);
        addFoodFragment.setArguments(bundle);
        return addFoodFragment;
    }

    public static AddFoodFragment newInstance() {
        return new AddFoodFragment();
    }

    public AddFoodFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_info_food, container, false);

        initUI();

        infoFoodPresenter = new AddFoodPresenterImpl();
        infoFoodPresenter.setView(this);

        Bundle bundle = getArguments();

        if(bundle != null) {
            food = bundle.getParcelable("food");
            infoFoodPresenter.isExistFavorite(food);
            setNutrients(food.getNutrients().get(0).getValue(),food.getNutrients().get(1).getValue()
                    ,food.getNutrients().get(2).getValue(),food.getNutrients().get(3).getValue()
                    ,food.getName());
        }

        mAddFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (food !=null) {
                    if (!numberOfServingsEt.getText().toString().equals("")) {
                       food = infoFoodPresenter.updateFood(food, proteinFoodTv.getText().toString()
                                , caloriesFoodTv.getText().toString()
                                , fatFoodTv.getText().toString()
                                , cabsFoodTv.getText().toString()
                                , nameFoodTv.getText().toString()
                                ,numberOfServingsEt.getText().toString());

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

        addFavoriteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addFavoriteIv.getDrawable().getConstantState() == getResources()
                        .getDrawable(R.drawable.ic_favorite_border_black_24dp )
                        .getConstantState()) {
                    if(infoFoodPresenter.addToFavorite(food)) {
                        addFavoriteIv.setImageResource(R.drawable.ic_favorite_black_24dp);
                        Snackbar.make(coordinatorLayout, "Adding a food to favorites is complete.", Snackbar.LENGTH_SHORT).show();

                    }
                } else {

                    infoFoodPresenter.removeFromFavorite(food);
                    addFavoriteIv.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    Snackbar.make(coordinatorLayout, "Deleting a food from favorites is complete.", Snackbar.LENGTH_SHORT).show();
                }
            }

        });
        return view;
    }

    @Override
    public void navigateToDiary() {

        String name = getActivity().getSupportFragmentManager().getBackStackEntryAt(1).getName();
        getActivity().getSupportFragmentManager().popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(MainActivity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        Snackbar.make(coordinatorLayout, "Food added successfully.", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setNutrients(String protein, String calories, String fat, String cabs,String name) {
        proteinFoodTv.setText(protein);
        caloriesFoodTv.setText(calories);
        fatFoodTv.setText(fat);
        cabsFoodTv.setText(cabs);
        nameFoodTv.setText(name);
    }

    @Override
    public void updateFavoriteImage(boolean existIn) {
        if(existIn) {
            addFavoriteIv.setImageResource(R.drawable.ic_favorite_black_24dp);

        } else {
            addFavoriteIv.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
    }

    private void initUI() {
        cabsFoodTv = (TextView) view.findViewById(R.id.tv_cabs_food_info);
        fatFoodTv = (TextView) view.findViewById(R.id.tv_fat_food_info);
        proteinFoodTv = (TextView) view.findViewById(R.id.tv_protein_food_info);
        caloriesFoodTv = (TextView) view.findViewById(R.id.tv_calories_food_info);
        nameFoodTv = (TextView) view.findViewById(R.id.tv_name_food_info);
        numberOfServingsEt = (EditText) view.findViewById(R.id.number_of_servings_et);
        mAddFoodBtn = (Button) view.findViewById(R.id.add_food_btn);
        addFavoriteIv = (ImageView) view.findViewById(R.id.favorites_info_iv);
        coordinatorLayout = (CoordinatorLayout) getActivity().findViewById(R.id.coordinator_layout);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Add Food");
    }
}
