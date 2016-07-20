package com.example.roshk1n.foodcalculator.rest.service;


import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.NutrientFoodResponse;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.ListFoodResponse;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by roshk1n on 7/18/2016.
 */
public interface NdbApi {

    @GET("/ndb/search/")
    void searchFood(@Query("format") String format
            , @Query("q") String name
            , @Query("max") String max
            , @Query("api_key") String api_key
            , Callback<ListFoodResponse> callback);

    @GET("/ndb/nutrients/")
    void getNutrientFood(@Query("ndbno") String ndbno
            , @Query("nutrients") String [] nutrients
            , @Query("api_key") String api_key
            , Callback<NutrientFoodResponse> callback);


}
