package com.example.roshk1n.foodcalculator.rest.model.ndbApi.response;

import android.os.Parcel;
import android.os.Parcelable;
import com.example.roshk1n.foodcalculator.realm.FoodRealm;
import com.example.roshk1n.foodcalculator.realm.NutrientRealm;
import java.util.ArrayList;

public class Food  implements Parcelable {
    private String ndbno;
    private String name;
    private int portion = 1;
    private long time;
    private ArrayList<Nutrient> nutrients = new ArrayList<Nutrient>();

    public Food() {}

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
}
