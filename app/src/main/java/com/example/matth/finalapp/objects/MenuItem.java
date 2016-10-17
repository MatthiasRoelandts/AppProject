package com.example.matth.finalapp.objects;

import java.sql.Time;

/**
 * Created by matth on 16/10/2016.
 */

public class MenuItem {

    private String title;
    private String description;
    private double price;
    private Integer btw;
    private Time avrtime;
    private int itemCategoryId;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getBtw() {
        return btw;
    }

    public void setBtw(Integer btw) {
        this.btw = btw;
    }

    public Time getAvrtime() {
        return avrtime;
    }

    public void setAvrtime(Time avrtime) {
        this.avrtime = avrtime;
    }

    public int getItemCategoryId() {
        return itemCategoryId;
    }

    public void setItemCategoryId(int itemCategoryId) {
        this.itemCategoryId = itemCategoryId;
    }
}
