package com.example.a.myapplication.OBD.obdConnection;

/**
 * Created by Ahmed on 3/15/2018.
 */


/**
 * Defines several constants used between {@link obdBlutoothManager} and the UI.
 */
public interface Constants {

    // Message types sent from the obdBlutoothManager Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the obdBlutoothManager Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

}