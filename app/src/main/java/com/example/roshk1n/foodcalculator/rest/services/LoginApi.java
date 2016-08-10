package com.example.roshk1n.foodcalculator.rest.services;

import com.example.roshk1n.foodcalculator.rest.model.loginApi.ActivateUser;
import com.example.roshk1n.foodcalculator.rest.model.loginApi.LogoutUser;
import com.example.roshk1n.foodcalculator.rest.model.loginApi.response.ActivateResponse;
import com.example.roshk1n.foodcalculator.rest.model.loginApi.response.LoginResponse;
import com.example.roshk1n.foodcalculator.rest.model.loginApi.LoginUser;
import com.example.roshk1n.foodcalculator.rest.model.loginApi.response.LogoutResponse;
import com.example.roshk1n.foodcalculator.rest.model.loginApi.response.RegistrationResponse;
import com.example.roshk1n.foodcalculator.rest.model.loginApi.RegistrationUser;
import com.example.roshk1n.foodcalculator.rest.model.loginApi.response.VerifyResponse;
import com.example.roshk1n.foodcalculator.rest.model.loginApi.VerifyUser;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.PUT;

public interface LoginApi {

    @POST("/registration")
    void registrationUser(@Body RegistrationUser registrationUser, Callback<RegistrationResponse> Callback);

    @POST("/users/verify")
    void verifyUser(@Body VerifyUser verifyUser, Callback<VerifyResponse> Callback);

    @POST("/login/email")
    void login(@Body LoginUser loginUser, Callback<LoginResponse> Callback);

    @POST("/login/logout")
    void logout(@Body LogoutUser logoutUserm, Callback<LogoutResponse> Callback);

    @PUT("/users/activate")
    void activationUser(@Body ActivateUser activateUser, Callback<ActivateResponse> Callback);

}
