package com.example.roshk1n.foodcalculator.realm;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by roshk1n on 7/21/2016.
 */
public class FoodRealm extends RealmObject {
    private String ndbno;
    private String name;
    private Date date;

    private RealmList<NutrientRealm> nutrients = new RealmList<NutrientRealm>();

    public FoodRealm() {
    }

    public FoodRealm(String ndbno, String name) {
        this.ndbno = ndbno;
        this.name = name;
    }

    public String getNdbno() {
        return ndbno;
    }

    public void setNdbno(String ndbno) {
        this.ndbno = ndbno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<NutrientRealm> getNutrients() {
        return nutrients;
    }

    public void setNutrients(RealmList<NutrientRealm> nutrients) {
        this.nutrients = nutrients;
    }
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
