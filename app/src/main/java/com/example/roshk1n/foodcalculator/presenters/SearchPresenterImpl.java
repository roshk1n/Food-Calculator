package com.example.roshk1n.foodcalculator.presenters;

import com.example.roshk1n.foodcalculator.interfaces.RetrofitCallback;
import com.example.roshk1n.foodcalculator.RetrofitManager;
import com.example.roshk1n.foodcalculator.Views.SearchView;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.FoodResponse;

public class SearchPresenterImpl implements SearchPresenter, RetrofitCallback {

    private RetrofitManager retrofitManager = new RetrofitManager(this);
    private SearchView searchView;

    public SearchPresenterImpl() {}

    public void setView(SearchView view) {
        this.searchView = view;
    }

    @Override
    public void searchFood(String search) {
        retrofitManager.searchFoodApi(search);
    }

    @Override
    public void addFood(FoodResponse foodResponse) {
        searchView.setFoodNutrients(foodResponse.getReport().getFood());
    }

    @Override
    public void errorNetwork() {
        searchView.setErrorNetwork();
    }
}
