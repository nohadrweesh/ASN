package com.example.a.myapplication;

import java.io.Serializable;

/**
 * Created by user on 05/04/2018.
 */

public class Driver implements Serializable {
    private int ID;
    private String username;
    private String email;
    private String token;
    private String phonenumber;
    private String status;

    public Driver() {
    }

    public Driver(int ID, String email) {
        this.ID = ID;
        this.email = email;
    }

    public Driver(int ID, String username, String email, String token, String phonenumber, String status) {
        this.ID = ID;
        this.username = username;
        this.email = email;
        this.token = token;
        this.phonenumber = phonenumber;
        this.status = status;
    }

    public int getID() {return ID;}
    public String getDriverName() {return username;}
    public String getDriverEmail() {return email;}

    public void setID(int driverID) {this.ID = driverID;}
    public void setDriverName(String driverName) {this.username = driverName;}
    public void setDriverEmail(String driverEmail) {this.email = driverEmail;}

    @Override
    public String toString() {
        return "Driver{" +
                "ID=" + ID +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", token='" + token + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", status='" + status + '\'' +

                '}';
    }
}

