package com.example.a.myapplication;

import java.io.Serializable;

/**
 * Created by user on 05/04/2018.
 */

public class Driver implements Serializable {
    private int ID;
    private String name;
    private String email;
    private int carID;

    public Driver(int id, String name, String email,int carId)
    {
        this.ID = id;
        this.name = name;
        this.email = email;
        this.carID = carId;
    }

    public Driver() {
    }

    public int getDriverID() {return ID;}
    public String getDriverName() {return name;}
    public String getDriverEmail() {return email;}
    public int getCarID(){return carID;}

    public void setDriverID(int driverID) {this.ID = driverID;}
    public void setDriverName(String driverName) {this.name = driverName;}
    public void setDriverEmail(String driverEmail) {this.email = driverEmail;}
}

