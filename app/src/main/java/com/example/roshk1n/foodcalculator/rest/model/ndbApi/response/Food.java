package com.example.roshk1n.foodcalculator.rest.model.ndbApi.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by roshk1n on 7/18/2016.
 */
public class Food implements Parcelable {
    private String ndbno;
    private String name;
    private float weight;
    private String measure;
    private ArrayList<Nutrient> nutrients = new ArrayList<Nutrient>();

    public Food() {}

    protected Food(Parcel in) {
        ndbno = in.readString();
        name = in.readString();
        weight = in.readFloat();
        measure = in.readString();
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

    public float getWeight() { return weight; }

    public void setWeight(float weight) { this.weight = weight; }

    public String getMeasure() { return measure; }

    public void setMeasure(String measure) { this.measure = measure; }

    public ArrayList<Nutrient> getNutrients() {
        return nutrients;
    }

    public void setNutrients(ArrayList<Nutrient> nutrients) {
        this.nutrients = nutrients;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ndbno);
        dest.writeString(name);
        dest.writeFloat(weight);
        dest.writeString(measure);
        dest.writeTypedList(nutrients);
    }
}
