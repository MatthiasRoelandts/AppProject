package com.example.matth.finalapp.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by matth on 7/10/2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {


    private int id;
    private String name;
    private String lastname;
    private String email;
    private String phone;
    private String password;
    private String token;

    public User(String email, String password) {

        this.email = email;
        this.password = password;
    }

    public User(){}

    public int getId() {  return id; }

    public void setId(int id) {  this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
