package com.example.matth.finalapp.token;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by michael on 9/10/2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class JwtAuthenticationToken {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}
