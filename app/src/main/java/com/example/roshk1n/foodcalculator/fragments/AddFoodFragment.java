package com.example.roshk1n.foodcalculator.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.interfaces.OnFragmentListener;
import com.example.roshk1n.foodcalculator.presenters.AddFoodPresenterImpl;
import com.example.roshk1n.foodcalculator.views.AddFoodView;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;
import com.example.roshk1n.foodcalculator.utils.Utils;


public class AddFoodFragment extends Fragment implements AddFoodView, View.OnClickListener, View.OnFocusChangeListener {
    private static final String FOOD_KEY = "food";

    private AddFoodPresenterImpl presenter;
    private OnFragmentListener mFragmentListener;
    private Food food;
    private boolean isExistFavorite;

    private View view;
    private CoordinatorLayout coordinatorLayout;
    private Button mAddFoodBtn;
    private TextView nameFoodTv;
    private TextView cabsFoodTv;
    private TextView fatFoodTv;
    private TextView proteinFoodTv;
    private TextView caloriesFoodTv;
    private EditText numberOfServingsEt;
    private ImageView addFavoriteIv;

    public static AddFoodFragment newInstance(Food food) {
        AddFoodFragment addFoodFragment = new AddFoodFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(FOOD_KEY, food);
        addFoodFragment.setArguments(bundle);
        return addFoodFragment;
    }

    public static AddFoodFragment newInstance() {
        return new AddFoodFragment();
    }

    public AddFoodFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new AddFoodPresenterImpl();
        presenter.setView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_food, container, false);
        initUI();
        numberOfServingsEt.setOnFocusChangeListener(this);
        if(mFragmentListener != null) {
            mFragmentListener.setTitle(getString(R.string.add_food));
            mFragmentListener.setArrowToolbar();
            mFragmentListener.disabledMenuSwipe();
        }

        if (getArguments() != null) {
            food = getArguments().getParcelable(FOOD_KEY);
            presenter.isExistFavorite(food);
            setNutrients(food.getNutrients().get(1).getValue(), food.getNutrients().get(2).getValue(),
                    food.getNutrients().get(3).getValue(), food.getNutrients().get(4).getValue(),
                    food.getName());
        }

        addFavoriteIv.setOnClickListener(this);
        mAddFoodBtn.setOnClickListener(this);

        numberOfServingsEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    presenter.updateUI(food, Integer.valueOf(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentListener) {
            mFragmentListener = (OnFragmentListener) context;
        }
    }

    @Override
    public void onDetach() {
        mFragmentListener = null;
        presenter.destroy();
        super.onDetach();
    }

    @Override
    public void navigateToDiary() {
        Utils.clearBackStack(getActivity().getSupportFragmentManager());
        Utils.hideKeyboard(getContext(), getActivity().getCurrentFocus());
        Snackbar.make(coordinatorLayout, R.string.add_food_success, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setNutrients(String calories, String protein, String fat, String cabs, String name) {
        caloriesFoodTv.setText(calories);
        proteinFoodTv.setText(protein);
        fatFoodTv.setText(fat);
        cabsFoodTv.setText(cabs);
        nameFoodTv.setText(name);
    }

    @Override
    public void updateFavoriteImage(boolean existIn) {
        addFavoriteIv.setClickable(true);
        if (existIn) {
            addFavoriteIv.setImageResource(R.drawable.ic_favorite_black_24dp);
            isExistFavorite = true;

        } else {
            addFavoriteIv.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            isExistFavorite = false;
        }
    }

    @Override
    public void onClick(View v) {
        if (v == addFavoriteIv) {
            if (!isExistFavorite) {
                addFavoriteIv.setClickable(false);
                presenter.addToFavorite(food);
                Snackbar.make(coordinatorLayout, R.string.add_food_favorite, Snackbar.LENGTH_SHORT).show();

            } else {
                addFavoriteIv.setClickable(false);
                presenter.removeFromFavorite(food.getNdbno());
                Snackbar.make(coordinatorLayout, R.string.delete_food_favorite, Snackbar.LENGTH_SHORT).show();
            }

        } else if (v == mAddFoodBtn) {
            if (food != null) {
                if (!numberOfServingsEt.getText().toString().equals("")) {
                    food = presenter.updateFood(food, caloriesFoodTv.getText().toString(),
                            proteinFoodTv.getText().toString(),
                            fatFoodTv.getText().toString(),
                            cabsFoodTv.getText().toString(),
                            nameFoodTv.getText().toString(),
                            numberOfServingsEt.getText().toString());

                    Utils.hideKeyboard(getContext(), getActivity().getCurrentFocus());
                    presenter.addNewFood(food);
                } else {
                    numberOfServingsEt.setError(getString(R.string.error_number_sevings));
                }
            }
        }
    }

    private void initUI() {
        cabsFoodTv = (TextView) view.findViewById(R.id.tv_cabs_food_add);
        fatFoodTv = (TextView) view.findViewById(R.id.tv_fat_food_add);
        proteinFoodTv = (TextView) view.findViewById(R.id.tv_protein_food_add);
        caloriesFoodTv = (TextView) view.findViewById(R.id.tv_calories_food_add);
        nameFoodTv = (TextView) view.findViewById(R.id.tv_name_food_add);
        numberOfServingsEt = (EditText) view.findViewById(R.id.number_of_servings_et);
        mAddFoodBtn = (Button) view.findViewById(R.id.add_food_btn);
        addFavoriteIv = (ImageView) view.findViewById(R.id.favorites_add_iv);
        coordinatorLayout = (CoordinatorLayout) getActivity().findViewById(R.id.coordinator_layout);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(!hasFocus) {
            Utils.hideKeyboard(getContext(),v);
            v.clearFocus();
        }
    }
}
