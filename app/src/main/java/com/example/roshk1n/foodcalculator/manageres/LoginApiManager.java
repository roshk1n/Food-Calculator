package com.example.roshk1n.foodcalculator.manageres;

import com.example.roshk1n.foodcalculator.rest.RestClient;
import com.example.roshk1n.foodcalculator.rest.model.loginApi.ActivateUser;
import com.example.roshk1n.foodcalculator.rest.model.loginApi.LoginUser;
import com.example.roshk1n.foodcalculator.rest.model.loginApi.LogoutUser;
import com.example.roshk1n.foodcalculator.rest.model.loginApi.VerifyUser;
import com.example.roshk1n.foodcalculator.rest.model.loginApi.response.ActivateResponse;
import com.example.roshk1n.foodcalculator.rest.model.loginApi.response.LoginResponse;
import com.example.roshk1n.foodcalculator.rest.model.loginApi.response.LogoutResponse;
import com.example.roshk1n.foodcalculator.rest.model.loginApi.response.RegistrationResponse;
import com.example.roshk1n.foodcalculator.rest.model.loginApi.RegistrationUser;
import com.example.roshk1n.foodcalculator.rest.model.loginApi.response.VerifyResponse;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public  class LoginApiManager {
    private static RestClient restClient;

    public LoginApiManager() {}

    public static void registerUser(String f_namem, String l_name, String u_email, String u_password) {
        RegistrationUser registrationUser = new RegistrationUser(f_namem,l_name,u_email,u_password);

        restClient.getLoginService().registrationUser(registrationUser, new Callback<RegistrationResponse>() {
            @Override
            public void success(RegistrationResponse registrationResponse, Response response) {
                registrationResponse.getSucceeded();
                registrationResponse.getDataRegistration();
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }
    public static void verifyUser(String email) {
        VerifyUser verifyUser = new VerifyUser(email);

        restClient.getLoginService().verifyUser(verifyUser, new Callback<VerifyResponse>() {
            @Override
            public void success(VerifyResponse verifyResponse, Response response) {
                verifyResponse.getData();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public static void activationUser(String code,String email) {
        ActivateUser activateUser = new ActivateUser(code,email);

        restClient.getLoginService().activationUser(activateUser, new Callback<ActivateResponse>() {
            @Override
            public void success(ActivateResponse activateResponse, Response response) {
                activateResponse.getData();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public static void login(String email,String password) {
        LoginUser loginUser = new LoginUser(email,password);

        restClient.getLoginService().login(loginUser, new Callback<LoginResponse>() {
            @Override
            public void success(LoginResponse loginResponse, Response response) {
                loginResponse.getData();
            }
            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public static void logout(String email,String access_token) {
        LogoutUser logoutUser = new LogoutUser(email,access_token);

        restClient.getLoginService().logout(logoutUser, new Callback<LogoutResponse>() {
            @Override
            public void success(LogoutResponse logoutResponse, Response response) {
                logoutResponse.getData();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}