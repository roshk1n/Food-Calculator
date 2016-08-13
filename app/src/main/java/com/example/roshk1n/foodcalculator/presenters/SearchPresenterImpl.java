package com.example.roshk1n.foodcalculator.presenters;

import com.example.roshk1n.foodcalculator.CallbackRetrofit;
import com.example.roshk1n.foodcalculator.MyApplication;
import com.example.roshk1n.foodcalculator.RetrofitManager;
import com.example.roshk1n.foodcalculator.Views.SearchView;
import com.example.roshk1n.foodcalculator.rest.RestClient;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.ListFoodResponse;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.NutrientSpecialFoodResponse;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SearchPresenterImpl implements SearchPresenter, CallbackRetrofit{

    private RetrofitManager retrofitManager = new RetrofitManager(this);

    private NutrientSpecialFoodResponse nutrientSpecial;

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
    public void addFood(NutrientSpecialFoodResponse nutrientSpecial) {
        this.nutrientSpecial = nutrientSpecial;
        searchView.setFoodNutrients(nutrientSpecial);
    }

    @Override
    public void errorNetwork() {
        searchView.setErrorNetwork();
    }
}
