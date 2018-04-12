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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {


    private TextView textViewUsername,textViewLongitude,textViewLatitude,textViewAltitude,
                        textViewUserId,textViewUserEmail,textViewCarId,textViewNeighbours;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private ProgressDialog progressDialog;

    private static final String TAG = "ProfileActivity";

    String username;
    String userEmail;
    int userID;
    int carID;
    Driver driver;
    List<Car> neighbourCars=new ArrayList<>();
    private static LocationObject driverPosition =new LocationObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, SignIn.class));
        }

        LocationManipulating locationManipulating=new LocationManipulating(getApplicationContext());


        driverPosition =locationManipulating.getLocation();
        //TODO:- fix this username returns email ....userEmail returns password
        username =SharedPrefManager.getInstance(this).getUsername();
        userEmail =SharedPrefManager.getInstance(this).getUserEmail();
        userID =SharedPrefManager.getInstance(this).getUserId();
        carID =SharedPrefManager.getInstance(this).getCarId();
        driver=new Driver(userID,username,userEmail,carID);

        Log.d("ProfileActivity ","username "+driver.getDriverName()+" user email "+driver.getDriverEmail()+" user id "+driver.getDriverID());
        Log.d("ProfileActivity ","carid "+driver.getDriverID());

        textViewUsername = (TextView) findViewById(R.id.tv_username);
        textViewUserEmail = (TextView) findViewById(R.id.tv_useremail);


        textViewLongitude=(TextView)findViewById(R.id.longitude_tv);
        textViewLatitude=(TextView)findViewById(R.id.latitude_tv);
        textViewAltitude=(TextView)findViewById(R.id.altitude_tv);
        textViewNeighbours=(TextView)findViewById(R.id.tv_neigbours);
        textViewUserId=(TextView)findViewById(R.id.tv_userID);
        textViewCarId=(TextView)findViewById(R.id.tv_carID);


        textViewUserEmail.setText(userEmail);
        textViewUsername.setText(username);
        textViewUserId.setText(String.valueOf(userID));
        textViewCarId.setText(String.valueOf(carID));
        textViewLongitude.setText(String.valueOf(driverPosition.getLongitude()));
        textViewLatitude.setText(String.valueOf(driverPosition.getLatitude()));
        textViewAltitude.setText(String.valueOf(driverPosition.getAltitude()));

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

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

    public void AddLocation(View view)
    {
        Log.d("ProfileActivity: "," add location clicked user ID "+userID+" username "+username+" user email "+userEmail+ " car ID "+carID+
                " longitude "+driverPosition.getLongitude()+" latitude "+driverPosition.getLatitude()+
                " altitude "+driverPosition.getAltitude());
        setLocation(driverPosition);
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
                        try
                        {
                            JSONObject obj = new JSONObject(response);
                            Log.d(TAG, "onResponse: "+response);
                            if(!obj.getBoolean("error"))
                                {Toast.makeText(getApplicationContext(),"location set ",Toast.LENGTH_LONG).show();}
                            else
                                {Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();}
                        } catch (JSONException e)
                        {
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
                        Toast.makeText(getApplicationContext(), "unknown error  error is  "+error.toString(), Toast.LENGTH_LONG).show();
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
//        userID=SharedPrefManager.getInstance(this).getUserId();
//        int carID=SharedPrefManager.getInstance(this).getCarId();
        LocationManipulating locationManipulating=new LocationManipulating(getApplicationContext());
        LocationObject currentLocation=locationManipulating.getLocation();
        setLocation(currentLocation);
        Log.d("ProfileActivity:"," get neighbours clicked");
        getNeighboursFromDb(userID,currentLocation);
        Log.d("ProfileActivity:"," get neighbours from database finished");
    }

    public List<Car> getNeighboursFromDb(final int userID, final LocationObject curr)
    {
//        progressDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_NEIGBOURS,
                //TODO: there are no neighbours retrieved with error msg= {"error":true,"message":"Required fields are missing"}
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                            {neighbourCars=getNeighboursFromJSON(response);}
                        catch (JSONException e)
                            {e.printStackTrace();}
                        Log.d(TAG, "onResponse: starts with response "+response);
//                        textViewNeighbours.setText(response);

//                        progressDialog.dismiss();
                        try
                        {
                            JSONObject obj = new JSONObject(response);
                            //retJSON=new JSONObject(response);
                            Log.d(TAG, "onResponse: "+response);
                            if(!obj.getBoolean("error"))
                                {
//                                    Toast.makeText(getApplicationContext(),"Retreived neighbours are  "+response,Toast.LENGTH_LONG).show();
                                }
                            else
                                {
//                                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                                }
                        }
                        catch (JSONException e)
                        {
                            Log.d(TAG, "onResponse: error"+response);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: starts");
//                        progressDialog.dismiss();
//                        Toast.makeText(getApplicationContext(), "unknown error  error is  "+error.toString(), Toast.LENGTH_LONG).show();
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
                //params.put("carID", String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getCarId()));
                params.put("time", timeStamp);
                return params;
            }

        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        return neighbourCars;
    }

    public List<Car> getNeighboursFromJSON(String response) throws JSONException {
        List <Car> nc=new ArrayList<>();
        JSONObject responseObject= new JSONObject(response);
        JSONObject result = (JSONObject) responseObject.get("result");

        JSONArray neighbourArray= result.getJSONArray("users");

        for (int i=0;i<neighbourArray.length();i++)
        {
            JSONObject neighbour=neighbourArray.getJSONObject(i);
            double latitude  =neighbour.getDouble("latitude");
            double longitude =neighbour.getDouble("longitude");
            double altitude  =neighbour.getDouble("altitude");
            LocationObject neighbourPosition=new LocationObject(longitude,latitude,altitude);
//            int neighbourCarID=neighbour.getInt("");
            int neighbourDriverID=neighbour.getInt("driverID") ;
            String neighbourName = neighbour.getString("driverName");
            Car neighbourCar = new Car(neighbourPosition,neighbourName,neighbourDriverID);
            nc.add(neighbourCar);
        }
        return nc;
    }

    public void gotoGoogleMaps(View view)
    {
        Intent i=new Intent(ProfileActivity.this,MapsActivity.class);
        i.putExtra("driver", (Serializable) driver);
        startActivity(i);
    }
}
