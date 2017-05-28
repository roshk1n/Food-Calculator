package com.example.roshk1n.foodcalculator.presenters;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.roshk1n.foodcalculator.interfaces.RetrofitCallback;
import com.example.roshk1n.foodcalculator.interfaces.SearchFoodCallback;
import com.example.roshk1n.foodcalculator.manageres.DataManager;
import com.example.roshk1n.foodcalculator.manageres.RetrofitManager;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;
import com.example.roshk1n.foodcalculator.views.SearchView;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.FoodResponse;

import java.util.ArrayList;

public class SearchPresenterImpl implements SearchPresenter, RetrofitCallback {
    private RetrofitManager retrofitManager = new RetrofitManager(this);
    private DataManager dataManager = new DataManager();
    private SearchView searchView;

    private SearchFoodCallback searchFoodCallback = new SearchFoodCallback() {
        @Override
        public void setFood(Food foodResult) {
            searchView.setFoodNutrients(foodResult);
            Log.i("s", "setFood: ");
        }

        @Override
        public void error(String message) {
            if (searchView != null)
                searchView.showSnackBar(message);
        }
    };

    public SearchPresenterImpl() {
    }

    public void setView(SearchView view) {
        this.searchView = view;
    }

    @Override
    public void searchFood(Context context, String search) {
        dataManager.searchFood(context, search, searchFoodCallback);
       // retrofitManager.searchFoodApi(search);
    }


    @Override
    public void destroy() {
        searchView = null;
    }

    @Override
    public void addFood(FoodResponse foodResponse) {
        if (searchView != null) {
            searchView.setFoodNutrients(foodResponse.getReport().getFood());
        }
    }

    @Override
    public void setError(String message) {
        if (searchView != null)
            searchView.showSnackBar(message);
    }
}
