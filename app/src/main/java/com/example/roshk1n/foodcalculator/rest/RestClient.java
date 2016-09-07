package com.example.roshk1n.foodcalculator.rest;


import com.example.roshk1n.foodcalculator.rest.services.LoginService;
import com.example.roshk1n.foodcalculator.rest.services.NdbService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class RestClient {
    private LoginService loginService;
    private NdbService ndbService;
    private final String api_key = "MmHcNZ8WUfr29ekyImQB7zPfDJSeX3Qnvi7KDcTJ";

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

        loginService = loginRestAdapter.create(LoginService.class);
        ndbService = ndbRestAdapter.create(NdbService.class);
    }

    public String getApi_key() {
        return api_key;
    }

    public LoginService getLoginService() { return loginService; }

    public NdbService getNdbService() { return ndbService; }
}
