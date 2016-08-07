package com.example.roshk1n.foodcalculator;

import com.example.roshk1n.foodcalculator.rest.RestClient;

public class ManageNdbApi {

    private static RestClient restClient;

    public ManageNdbApi() {}

    public static void searchFood(String format,String name,String max, final String api_key)
    {
       /*final ListInfoFoodResponse infoFoodResponses = new ListInfoFoodResponse();
        restClient = new RestClient();

        MyApplication.getRestClient().getNdbApi().searchFood(format,name, max,api_key, new Callback<ListFoodResponse>() {
            @Override
            public void success(final ListFoodResponse listFoodResponse, Response response) {

                for(int i =0;i<listFoodResponse.getList().getItem().size();i++)
                {

                    // It`s feature for this API I can`t receive information about food through name_food only through id_food
                    restClient.getNdbApi().searchNutrientFood(listFoodResponse.getList().getItem().get(i).getNdbno(), api_key, new Callback<NutrientFoodResponse>() {
                        @Override
                        public void success(NutrientFoodResponse nutrientFoodResponse, Response response) {
                            Log.d("Mssssy", nutrientFoodResponse.getReport().getFood().getNutrients().get(0).getName());
                            infoFoodResponses.getFoodResponses().add(nutrientFoodResponse);

                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });
                }
             //   EventBus.getDefault().post(infoFoodResponses);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });*/

    }

    private static void updateView() {

    }
}
