package com.example.roshk1n.foodcalculator.rest.model.loginApi.response;

public class VerifyResponse {
    private Succeeded succeeded;
    private DataVerify data;

    public VerifyResponse() {}

    public Succeeded getSucceeded() {
        return succeeded;
    }

    public void setSucceeded(Succeeded succeeded) {
        this.succeeded = succeeded;
    }

    public DataVerify getData() {
        return data;
    }

    public void setData(DataVerify data) {
        this.data = data;
    }
}
