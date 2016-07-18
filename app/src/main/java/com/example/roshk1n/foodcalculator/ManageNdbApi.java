package com.example.roshk1n.foodcalculator;

import android.util.Log;

import com.example.roshk1n.foodcalculator.rest.RestClient;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.FoodSearch;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.InfoFoodResponse;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.SearchResponse;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by roshk1n on 7/18/2016.
 */
public class ManageNdbApi {

    private static RestClient restClient;

    public ManageNdbApi() {}

    public static void searchFood(String format, String name,String max, final String api_key)
    {
        restClient = new RestClient();
        restClient.getNdbApi().searchFood(format, name, max,api_key, new Callback<SearchResponse>() {
            @Override
            public void success(SearchResponse searchResponse, Response response) {
               restClient.getNdbApi().searchNutrientFood(searchResponse.getList().getItem().get(0).getNdbno(), api_key, new Callback<InfoFoodResponse>() {
                   @Override
                   public void success(InfoFoodResponse infoFoodResponse, Response response) {
                        Log.d("My",infoFoodResponse.getReport().getFood().getNutrients().get(0).getName());
                   }
                   @Override
                   public void failure(RetrofitError error) {

                   }
               });
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
