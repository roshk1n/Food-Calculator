package com.example.roshk1n.foodcalculator.rest.model.ndbApi.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.roshk1n.foodcalculator.realm.NutrientRealm;

public class Nutrient  implements Parcelable {
    private String nutrient_id;
    private String nutrient;
    private String unit;
    private String gm;

    public Nutrient() { }

    public Nutrient(NutrientRealm nutrientRealm) {
        setGm(nutrientRealm.getValue());
        setNutrient(nutrientRealm.getNutrient());
        setNutrient_id(nutrientRealm.getNutrient_id());
        setUnit(nutrientRealm.getUnit());
    }

    private Nutrient(Parcel in) {
        nutrient_id = in.readString();
        nutrient = in.readString();
        unit = in.readString();
        gm = in.readString();
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

    public String getNutrient() { return nutrient; }

    public void setNutrient(String nutrient) { this.nutrient = nutrient; }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getGm() {
        return gm;
    }

    public void setGm(String gm) {
        this.gm = gm;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nutrient_id);
        dest.writeString(nutrient);
        dest.writeString(unit);
        dest.writeString(gm);
    }
}
