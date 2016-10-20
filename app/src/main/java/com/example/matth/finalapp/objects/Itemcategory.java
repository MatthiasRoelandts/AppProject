package com.example.matth.finalapp.objects;

/**
 * Created by matth on 16/10/2016.
 */

public class Itemcategory {

    private int id;
    private String name;
    private int restaurant_id;

    public int getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(int restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
