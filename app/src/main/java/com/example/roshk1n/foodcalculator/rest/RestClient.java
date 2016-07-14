package com.example.roshk1n.foodcalculator.rest;


import com.example.roshk1n.foodcalculator.rest.service.LoginApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by roshk1n on 7/14/2016.
 */
public class RestClient {
    private LoginApi loginApi;

    public RestClient() {
        Gson gson = new GsonBuilder().create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(gson))
                .setEndpoint("http://146.185.180.39:4020")
                .build();

        loginApi = restAdapter.create(LoginApi.class);
    }

    public LoginApi getLoginApi() {
        return loginApi;
    }
}
