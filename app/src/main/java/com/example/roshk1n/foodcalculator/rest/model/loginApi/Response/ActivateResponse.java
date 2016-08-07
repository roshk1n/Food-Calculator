package com.example.roshk1n.foodcalculator.rest.model.loginApi.response;

public class ActivateResponse {
    private Succeeded succeeded;
    private DataActivate data;

    public ActivateResponse() {}

    public Succeeded getSucceeded() {
        return succeeded;
    }

    public void setSucceeded(Succeeded succeeded) {
        this.succeeded = succeeded;
    }

    public DataActivate getData() {
        return data;
    }

    public void setData(DataActivate data) {
        this.data = data;
    }
}
