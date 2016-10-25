package com.example.matth.finalapp.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

/**
 * Created by michael on 14/10/2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Business {

    private int id;
    private String name;
    private String info;
    private String address;
    private String city;
    private Integer postal;
    private Integer rating;
    private BigDecimal revenue;
    private String ownerEmail;

    private boolean tables;
    private boolean reservations;
    private boolean personnel;
    private boolean kitchen;

    public Business(String name, String address, String city, Integer postal) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.postal = postal;
    }

    public Business() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
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

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }


    public boolean isKitchen() {
        return kitchen;
    }

    public void setKitchen(boolean kitchen) {
        this.kitchen = kitchen;
    }

    public boolean isPersonnel() {
        return personnel;
    }

    public void setPersonnel(boolean personnel) {
        this.personnel = personnel;
    }

    public boolean isReservations() {
        return reservations;
    }

    public void setReservations(boolean reservations) {
        this.reservations = reservations;
    }

    public boolean isTables() {
        return tables;
    }

    public void setTables(boolean tables) {
        this.tables = tables;
    }


    @Override
    public String toString() {
        return this.name;
    }

}
