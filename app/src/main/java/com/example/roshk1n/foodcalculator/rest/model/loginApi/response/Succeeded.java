package com.example.roshk1n.foodcalculator.rest.model.loginApi.response;

/**
 *  Created by Mykhailo Nester on 7/14/16.
 */
class Succeeded {

    private Boolean seccess;
    private String message;

    public Succeeded() {}

    public Boolean isSeccess() {
        return seccess;
    }

    public void setSeccess(Boolean seccess) {
        this.seccess = seccess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
