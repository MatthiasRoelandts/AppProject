package com.example.matth.finalapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.matth.finalapp.objects.Owner;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

public class RegisterActivity extends BaseActivity {

    private EditText rFirstNameView;
    private EditText rLastNameView;
    private EditText rEmailView;
    private EditText rPasswordView;
    private Activity registerActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Register");
        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        rFirstNameView = (EditText) findViewById(R.id.register_firstname);
        rLastNameView = (EditText) findViewById(R.id.register_lastname);
        rEmailView = (EditText) findViewById(R.id.register_email);
        rPasswordView = (EditText) findViewById(R.id.register_password);
        registerActivity = this;

    }

    public void attemptRegistration(View view){

        // Reset errors.
        rFirstNameView.setError(null);
        rLastNameView.setError(null);
        rEmailView.setError(null);
        rPasswordView.setError(null);

        // Store values at the time of the registration attempt.
        String firstname = rFirstNameView.getText().toString();
        String lastname = rLastNameView.getText().toString();
        String email = rEmailView.getText().toString();
        String password = rPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            rPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = rPasswordView;
            cancel = true;
        }else if(TextUtils.isEmpty(password)){

            rPasswordView.setError(getString(R.string.error_field_required));
            focusView = rPasswordView;
            cancel = true;

        }


        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            rEmailView.setError(getString(R.string.error_field_required));
            focusView = rEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            rEmailView.setError(getString(R.string.error_invalid_email));
            focusView = rEmailView;
            cancel = true;
        }

        //Check if the user entered a firstname and last name

        if(TextUtils.isEmpty(lastname)){
            rLastNameView.setError(getString(R.string.error_field_required));
            focusView = rLastNameView;
            cancel = true;
        }
        if(TextUtils.isEmpty(firstname)){
            rFirstNameView.setError(getString(R.string.error_field_required));
            focusView = rFirstNameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt registration and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            register();
        }
    }

    public void register(){
        new AsyncTask<Void, Void, HttpStatus>() {
            Owner owner;
            @Override
            protected void onPreExecute() {

                owner = new Owner(rEmailView.getText().toString(),rPasswordView.getText().toString());
                owner.setName(rFirstNameView.getText().toString());
                owner.setLastname(rLastNameView.getText().toString());
            }
            @Override
            protected HttpStatus doInBackground(Void... params) {

                    final String url = "http://10.0.2.2:8080/auth/register/owner";
                    HttpStatus status = null;
                    RestTemplate restTemplate = new RestTemplate();
                    // Add the Jackson and String message converters
                    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                    restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                    // Make the HTTP POST request, marshaling the request to JSON, and the response to a String
                    //User response = restTemplate.postForObject(url, owner, Owner.class);
                    ResponseEntity response = null;
                    try {
                        HttpEntity<Owner> request = new HttpEntity(owner);
                        response = restTemplate.exchange(url, HttpMethod.POST, request, Owner.class);
                        status = response.getStatusCode();

                    } catch (HttpClientErrorException e){
                       if(e.getStatusCode() == HttpStatus.CONFLICT){
                           status = HttpStatus.CONFLICT;
                       }
                    }catch(ResourceAccessException e){
                        status = HttpStatus.BAD_REQUEST;
                    }

                    return status;

            }

            @Override
            protected void onPostExecute(HttpStatus status){
                if(status == HttpStatus.ACCEPTED){
                    Intent intent = new Intent(registerActivity,LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("registration_successfull",true);
                    startActivity(intent);
                    finish();
                }
                if(status == HttpStatus.CONFLICT){
                    System.out.println("Very bad conflict");
                    rFirstNameView.setError(null);
                    rLastNameView.setError(null);
                    rEmailView.setError(null);
                    rPasswordView.setError(null);
                    rEmailView.setError(getString(R.string.error_duplicate_email));
                }
            }
        }.execute();
    }
}
