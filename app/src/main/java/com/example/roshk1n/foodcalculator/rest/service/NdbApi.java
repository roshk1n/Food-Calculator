package com.example.roshk1n.foodcalculator.rest.service;


import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.InfoFoodResponse;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.SearchResponse;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
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
            , Callback<SearchResponse> callback);

    @GET("/ndb/reports/")
    void searchNutrientFood(@Query("ndbno") String ndbno
            , @Query("api_key") String api_key
            , Callback<InfoFoodResponse> callback);


}
