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
    private long date;
    private ArrayList<Nutrient> nutrients = new ArrayList<Nutrient>();

    public Food() {}

    private Food(Parcel in) {
        ndbno = in.readString();
        name = in.readString();
        portion = in.readInt();
        date = in.readLong();
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getPortion() {
        return portion;
    }

    public void setPortion(int portion) {
        this.portion = portion;
    }

    public FoodRealm converToRealm() {
        FoodRealm foodRealm = new FoodRealm();
        foodRealm.setName(this.getName());
        foodRealm.setNdbno(this.getNdbno());
        foodRealm.setPortion(this.getPortion());
        for(int i=0;i<this.getNutrients().size();i++)
        {
            NutrientRealm nutrientRealm = new NutrientRealm();
            nutrientRealm.setNutrient_id(this.getNutrients().get(i).getNutrient_id());
            nutrientRealm.setNutrient(this.getNutrients().get(i).getNutrient());
            nutrientRealm.setValue(this.getNutrients().get(i).getValue());
            nutrientRealm.setUnit(this.getNutrients().get(i).getUnit());
            foodRealm.getNutrients().add(nutrientRealm);
        }
        foodRealm.setTime(this.getDate());
        return foodRealm;
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
        dest.writeLong(date);
        dest.writeTypedList(nutrients);
    }
}
