package com.example.roshk1n.foodcalculator.rest.model.ndbApi.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.roshk1n.foodcalculator.realm.NutrientRealm;

import java.io.Serializable;

public class Nutrient  implements Parcelable, Serializable {
    private String nutrient_id;
    private String name;
    private String unit;
    private String value;

    public Nutrient() { }

    public Nutrient(NutrientRealm nutrientRealm) {
        setValue(nutrientRealm.getValue());
        setName(nutrientRealm.getName());
        setNutrient_id(nutrientRealm.getNutrient_id());
        setUnit(nutrientRealm.getUnit());
    }

    private Nutrient(Parcel in) {
        nutrient_id = in.readString();
        name = in.readString();
        unit = in.readString();
        value = in.readString();
    }

    public static final Creator<Nutrient> CREATOR = new Creator<Nutrient>() {
        @Override
        public Nutrient createFromParcel(Parcel in) {
            return new Nutrient(in);
        }

        @Override
        public Nutrient[] newArray(int size) {
            return new Nutrient[size];
        }
    };

    public String getNutrient_id() {
        return nutrient_id;
    }

    public void setNutrient_id(String nutrient_id) {
        this.nutrient_id = nutrient_id;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nutrient_id);
        dest.writeString(name);
        dest.writeString(unit);
        dest.writeString(value);
    }
}
