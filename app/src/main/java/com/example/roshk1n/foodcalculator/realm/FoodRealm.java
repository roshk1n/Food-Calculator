package com.example.roshk1n.foodcalculator.realm;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by roshk1n on 7/21/2016.
 */
public class FoodRealm extends RealmObject {

    private String ndbno;
    private String name;
    private Date time;
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public RealmList<NutrientRealm> getNutrients() {
        return nutrients;
    }

    public void setNutrients(RealmList<NutrientRealm> nutrients) {
        this.nutrients = nutrients;
    }
}
