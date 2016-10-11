package com.example.matth.finalapp.notification;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by matth on 10/10/2016.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String REG_TOKEN = "REG_TOKEN";
    private String token;

    @Override
    public void onTokenRefresh() {

        token = FirebaseInstanceId.getInstance().getToken();
        Log.d(REG_TOKEN, token);
        //registerToken(recent_token);
    }

    public String getToken() {
        return token;
    }
}
