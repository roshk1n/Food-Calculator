package com.example.roshk1n.foodcalculator.rest.service;

import com.example.roshk1n.foodcalculator.rest.model.ResponeRegister;
import com.example.roshk1n.foodcalculator.rest.model.User;

import java.util.Map;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.PUT;

public interface LoginApi {

    @POST("/registration")
    void registrationUser(@Body User user, Callback<ResponeRegister> Callback);

    @POST("/users/verify")
    Response verifyUser(@Body Map<String, String> parameters);

    @POST("/login/email")
    Response login(@Body Map<String, String> parameters);

    @POST("/login/logout")
    Response logout(@Body Map<String, String> parameters);

    @PUT("/users/activate")
    Response activationUser(@Body Map<String, String> parameters);

}
