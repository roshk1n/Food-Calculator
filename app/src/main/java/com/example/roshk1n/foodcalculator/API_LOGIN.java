package com.example.roshk1n.foodcalculator;

import java.util.Map;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;

/**
 * Created by roshk1n on 7/10/2016.
 */
public interface API_LOGIN {

    @Headers({
            "Accept: application/json",
            "User-Agent: Retrofit-Sample-App",
            "Content-type: application/json"
    })
    @POST("/registration")
    Response registrationUser(@Body Map<String, String> parameters);


    @Headers({
            "Accept: application/json",
            "User-Agent: Retrofit-Sample-App",
            "Content-type: application/json"
    })
    @POST("/users/verify")
    Response verifyUser(@Body Map<String, String> parameters);


    @Headers({
            "Accept: application/json",
            "User-Agent: Retrofit-Sample-App",
            "Content-type: application/json"
    })
    @POST("/login/email")
    Response login(@Body Map<String, String> parameters);


    @Headers({
            "Accept: application/json",
            "User-Agent: Retrofit-Sample-App",
            "Content-type: application/json"
    })
    @POST("/login/logout")
    Response logout(@Body Map<String, String> parameters);


    @Headers({
            "Accept: application/json",
            "User-Agent: Retrofit-Sample-App",
            "Content-type: application/json"
    })
    @PUT("/users/activate")
    Response activationUser(@Body Map<String, String> parameters);


}
