package com.example.roshk1n.foodcalculator.presenters;

import android.util.Log;

import com.example.roshk1n.foodcalculator.MyApplication;
import com.example.roshk1n.foodcalculator.Views.InfoFoodView;
import com.example.roshk1n.foodcalculator.rest.RestClient;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.NutrientBasicFoodResponse;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by roshk1n on 8/11/2016.
 */
public class InfoFoodPresenterImpl implements InfoFoodPresenter {

    private InfoFoodView infoFoodView;

    @Override
    public void setView(InfoFoodView view) {
        this.infoFoodView = view;
    }

    @Override
    public void getNutrientsBasic(String ndbno, String type) {
        final RestClient restClient = MyApplication.getRestClient();
        restClient.getNdbApi().getBasicNutrientsFood(restClient.getApi_key(), ndbno, type, new Callback<NutrientBasicFoodResponse>() {
            @Override
            public void success(NutrientBasicFoodResponse nutrientBasicFoodResponse, Response response) {
                Log.d("Myyy",nutrientBasicFoodResponse.getReport().getFood().getNutrients().get(0).getName());
                infoFoodView.addNutrients(nutrientBasicFoodResponse.getReport().getFood().getNutrients());
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }
}
