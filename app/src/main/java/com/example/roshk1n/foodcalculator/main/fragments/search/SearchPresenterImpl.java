package com.example.roshk1n.foodcalculator.main.fragments.search;

import com.example.roshk1n.foodcalculator.MyApplication;
import com.example.roshk1n.foodcalculator.rest.RestClient;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.ListFoodResponse;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.NutrientFoodResponse;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by roshk1n on 7/19/2016.
 */
public class SearchPresenterImpl implements SearchPresenter {

    private ArrayList<NutrientFoodResponse> nutrientFoodResponses = new ArrayList<NutrientFoodResponse>();
    private String[] nutrients = {"204","208","205","203"};;

    private SearchView searchView;

    public SearchPresenterImpl() {}

    public void setView(SearchView view) {
        this.searchView = view;
    }

    @Override
    public void searchFood(final String search) {

        final RestClient restClient = MyApplication.getRestClient();
        restClient.getNdbApi().searchFood("json", search,"20"
                ,restClient.getApi_key(), new Callback<ListFoodResponse>() {
            @Override
            public void success(final ListFoodResponse listFoodResponse, Response response) {
                for(int i =0;i<listFoodResponse.getList().getItem().size();i++)
                {
                    MyApplication.getRestClient().getNdbApi().getNutrientFood(listFoodResponse.getList().getItem().get(i).getNdbno(),nutrients, restClient.getApi_key(), new Callback<NutrientFoodResponse>() {
                        @Override
                        public void success(NutrientFoodResponse nutrientFoodResponse, Response response) {
                            if(nutrientFoodResponse.getReport().getFoods().size()>0) {
                                nutrientFoodResponses.add(nutrientFoodResponse);
                            }
                        }
                        @Override
                        public void failure(RetrofitError error) {
                        }
                    });
                }
                searchView.updateUI(nutrientFoodResponses);
            }
            @Override
            public void failure(RetrofitError error) {
            }
        });

    }

}
