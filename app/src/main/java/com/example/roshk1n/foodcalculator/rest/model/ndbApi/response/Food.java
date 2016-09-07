package com.example.roshk1n.foodcalculator.rest.model.ndbApi.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.roshk1n.foodcalculator.realmModel.FoodRealm;
import com.example.roshk1n.foodcalculator.realmModel.NutrientRealm;
import com.example.roshk1n.foodcalculator.remoteDB.model.FoodFirebase;
import com.example.roshk1n.foodcalculator.remoteDB.model.NutrientFirebase;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Food implements Parcelable, Serializable {
    private String ndbno;
    private String name;
    private int portion = 1;
    private long time;
    private ArrayList<Nutrient> nutrients = new ArrayList<>();

    public Food() {
    }

    public Food(FoodRealm foodRealm) {
        setName(foodRealm.getName());
        setNdbno(foodRealm.getNdbno());
        setTime(foodRealm.getTime());
        setPortion(foodRealm.getPortion());
        ArrayList<Nutrient> nutrient = new ArrayList<>();
        for (NutrientRealm nutrientRealm : foodRealm.getNutrients()) {
            nutrient.add(new Nutrient(nutrientRealm));
        }
        setNutrients(nutrient);
    }

    public Food(FoodFirebase foodFirebase) {
        setName(foodFirebase.getName());
        setNdbno(foodFirebase.getNdbno());
        setTime(foodFirebase.getTime());
        setPortion((int)(long)foodFirebase.getPortion());
        ArrayList<Nutrient> nutrient = new ArrayList<>();
        for (NutrientFirebase nutrientFirebase : foodFirebase.getNutrients()) {
            nutrient.add(new Nutrient(nutrientFirebase));
        }
        setNutrients(nutrient);
    }

    private Food(Parcel in) {
        ndbno = in.readString();
        name = in.readString();
        portion = in.readInt();
        time = in.readLong();
        nutrients = in.createTypedArrayList(Nutrient.CREATOR);
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };

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

    public ArrayList<Nutrient> getNutrients() {
        return nutrients;
    }

    public void setNutrients(ArrayList<Nutrient> nutrients) {
        this.nutrients = nutrients;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getPortion() {
        return portion;
    }

    public void setPortion(int portion) {
        this.portion = portion;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ndbno);
        dest.writeString(name);
        dest.writeInt(portion);
        dest.writeLong(time);
        dest.writeTypedList(nutrients);
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        HashMap<String, String> nutrientMap = new HashMap<>();
        HashMap<String, Object> nutrient = new HashMap<>();
        result.put("ndbno", ndbno);
        result.put("name", name);
        result.put("portion", portion);
        result.put("time", time);

        for (Nutrient nut : nutrients) {
            nutrientMap.put("nutrient_id", nut.getNutrient_id());
            nutrientMap.put("name", nut.getName());
            nutrientMap.put("value", nut.getValue());
            nutrientMap.put("unit", nut.getUnit());
            nutrient.put(nut.getNutrient_id(),nutrientMap);
        }

        result.put("nutrients", nutrient);

        return result;
    }
}
