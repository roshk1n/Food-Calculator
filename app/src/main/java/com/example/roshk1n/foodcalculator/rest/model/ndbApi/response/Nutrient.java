package com.example.roshk1n.foodcalculator.rest.model.ndbApi.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.roshk1n.foodcalculator.realmModel.NutrientRealm;
import com.example.roshk1n.foodcalculator.remoteDB.model.NutrientFirebase;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Nutrient implements Parcelable, Serializable {
    private String nutrient_id;
    private String name;
    private String nameEng;
    private String unit;
    private String value;

    public Nutrient() {
    }

    public Nutrient(String nutrient_id, String name, String nameEng, String unit, String value) {
        this.nutrient_id = nutrient_id;
        this.name = name;
        this.nameEng = nameEng;
        this.unit = unit;
        this.value = value;
    }

    public Nutrient(NutrientRealm nutrientRealm) {
        setValue(nutrientRealm.getValue());
        setName(nutrientRealm.getName());
        setNameEng(nutrientRealm.getNameEng());
        setNutrient_id(nutrientRealm.getNutrient_id());
        setUnit(nutrientRealm.getUnit());
    }

    public Nutrient(NutrientFirebase nutrientFirebase) {
        setValue(nutrientFirebase.getValue());
        setName(nutrientFirebase.getName());
        setNameEng(nutrientFirebase.getNameEng());
        setNutrient_id(nutrientFirebase.getNutrient_id());
        setUnit(nutrientFirebase.getUnit());
    }


    protected Nutrient(Parcel in) {
        nutrient_id = in.readString();
        name = in.readString();
        nameEng = in.readString();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getNameEng() {
        return nameEng;
    }

    public void setNameEng(String nameEng) {
        this.nameEng = nameEng;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nutrient_id);
        dest.writeString(name);
        dest.writeString(nameEng);
        dest.writeString(unit);
        dest.writeString(value);
    }
}
