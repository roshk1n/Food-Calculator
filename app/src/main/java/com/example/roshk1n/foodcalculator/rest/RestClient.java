package com.example.roshk1n.foodcalculator.rest;


import com.example.roshk1n.foodcalculator.rest.service.LoginApi;
import com.example.roshk1n.foodcalculator.rest.service.NdbApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by roshk1n on 7/14/2016.
 */
public class RestClient { //TODO: Create in MyApplication
    private LoginApi loginApi;
    private NdbApi ndbApi;

    public RestClient() {
        Gson gson = new GsonBuilder().create();

        RestAdapter loginRestAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(gson))
                .setEndpoint("http://146.185.180.39:4020")
                .build();

        RestAdapter ndbRestAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(gson))
                .setEndpoint("http://api.nal.usda.gov")
                .build();

        loginApi = loginRestAdapter.create(LoginApi.class);
        ndbApi = ndbRestAdapter.create(NdbApi.class);
    }

    public LoginApi getLoginApi() { return loginApi; }

    public NdbApi getNdbApi() { return ndbApi; }
}
