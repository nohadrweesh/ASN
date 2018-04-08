package com.example.a.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.FusedLocationProviderClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {


    private TextView textViewUsername,textViewLongitude,textViewLatitude,textViewAltitude,
                        textViewUserId,textViewCarId,textViewNeighbours;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static LocationObject retObject=new LocationObject();

    private ProgressDialog progressDialog;

    private static final String TAG = "ProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, SignIn.class));
        }

        textViewUsername = (TextView) findViewById(R.id.textViewUsername);
        //textViewUserEmail = (TextView) findViewById(R.id.textViewUseremail);



        //textViewUserEmail.setText(SharedPrefManager.getInstance(this).getUserEmail());
        textViewUsername.setText(SharedPrefManager.getInstance(this).getUsername());


        textViewLongitude=(TextView)findViewById(R.id.longitude_tv);
        textViewLatitude=(TextView)findViewById(R.id.latitude_tv);
        textViewAltitude=(TextView)findViewById(R.id.altitude_tv);

        textViewNeighbours=(TextView)findViewById(R.id.tv_neigbours);

        textViewUserId=(TextView)findViewById(R.id.tv_userID);
        textViewCarId=(TextView)findViewById(R.id.tv_carID);
        textViewUserId.setText(String.valueOf(SharedPrefManager.getInstance(this).getUserId()));
        textViewCarId.setText(String.valueOf(SharedPrefManager.getInstance(this).getCarId()));

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");



       LocationManipulating locationManipulating=new LocationManipulating(getApplicationContext());
         retObject=locationManipulating.getLocation();


        textViewLongitude.setText(String.valueOf(retObject.getLongitude()));
        textViewLatitude.setText(String.valueOf(retObject.getLatitude()));
        textViewAltitude.setText(String.valueOf(retObject.getAltitude()));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("Profile", "onCreateOptionsMenu: starts");
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menuLogout:
                SharedPrefManager.getInstance(this).logout();
                finish();
                startActivity(new Intent(this, SignIn.class));
                break;
            case R.id.menuSettings:
                Toast.makeText(this, "You clicked settings", Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }
    public void AddLocation(View view){
        setLocation(retObject);
    }
    
    private void setLocation(final LocationObject locationObject){
        final double latitude=locationObject.getLatitude();
        final double longitude=locationObject.getLongitude();
        final double altitude=locationObject.getAltitude();


        progressDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.LOCATION_SET,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: starts with response "+response);
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.d(TAG, "onResponse: "+response);
                            if(!obj.getBoolean("error")){
                                Toast.makeText(getApplicationContext(),"location set ",Toast.LENGTH_LONG).show();
                                //finish();
                            }else{
                                Toast.makeText(
                                        getApplicationContext(),
                                        obj.getString("message"),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        } catch (JSONException e) {
                            Log.d(TAG, "onResponse: error"+response);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: starts");
                        progressDialog.dismiss();

                        Toast.makeText(
                                getApplicationContext(),
                                "unknown error  error is  "+error.toString(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
                Log.d(TAG, "getParams: starts with "+locationObject.toString());
                Log.d(TAG, "getParams: starts with time "+timeStamp);

                Map<String, String> params = new HashMap<>();
                params.put("latitude", String.valueOf(latitude));
                params.put("longitude", String.valueOf(longitude));
                params.put("altitude", String.valueOf(altitude));
                params.put("userID", String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getUserId()));
                params.put("carID", String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getCarId()));
                params.put("locationTime", timeStamp);

                return params;
            }

        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }


    public void getNeighbours(View view){
        int userID=SharedPrefManager.getInstance(this).getUserId();
        //int carID=SharedPrefManager.getInstance(this).getCarId();
        LocationManipulating locationManipulating=new LocationManipulating(getApplicationContext());
        LocationObject currentLocation=locationManipulating.getLocation();
        setLocation(currentLocation);
        getNeighboursFromDb(currentLocation);
    }
    public void getNeighboursFromDb( final LocationObject curr){
        progressDialog.show();
        //final JSONObject retJSON;

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_NEIGBOURS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: starts with response "+response);
                        textViewNeighbours.setText(response);

                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            //retJSON=new JSONObject(response);
                            Log.d(TAG, "onResponse: "+response);
                            if(!obj.getBoolean("error")){
                                Toast.makeText(getApplicationContext(),"Retreived neighbours are  "+response,Toast.LENGTH_LONG).show();

                               // finish();
                            }else{
                                Toast.makeText(
                                        getApplicationContext(),
                                        obj.getString("message"),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        } catch (JSONException e) {
                            Log.d(TAG, "onResponse: error"+response);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: starts");
                        progressDialog.dismiss();

                        Toast.makeText(
                                getApplicationContext(),
                                "unknown error  error is  "+error.toString(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
                Log.d(TAG, "getParams: starts with "+curr.toString());
                Log.d(TAG, "getParams: starts with time "+timeStamp);

                Map<String, String> params = new HashMap<>();
                params.put("latitude", String.valueOf(curr.getLatitude()));
                params.put("longitude", String.valueOf(curr.getLongitude()));
                params.put("altitude", String.valueOf(curr.getAltitude()));
                params.put("userID", String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getUserId()));
                params.put("time", timeStamp);
                //params.put("carID", String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getCarId()));
                //params.put("locationTime", timeStamp);

                return params;
            }

        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
}
