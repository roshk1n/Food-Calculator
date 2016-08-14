package com.example.roshk1n.foodcalculator;

import com.example.roshk1n.foodcalculator.rest.RestClient;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.ListFoodResponse;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.FoodResponse;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RetrofitManager {

    CallbackRetrofit callbackRetrofit;

    public RetrofitManager(CallbackRetrofit callbackRetrofit) {
        this.callbackRetrofit = callbackRetrofit;
    }

    public void searchFoodApi(String search) {
        final RestClient restClient = MyApplication.getRestClient();
        restClient.getNdbService().searchFood("json", search, "20"
                , restClient.getApi_key(), new Callback<ListFoodResponse>() {// get list id product
                    @Override
                    public void success(final ListFoodResponse listFoodResponse, Response response) {
                        for (int i = 0; i < listFoodResponse.getList().getItem().size(); i++) {
                            MyApplication
                                    .getRestClient()
                                    .getNdbService()
                                    .getNutrientsFood(restClient.getApi_key(),
                                            listFoodResponse.getList().getItem().get(i).getNdbno(),
                                            "b", new Callback<FoodResponse>() {
                                                @Override
                                                public void success(FoodResponse foodResponse,
                                                                    Response response) {
                                                    callbackRetrofit.addFood(foodResponse);
                                                }

                                                @Override
                                                public void failure(RetrofitError error) {
                                                    callbackRetrofit.errorNetwork();
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
