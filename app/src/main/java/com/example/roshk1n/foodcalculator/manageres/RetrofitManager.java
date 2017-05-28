package com.example.roshk1n.foodcalculator.manageres;

import com.example.roshk1n.foodcalculator.FoodCalculatorApplication;
import com.example.roshk1n.foodcalculator.interfaces.RetrofitCallback;
import com.example.roshk1n.foodcalculator.rest.RestClient;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.ListFoodResponse;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.FoodResponse;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RetrofitManager {
    RetrofitCallback retrofitCallback;

    public RetrofitManager(RetrofitCallback retrofitCallback) {
        this.retrofitCallback = retrofitCallback;
    }

    public void searchFoodApi(String search) {
        final RestClient restClient = FoodCalculatorApplication.getRestClient();
        restClient.getNdbService().searchFood("json", search, "20"
                , restClient.getApi_key(), new Callback<ListFoodResponse>() {// get list id product
                    @Override
                    public void success(final ListFoodResponse listFoodResponse, Response response) {
                        if(listFoodResponse.getList() != null) {
                            for (int i = 0; i < listFoodResponse.getList().getItem().size(); i++) {
                                FoodCalculatorApplication
                                        .getRestClient()
                                        .getNdbService()
                                        .getNutrientsFood(restClient.getApi_key(),
                                                listFoodResponse.getList().getItem().get(i).getNdbno(),
                                                "b", new Callback<FoodResponse>() {
                                                    @Override
                                                    public void success(FoodResponse foodResponse,
                                                                        Response response) {
                                                        retrofitCallback.addFood(foodResponse);
                                                    }

                                                    @Override
                                                    public void failure(RetrofitError error) {
                                                    }
                                                });
                            }
                        } else {
                            retrofitCallback.setError("Result is empty, change search parameter.");
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if(error.isNetworkError())
                            retrofitCallback.setError("Error network connection.");
                        else
                            retrofitCallback.setError("Result is empty, change search parameter.");
                    }
                });
    }
}
