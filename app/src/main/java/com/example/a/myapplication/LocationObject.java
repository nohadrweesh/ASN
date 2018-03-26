package com.example.a.myapplication;

/**
 * Created by Speed on 22/03/2018.
 */

class LocationObject {
    private double mLongitude;
    private double mLatitude;
    private double mAltitude;

    public LocationObject() {
    }

    public void setLongitude(double longitude) {

        mLongitude = longitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public void setAltitude(double altitude) {
        mAltitude = altitude;
    }

    public LocationObject(double longitude, double latitude, double altitude) {
        mLongitude = longitude;
        mLatitude = latitude;
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
    public String toString() {
        return "location @ "+mLongitude+" mlong &@ "+mLatitude+" mLat &@ "+mAltitude+" mAlt";
    }
}