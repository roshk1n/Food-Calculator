package com.example.roshk1n.foodcalculator.rest.services;


import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.NutrientBasicFoodResponse;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.NutrientSpecialFoodResponse;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.ListFoodResponse;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by roshk1n on 7/18/2016.
 */
public interface NdbApi {

    @GET("/ndb/search/")
    void searchFood(@Query("format") String format,
                    @Query("q") String name,
                    @Query("max") String max,
                    @Query("api_key") String api_key ,
                    Callback<ListFoodResponse> callback);

    @GET("/ndb/nutrients/")
    void getNutrientFood(@Query("ndbno") String ndbno,
                         @Query("nutrients") String [] nutrients,
                         @Query("api_key") String api_key,
                         Callback<NutrientSpecialFoodResponse> callback);

    @GET("/ndb/reports/")
    void getBasicNutrientsFood(@Query("api_key") String api_key,
                               @Query("ndbno") String ndbno,
                               @Query("type") String type,
                               Callback<NutrientBasicFoodResponse> callback
                               );

}
