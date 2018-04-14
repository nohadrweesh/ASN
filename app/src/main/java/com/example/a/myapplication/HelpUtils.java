package com.example.a.myapplication;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

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

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Speed on 13/04/2018.
 */

public class HelpUtils {
    private static final String TAG = "HelpUtils";
    private static Context mContext;
    private static HelpUtils mInstance;

    private HelpUtils(Context context) {
        mContext = context;
    }

    public static HelpUtils getInstance(Context context) {
        if (mInstance == null)
            mInstance = new HelpUtils(context);
        return mInstance;
    }

    public void help(final String type, final String message, final String location) {

        Log.d(TAG, "help: starts");


        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_PROBLEM,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: starts with response " + response);
                        //tx.setText(response);

                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.d(TAG, "onResponse: " + response);
                            if (!obj.getBoolean("error")) {
                                Log.d(TAG, "onResponse: pb sent");

                            } else {
                                Log.d(TAG, "onResponse: error " + obj.getString("message"));

                            }


                        } catch (JSONException e) {
                            Log.d(TAG, "onResponse: error" + response);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: starts error " + error.toString());

                    }

                }
        ) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                LocationManager locationManager = (LocationManager) mContext
                        .getSystemService(LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    Log.d(TAG, "getParams: Accept permissions ");
                }
                Location location1 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);


                final String currentTime = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
                Log.d(TAG, "getParams: starts with " + currentTime);
                Map<String, String> params = new HashMap<>();
                params.put("type", type);
                params.put("time", currentTime);
                params.put("location", location);
                params.put("message", message);
                params.put("driverID", String.valueOf(SharedPrefManager.getInstance(mContext).getUserId()));
                params.put("carID", String.valueOf(SharedPrefManager.getInstance(mContext).getCarId()));
                params.put("latitude",String.valueOf(location1.getLatitude()));
                params.put("longitude",String.valueOf(location1.getLongitude()));
                params.put("altitude",String.valueOf(location1.getAltitude()));



                return params;
            }

        };
        RequestHandler.getInstance(mContext).addToRequestQueue(stringRequest);

    }


    public void sendHelpTo(final int needingHelpID,final int needingHelpCarID){
        Log.d(TAG, "sendHelpTo: starts");

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_SEND_HELP_TO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: starts with response " + response);
                        //tx.setText(response);

                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.d(TAG, "onResponse: " + response);
                            if (!obj.getBoolean("error")) {
                                Log.d(TAG, "onResponse: sentHelp sent");

                            } else {
                                Log.d(TAG, "onResponse: error " + obj.getString("message"));

                            }


                        } catch (JSONException e) {
                            Log.d(TAG, "onResponse: error" + response);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: starts error " + error.toString());

                    }

                }
        ) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                LocationManager locationManager = (LocationManager) mContext
                        .getSystemService(LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    Log.d(TAG, "getParams: Accept permissions ");
                }
                Location location1 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);


                final String currentTime = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
                Log.d(TAG, "getParams: starts with " + currentTime);
                Map<String, String> params = new HashMap<>();

                params.put("time", currentTime);

                params.put("driverID", String.valueOf(SharedPrefManager.getInstance(mContext).getUserId()));
                params.put("carID", String.valueOf(SharedPrefManager.getInstance(mContext).getCarId()));
                params.put("latitude",String.valueOf(location1.getLatitude()));
                params.put("longitude",String.valueOf(location1.getLongitude()));
                params.put("altitude",String.valueOf(location1.getAltitude()));
                params.put("to",String.valueOf(needingHelpID));
                params.put("toCar",String.valueOf(needingHelpCarID));



                return params;
            }

        };
        RequestHandler.getInstance(mContext).addToRequestQueue(stringRequest);

    }

}
