package com.example.a.myapplication;

/**
 * Created by user on 05/04/2018.
 */

public class Driver {
    private int ID;
    private String name;
    private String email;

    public Driver() {
    }

    public int getID() {return ID;}
    public String getDriverName() {return name;}
    public String getDriverEmail() {return email;}

    public void setID(int driverID) {this.ID = driverID;}
    public void setDriverName(String driverName) {this.name = driverName;}
    public void setDriverEmail(String driverEmail) {this.email = driverEmail;}
}

