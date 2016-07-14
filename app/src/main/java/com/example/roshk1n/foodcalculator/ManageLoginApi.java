package com.example.roshk1n.foodcalculator;

import android.util.Log;

import com.example.roshk1n.foodcalculator.rest.RestClient;
import com.example.roshk1n.foodcalculator.rest.model.RegistrationResponse;
import com.example.roshk1n.foodcalculator.rest.model.User;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by roshk1n on 7/10/2016.
 */
public  class ManageLoginApi {

    public static void registerUser(String f_namem, String l_name, String u_email, String u_password) {
        User user = new User(f_namem,l_name,u_email,u_password);
        RestClient restClient = new RestClient();

        restClient.getLoginApi().registrationUser(user, new Callback<RegistrationResponse>() {
            @Override
            public void success(RegistrationResponse registrationResponse, Response response) {
//                TODO: add code
                registrationResponse.getSucceeded();
//                or
                registrationResponse.getData();


                Log.d("TAG", "success");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("TAG", "fail");
            }
        });
    }


/*    private static RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint("http://146.185.180.39:4020")
            .build();
    private static LoginApi service = restAdapter.create(LoginApi.class);*/


 /*       Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("f_name", first_name);
        parameters.put("l_name", last_name);
        parameters.put("u_email", email);
        parameters.put("u_password", password);
        parameters.put("role", role);
*/
//        Response response  = service.registrationUser(parameters);
//        return  stringFromResponse(response);
//    }
/*
    public static String verifyUser(String email) {

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("u_email", email);

        Response response  = service.verifyUser(parameters);
        return  stringFromResponse(response);
    }

    public static String activationUser(String code,String email) {

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("v_code",code);
        parameters.put("u_email", email);

        Response response  = service.activationUser(parameters);
        return  stringFromResponse(response);
    }


    public static String login(String email,String password) {

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("u_email",email);
        parameters.put("u_password", password);

        Response response  = service.login(parameters);
        return  stringFromResponse(response);
    }

    public static String logout(String email,String access_token) {

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("u_email",email);
        parameters.put("a_token", access_token);

        Response response  = service.logout(parameters);
        return  stringFromResponse(response);
    }
    private static String stringFromResponse(Response response){
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();

        try {
            reader = new BufferedReader(new InputStreamReader(
                    response.getBody().in()));
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = sb.toString();
        return result;
    }*/

}