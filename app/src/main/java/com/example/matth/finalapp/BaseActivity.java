package com.example.matth.finalapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.matth.finalapp.objects.Business;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.google.gson.Gson;

/**
 * Created by michael on 11/10/2016.
 */
public class BaseActivity extends AppCompatActivity {


    private SharedPreferences preferences;
    private String authToken;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private RestCommon restCommon;

    //this object holds the user information this can be an owner or personel
    private Object user;

    /*
    * TODO check if the token has expired or not by making a request to the  server otherwise return to the login page
    * */

    @Override
    protected void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);
        System.out.println("Activity created !!!");
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        restCommon = new RestCommon(getAuthToken());
    }

    public void setUserEmail(String email){
        editor.putString("userEmail",email);
        editor.apply();
    }

    public String getUserEmail(){
        return preferences.getString("userEmail","");
    }

    public void setBusinessId(int id){
        System.out.println("The id of the restaurant is " + id);
        editor.putInt("businessId",id);
        editor.commit();
    }


    public int getBusinessId(){ return preferences.getInt("businessId",-1); }

    public void setBusiness(Business business){
        Gson gson = new Gson();
        String json = gson.toJson(business);
        editor.putString("currentBusiness",json);
        editor.commit();
    }

    public Business getBusiness(){

        Gson gson = new Gson();
        String json  = preferences.getString("currentBusiness","");
        Business business = gson.fromJson(json,Business.class);
        return business;
    }

    public String getAuthToken() {

        return preferences.getString("authToken", "");
    }


    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }


    /*
    * Save the token in the shared preferences when the user is authenticated
    *
    * */
    public void saveToken(String authToken) {

        editor.putString("authToken", authToken);
        editor.apply();
    }
    /*
    * When the user logs out the auth token needs to be removed
    *
    * */
    public void removeToken() {

        editor.putString("authToken", "");
        editor.apply();
    }

    public void makeSnackbar(View view,CharSequence msg){
        Snackbar snackbar = Snackbar
                .make(view,msg,Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public void makeToast(CharSequence text){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public RestCommon getRestCommon() {
        return restCommon;
    }

    public void setRestCommon(RestCommon restCommon) {
        this.restCommon = restCommon;
    }


    //Functions for login and registration
    public boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    public boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

}
