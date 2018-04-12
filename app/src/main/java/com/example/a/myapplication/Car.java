package com.example.a.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by user on 05/04/2018.
 */

public class Car {

    LocationObject location;
    private int carID;
    private int driverID;
    private String driverName;
    List<Car> neighbourCars=new ArrayList<>();


    public Car(int carID, int driverID)
    {
        this.carID = carID;
        this.driverID = driverID;
    }
    public Car(LocationObject pos, int cID, int dID)
    {
        this.location = pos;
        this.carID = cID;
        this.driverID = dID;
    }

    public Car(LocationObject neighbourPosition, String driverName, int neighbourDriverID)
    {
        this.location = neighbourPosition;
        this.driverName = driverName;
        this.driverID = neighbourDriverID;
    }

    public LocationObject getPosition() {return this.getPosition();}

    public int getCarID() {return carID;}
    public int getDriverID() {return driverID;}

    public void setCarID(int carID) {this.carID = carID;}
    public void setDriverID(int driverID) {this.driverID = driverID;}

    public void setLocation(Context ctx,LocationObject locationObject, Driver driver)
    {
        carID=this.getCarID();
        driverID=this.getDriverID();
        DatabaseOperations dbOperations = new DatabaseOperations();;
        location=locationObject;
        Log.d("Car: location is"," latitude "+location.getLatitude()+" longitude "+location.getLongitude()+" altitude "+location.getAltitude());
        dbOperations.addLocationToDB(ctx, location,carID,driverID);
    }

    public List<Car> getNeighbours(Context ctx,int driverID,LocationObject driverLocation)
    {
        DatabaseOperations dbOperations=new DatabaseOperations();
        Log.d("Car Class "," getNeighbours");
        neighbourCars = dbOperations.getNeighboursFromDb(ctx,driverID,driverLocation);
        return neighbourCars;
    }

}
