package com.example.matth.finalapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.fasterxml.jackson.databind.deser.Deserializers;

/**
 * Created by michael on 11/10/2016.
 */
public class BaseActivity extends AppCompatActivity{


    private SharedPreferences preferences;
    private String authToken;

    /*
    * TODO check if the token has expired or not by making a request to the  server otherwise return to the login page
    * */

   public boolean getLoggedin() {

       SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getBoolean("isLoggedin",false);
   }

    public void setLoggedin(boolean loggedin) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        System.out.println("The value set for logged in is" + loggedin);
        editor.putBoolean("isLoggedin",loggedin);
        editor.apply();
        System.out.println("The user is logged in ? " +getLoggedin() );

    }

    public String getAuthToken() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getString("authToken","");
    }


    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }


    //Functions for login and registration
    public boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic add extra code here
        return email.contains("@");
    }

    public boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

}
