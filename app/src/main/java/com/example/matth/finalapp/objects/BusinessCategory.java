package com.example.matth.finalapp.objects;

/**
 * Created by michael on 24/10/2016.
 */

public class BusinessCategory {

    private int id;
    private String name;
    private boolean tables;
    private boolean reservations;
    private boolean personnel;
    private boolean kitchen;

    public BusinessCategory(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isTables() {
        return tables;
    }

    public void setTables(boolean tables) {
        this.tables = tables;
    }

    public boolean isReservations() {
        return reservations;
    }

    public void setReservations(boolean reservations) {
        this.reservations = reservations;
    }

    public boolean isPersonnel() {
        return personnel;
    }

    public void setPersonnel(boolean personnel) {
        this.personnel = personnel;
    }

    public boolean isKitchen() {
        return kitchen;
    }

    public void setKitchen(boolean kitchen) {
        this.kitchen = kitchen;
    }

}
