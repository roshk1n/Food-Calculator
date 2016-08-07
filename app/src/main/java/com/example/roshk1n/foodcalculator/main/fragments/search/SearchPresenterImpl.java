package com.example.roshk1n.foodcalculator.main.fragments.search;

import com.example.roshk1n.foodcalculator.MyApplication;
import com.example.roshk1n.foodcalculator.rest.RestClient;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.ListFoodResponse;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.NutrientFoodResponse;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SearchPresenterImpl implements SearchPresenter {

    private String[] nutrients = {"204","208","205","203"};

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
                                searchView.updateUI(nutrientFoodResponse);
                            }
                        }
                        @Override
                        public void failure(RetrofitError error) {
                        }
                    });
                }
            }
            @Override
            public void failure(RetrofitError error) {
            }
        });
    }
}
