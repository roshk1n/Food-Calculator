package com.example.roshk1n.foodcalculator.rest.model.ndbApi.response;

/**
 * Created by roshk1n on 7/18/2016.
 */
public class Measure {
    private String label;
    private float eqv;
    private float qty;
    private String value;

    public Measure() {}

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public float getEqv() {
        return eqv;
    }

    public void setEqv(float eqv) {
        this.eqv = eqv;
    }

    public float getQty() {
        return qty;
    }

    public void setQty(float qty) {
        this.qty = qty;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
