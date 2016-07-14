package com.example.roshk1n.foodcalculator.rest.model;

/**
 *  Created by roshk1n on 7/14/2016.
 */
public class RegistrationResponse {

//    TODO: objects should be separated not inner classes.
    private  Succeeded succeeded;
    private  Data data;

    public RegistrationResponse() {}

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Succeeded getSucceeded() {
        return succeeded;
    }

    public void setSucceeded(Succeeded succeeded) {
        this.succeeded = succeeded;
    }
}