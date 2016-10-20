package com.example.matth.finalapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

/*
* BaseActivty also displays the splashscreen
*
*
* */
public class SplashActivity extends BaseActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final Activity splashActivity = this;


        /* New Handler to start the Menu/Login-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                Intent mainIntent;
                /* Create an Intent that will start the Menu-Activity or Login-Activity based on login status of the user. */
                if(getLoggedin() == false){
                    System.out.println("The user is NOT logged in");
                    mainIntent = new Intent(splashActivity,LoginActivity.class);

                }else{
                    System.out.println("The user is logged in");
                    /**/
                    mainIntent = new Intent(splashActivity,MenuActivity.class);

                }
                splashActivity.startActivity(mainIntent);
                splashActivity.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);


    }
}
