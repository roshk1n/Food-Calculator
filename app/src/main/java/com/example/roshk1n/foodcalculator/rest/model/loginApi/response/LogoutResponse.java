package com.example.roshk1n.foodcalculator.rest.model.loginApi.response;

public class LogoutResponse {
    private Succeeded succeeded;
    private DataLogout data;

    public LogoutResponse() {}

    public Succeeded getSucceeded() {
        return succeeded;
    }

    public void setSucceeded(Succeeded succeeded) {
        this.succeeded = succeeded;
    }

    public DataLogout getData() {
        return data;
    }

    public void setData(DataLogout data) {
        this.data = data;
    }
}
