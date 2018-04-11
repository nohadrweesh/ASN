package com.example.a.myapplication;

import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import static com.google.android.gms.wearable.DataMap.TAG;

/**
 * Created by Speed on 22/03/2018.
 */

class LocationObject  {
    private double mLongitude;
    private double mLatitude;
    private double mAltitude;


    public LocationObject() {}
    public LocationObject(double longitude, double latitude, double altitude)
    {
        mLongitude = longitude;
        mLatitude = latitude;
        mAltitude = altitude;
    }


    public void setLongitude(double longitude) {mLongitude = longitude;}
    public void setLatitude(double latitude) {mLatitude = latitude;}
    public void setAltitude(double altitude) {
        mAltitude = altitude;
    }


    public double getLongitude() {
        return mLongitude;
    }
    public double getLatitude() {
        return mLatitude;
    }
    public double getAltitude() {
        return mAltitude;
    }


    @Override
    public String toString() {return "location: "+mLongitude+" mlong & "+mLatitude+" mLat & "+mAltitude+" mAlt";}


}