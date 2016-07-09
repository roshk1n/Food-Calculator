package com.example.roshk1n.foodcalculator;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;

import android.net.http.RequestQueue;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


public class LoginActivity extends Activity {

    private static String TAG = "MyLog";
    Firebase firebase;
    LoginButton btnLogInFacebook;
    Button btnLogIn;
    TextView info;
    EditText etLogin;
    EditText etPassword;
    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;


    ProfilePictureView profilePictureView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);
        btnLogInFacebook = (LoginButton) findViewById(R.id.btnLogInFacebook);
        btnLogIn = (Button)  findViewById(R.id.btnLogin);
        etLogin = (EditText) findViewById(R.id.etLogin);
        etPassword = (EditText) findViewById(R.id.etPassword);
        info = (TextView) findViewById(R.id.info);

        mAuth = FirebaseAuth.getInstance(); // перевірка статусу логіну
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user !=null){
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                }
                else
                {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
        btnLogInFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() { //login via facebook
            @Override
            public void onSuccess(LoginResult loginResult) {
  /*              info.setText(
                        "User ID: "
                                + loginResult.getAccessToken().getUserId()
                                + "\n"
                );*/

                startActivity(new Intent(LoginActivity.this,MainActivity.class));

                // profilePictureView.setProfileId(loginResult.getAccessToken().getUserId());
            }
            @Override
            public void onCancel() {
                info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException error) {
                info.setText("Login attempt failed.");
            }
        });

     //   profilePictureView = (ProfilePictureView) findViewById(R.id.ProfilePhotoFac);


       /* Firebase.setAndroidContext(this);
        firebase = new Firebase("https://food-calculator.firebaseio.com/");
        firebase.child("condition").setValue("Do you have data? You'll love Firebase.");*/

    }

    public void onStop()
    {
        super.onStop();
        if (mAuthListner != null) {
            mAuth.removeAuthStateListener(mAuthListner);
        }
    }
    public void onStart()
    {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        callbackManager.onActivityResult(requestCode,resultCode,data);

    }
    public void onGoSingInActivityClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), SingUpActivity.class);
        startActivity(intent); //перехід на SingUpActivity
    }
    public void onLogIn(View view) // кнопка логіну користувача через email/password
    {
       // Log.d(TAG,"{\nv_code:a7f370f6411aa7a21ac4d9c84ab196841f1cff86,\nu_email:\"roshk1n.ua@gmail.com\"");
        if(etLogin.getText().toString().equals("")||etPassword.getText().toString().equals(""))
        {
            Toast.makeText(LoginActivity.this, "Enter all the fields.",Toast.LENGTH_SHORT).show();
        }
        else
        {
        mAuth.signInWithEmailAndPassword(etLogin.getText().toString(),etPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed. Try again please!",Toast.LENGTH_SHORT).show();
                        }
                    }

                });
        }


    }


//-------------------------------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------------------
// API API API



    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }
    public static String POST(String url) {
        String result = "";
        String json = "";
        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            HttpPut httpPut = new HttpPut(url);
/*
        реєстрація
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("f_name", "Oleh");
            jsonObject.accumulate("l_name", "Roshka");
            jsonObject.accumulate("u_email", "roshk1n.ua@gmail.com");
            jsonObject.accumulate("u_password", "132132132");
            jsonObject.accumulate("role", 0);
            json = jsonObject.toString();

        отримання підтвердження
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("u_email", "roshk1n.ua@gmail.com");
            json = jsonObject.toString();

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            JSONObject jsonObject = new JSONObject();

            jsonObject.accumulate("v_code", "a7f370f6411aa7a21ac4d9c84ab196841f1cff86");
            jsonObject.accumulate("u_email", "roshk1n.ua@gmail.com");
            json = jsonObject.toString();

            StringEntity se = new StringEntity(json);
            httpPost.setEntity(se);

            HttpResponse httpResponse = httpclient.execute(httpPost);

            InputStream inputStream = httpResponse.getEntity().getContent();

            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

            */
            httpPut.setHeader("Accept", "application/json");
            httpPut.setHeader("Content-type", "application/json");

            JSONObject jsonObject = new JSONObject();

            jsonObject.accumulate("v_code", "a7f370f6411aa7a21ac4d9c84ab196841f1cff86");
            jsonObject.accumulate("u_email", "roshk1n.ua@gmail.com");
            json = jsonObject.toString();

            StringEntity se = new StringEntity(json);
            httpPut.setEntity(se);


            HttpResponse httpResponse = httpclient.execute(httpPut);

            InputStream inputStream = httpResponse.getEntity().getContent();

            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
                @Override
        protected String doInBackground(String... strings) {
            return POST(strings[0]);
        }

        
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Data Sent!" + result, Toast.LENGTH_LONG).show();
            Log.d(TAG,result);
        }
    }
    public void  onLogInApi(View view) {
        new HttpAsyncTask().execute("http://146.185.180.39:4020/users/activate");


    /*    User user = new User("11","Vova","vova@gmail.com","132132132","https://firebasestorage.googleapis.com/v0/b/food-calculator.appspot.com/o/images%2Fprofle_default.png?alt=media&token=812c2e4f-45e0-4c41-bbaf-a5b94e1b95c7");
        user.saveUser();*/
       // Firebase ref = new Firebase("https://food-calculator.firebaseio.com/users/");

    }

}
