package com.example.roshk1n.foodcalculator.rest.model.loginApi.response;

public class LoginResponse {
    private Succeeded succeeded;
    private com.example.roshk1n.foodcalculator.rest.model.loginApi.response.DataLogin data;

    public LoginResponse() {
    }

    public Succeeded getSucceeded() {

        return succeeded;
    }

    public void setSucceeded(Succeeded succeeded) {
        this.succeeded = succeeded;
    }

    public com.example.roshk1n.foodcalculator.rest.model.loginApi.response.DataLogin getData() {
        return data;
    }

    public void setData(com.example.roshk1n.foodcalculator.rest.model.loginApi.response.DataLogin data) {
        this.data = data;
    }
}
