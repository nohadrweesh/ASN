package com.example.a.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
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

public class Waiting extends AppCompatActivity {

    ////////////////////////////////////
    /////// YOMNA HESHAM 19 APRIL //////
    ////////////////////////////////////
    private static final String TAG = "Waiting";
    private static Context mContext;
    private static int problemID;
    ////////////////////////////////////
    ////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        ////////////////////////////////////
        /////// YOMNA HESHAM 19 APRIL //////
        ////////////////////////////////////
        Intent i = getIntent();
        problemID = i.getIntExtra("problemID", -1);

//        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
//        relativeLayout.startAnimation(animation);


        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_WAITING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: starts with response " + response);

                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.d(TAG, "onResponse: " + response);
                            if (!obj.getBoolean("error")) {
                                Log.d(TAG, "onResponse: respond is correct");

                                if(obj.getBoolean("acceptor_found")){
                                    Log.d(TAG, "onResponse: someone accepts <3");

                                    int acceptorID = obj.getJSONObject("acceptor_info").getInt("acceptorID");
                                    String username = obj.getJSONObject("acceptor_info").getString("usernamw");
                                    String phone = obj.getJSONObject("acceptor_info").getString("phone");
                                    JSONObject pos = obj.getJSONObject("acceptor_info").getJSONObject("position");
                                    double lng = pos.getDouble("lng");
                                    double lat = pos.getDouble("lat");
                                    double atit = pos.getDouble("atit");

                                    Intent i =new Intent(getApplicationContext(),AcceptorInfo.class);
                                    i.putExtra("acceptorID",acceptorID);
                                    i.putExtra("username"  ,username  );
                                    i.putExtra("phone"     ,phone     );
                                    i.putExtra("lng"       ,lng       );
                                    i.putExtra("lat"       ,lat       );
                                    i.putExtra("atit"      ,atit      );
                                    startActivity(i);
                                }
                                else{
                                    Log.d(TAG, "onResponse: NO ONE ACCEPTS :(");
                                }
                            }
                            else {
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
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                LocationManager locationManager = (LocationManager) mContext
                        .getSystemService(LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    Log.d(TAG, "getParams: Accept permissions ");
                }
                Location location1 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                final String currentTime = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
                Log.d(TAG, "getParams: starts with " + currentTime);
                Map<String, String> params = new HashMap<>();
                params.put("problemID", String.valueOf(problemID));

                return params;
            }

            ;
        };
        RequestHandler.getInstance(mContext).addToRequestQueue(stringRequest);

        ////////////////////////////////////
        ////////////////////////////////////
    }
}
