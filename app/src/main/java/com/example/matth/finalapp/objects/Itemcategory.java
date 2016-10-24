package com.example.matth.finalapp.objects;

/**
 * Created by matth on 16/10/2016.
 */

public class Itemcategory {

    private int id;
    private String name;
    private int restaurantId;

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurant_id) {
        this.restaurantId = restaurant_id;
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
