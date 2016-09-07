package com.example.roshk1n.foodcalculator.realmModel;

import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Nutrient;

import io.realm.RealmList;
import io.realm.RealmObject;

public class FoodRealm extends RealmObject {

    private String ndbno;
    private String name;
    private long time;
    private int portion = 1;
    private RealmList<NutrientRealm> nutrients = new RealmList<>();

    public FoodRealm() {
    }

    public FoodRealm(String ndbno, String name) {
        this.ndbno = ndbno;
        this.name = name;
    }

    public FoodRealm(Food food) {
        setNdbno(food.getNdbno());
        setName(food.getName());
        setTime(food.getTime());
        setPortion(food.getPortion());
        for (Nutrient nutrient : food.getNutrients()) {
            nutrients.add(new NutrientRealm(nutrient));
        }
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public RealmList<NutrientRealm> getNutrients() {
        return nutrients;
    }

    public void setNutrients(RealmList<NutrientRealm> nutrients) {
        this.nutrients = nutrients;
    }

    public int getPortion() {
        return portion;
    }

    public void setPortion(int portion) {
        this.portion = portion;
    }

    public boolean isExistIn(RealmList<FoodRealm> foods) {
        boolean check = false;
        for (int i = 0; i < foods.size(); i++) {
            if(this.getNdbno().equals(foods.get(i).getNdbno())) {
                check= true;
                break;
            }
        }
        return check;
    }
}
