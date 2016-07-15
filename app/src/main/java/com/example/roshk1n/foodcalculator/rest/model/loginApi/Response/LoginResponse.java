package com.example.roshk1n.foodcalculator.rest.model.loginApi.response;

/**
 * Created by roshk1n on 7/15/2016.
 */
public class LoginResponse {
    private Succeeded succeeded;
    private DataLogin data;

    public LoginResponse() {
    }

    public Succeeded getSucceeded() {

        return succeeded;
    }

    public void setSucceeded(Succeeded succeeded) {
        this.succeeded = succeeded;
    }

    public DataLogin getData() {
        return data;
    }

    public void setData(DataLogin data) {
        this.data = data;
    }
}
