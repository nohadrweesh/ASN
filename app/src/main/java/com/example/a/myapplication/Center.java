package com.example.a.myapplication;

/**
 * Created by a on 27/06/2018.
 */

public class Center {
    private String name,location;
    private String id;
    public Center(String id, String name, String location){
        this.id=id;
        this.name=name;
        this.location=location;
    }
    public void setId(String id){
        this.id=id;
    }
    public void setName(String name){
        this.name=name;
    }
    public void setLocation(String location){
        this.location=location;
    }
    public String getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getLocation(){
        return location;
    }
}
