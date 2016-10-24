package com.example.matth.finalapp.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.sql.Time;

/**
 * Created by michael on 20/10/2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Personnel extends User {

    public Personnel(String email,String password){super(email,password);}

    public Personnel(){}


    private double hourly_salary;
    private Time hours_worked;
    private String job_description;
    private String address;
    private String city;
    private Integer postal;
    private int restaurant_id;


    public double getHourly_salary() {
        return hourly_salary;
    }

    public void setHourly_salary(double hourly_salary) {
        this.hourly_salary = hourly_salary;
    }

    public Time getHours_worked() {
        return hours_worked;
    }

    public void setHours_worked(Time hours_worked) {
        this.hours_worked = hours_worked;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getPostal() {
        return postal;
    }

    public void setPostal(Integer postal) {
        this.postal = postal;
    }

    public int getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(int restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getJob_description() { return job_description; }

    public void setJob_description(String job_description) { this.job_description = job_description;}
}
