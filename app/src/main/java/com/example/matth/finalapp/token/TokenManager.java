package com.example.matth.finalapp.token;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.example.matth.finalapp.BaseActivity;
import com.example.matth.finalapp.LoginActivity;
import com.example.matth.finalapp.MenuActivity;
import com.example.matth.finalapp.objects.User;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by michael on 7/10/2016.
 */
public class TokenManager extends BaseActivity {



    public void getToken(final User user, LoginActivity reference) {


        new AsyncTask<Object, Void, Object[]>() {

            LoginActivity loginActivity;
            Object[] responseArray = new Object[3];

            @Override
            protected Object[] doInBackground(Object... params) {
                User user = (User) params[0];
                loginActivity = (LoginActivity) params[1];

                Boolean success = false;
                String error = "";
                String token = "";

                //auth returns the token for the client
                final String url = "http://10.0.2.2:8080/auth/login";
                RestTemplate restTemplate = new RestTemplate();
                // Add the Jackson and String message converters
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                HttpEntity<User> request = new HttpEntity<User>(user);

                try {
                    ResponseEntity<JwtAuthenticationToken> response = restTemplate.exchange(url, HttpMethod.POST, request, JwtAuthenticationToken.class);
                    System.out.println("The status code is " + response.getStatusCode() + "The body is " + response.getBody() + "The headers are " + response.getHeaders());
                    success = true;
                    token = response.getBody().getToken();

                } catch (HttpClientErrorException errorException) {
                    if (errorException.getStatusCode() == HttpStatus.BAD_REQUEST) {

                        error = "Username and password combination not found !";
                        System.out.println(error);
                    }
                } catch (ResourceAccessException e) {
                    error = "Unable to access server";
                    System.out.println(error);
                }

                responseArray[0] = success;
                responseArray[1] = error;
                responseArray[2] = token;

                return responseArray;
            }

            @Override
            protected void onPostExecute(Object[] result) {

                //login successfull
                if ((Boolean) result[0] == true) {
                    loginActivity.saveToken((String) result[2]);
                    loginActivity.setUserEmail(user.getEmail());
                    loginActivity.setUserId(user.getId());
                    //TODO redirect to homepage
                    Intent intent = new Intent(loginActivity, MenuActivity.class);
                    intent.putExtra("info", "Welcome to homepage");
                    loginActivity.startActivity(intent);
                    loginActivity.showProgress(false);
                    loginActivity.finish();

                } else {
                    loginActivity.showProgress(false);
                    loginActivity.makeToast((CharSequence) result[1]);
                }
            }
        }.execute(user, reference);
    }
}
