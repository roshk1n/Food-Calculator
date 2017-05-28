package com.example.roshk1n.foodcalculator.rest.model.loginApi.response;

/**
 *  Created by roshk1n on 7/14/2016.
 */
public class RegistrationResponse {
    private  Succeeded succeeded;
    private DataRegistration dataRegistration;

    public RegistrationResponse() {}

    public DataRegistration getDataRegistration() {
        return dataRegistration;
    }

    public void setDataRegistration(DataRegistration dataRegistration) {
        this.dataRegistration = dataRegistration;
    }

    public Succeeded getSucceeded() {
        return succeeded;
    }

    public void setSucceeded(Succeeded succeeded) {
        this.succeeded = succeeded;
    }
}