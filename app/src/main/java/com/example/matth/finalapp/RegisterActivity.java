package com.example.matth.finalapp;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Register User");
        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    public void register(View view){
        new AsyncTask<Void, Void, User>() {
            User user = new User();
            @Override
            protected void onPreExecute() {
                EditText userFistname = (EditText) findViewById(R.id.editText);
                EditText userLastname = (EditText) findViewById(R.id.editText2);
                EditText userEmail = (EditText) findViewById(R.id.editText3);
                EditText userPassword = (EditText) findViewById(R.id.editText4);
                user.setName(userFistname.getText().toString());
                user.setLastname(userLastname.getText().toString());
                user.setEmail(userEmail.getText().toString());
                user.setPassword(userPassword.getText().toString());
            }
            @Override
            protected User doInBackground(Void... params) {
                try {
                    final String url = "http://10.0.2.2:8080/registerUser";

                    RestTemplate restTemplate = new RestTemplate();
                    // Add the Jackson and String message converters
                    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                    restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                    // Make the HTTP POST request, marshaling the request to JSON, and the response to a String
                    User response = restTemplate.postForObject(url, user, User.class);
                    return response;

                } catch (Exception e) {
                    Log.e("MainActivity", e.getMessage(), e);
                }
                return null;
            }
        }.execute();
    }
}
