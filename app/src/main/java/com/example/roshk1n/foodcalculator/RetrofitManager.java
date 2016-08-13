package com.example.roshk1n.foodcalculator;

import com.example.roshk1n.foodcalculator.rest.RestClient;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.ListFoodResponse;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.NutrientBasicFoodResponse;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.NutrientSpecialFoodResponse;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RetrofitManager {

    CallbackRetrofit callbackRetrofit;
    private String[] nutrients = {"204","208","205","203"};

    public RetrofitManager(CallbackRetrofit callbackRetrofit) {
        this.callbackRetrofit = callbackRetrofit;
    }

    public void searchFoodApi(String search) {
        final RestClient restClient = MyApplication.getRestClient();
        restClient.getNdbApi().searchFood("json", search,"20"
                ,restClient.getApi_key(), new Callback<ListFoodResponse>() {// get list id product
                    @Override
                    public void success(final ListFoodResponse listFoodResponse, Response response) {
                        for (int i = 0; i < listFoodResponse.getList().getItem().size(); i++) {
                            MyApplication
                                    .getRestClient()
                                    .getNdbApi()
                                    .getBasicNutrientsFood(restClient.getApi_key(),
                                            listFoodResponse.getList().getItem().get(i).getNdbno(),
                                            "b", new Callback<NutrientBasicFoodResponse>() {
                                                @Override
                                                public void success(NutrientBasicFoodResponse nutrientBasicFoodResponse, Response response) {
                                                        callbackRetrofit.addFood(nutrientSpecial);
                                                    }


                                                @Override
                                                public void failure(RetrofitError error) {

                                                }
                                            })
                                    .getNutrientFood(listFoodResponse.getList().getItem().get(i).getNdbno(),
                                            nutrients, restClient.getApi_key(),
                                            new Callback<NutrientSpecialFoodResponse>() { // get nutrients report for each food
                                                @Override
                                                public void success(NutrientSpecialFoodResponse nutrientSpecial,
                                                                    retrofit.client.Response response) {
                                                    if (nutrientSpecial.getReport().getFoods().size() > 0) {
                                                        callbackRetrofit.addFood(nutrientSpecial);
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
                            callbackRetrofit.errorNetwork();
                    }
                });
    }

}
