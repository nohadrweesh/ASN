package com.example.a.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

public class ProfileActivity extends AppCompatActivity {


    private TextView textViewUsername, textViewUserEmail,textViewLongitude,textViewLatitude,textViewAltitude;
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

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");



       // LocationManipulating locationManipulating=new LocationManipulating(getApplicationContext());
        //LocationObject locationObject=locationManipulating.getLocation();
        mFusedLocationProviderClient=new FusedLocationProviderClient(getApplicationContext());
        if(mFusedLocationProviderClient==null)
            Log.d("Profile", "getLocation: is null");
        else
            Log.d("Profile", "getLocation: not null");


        int hasLocationPermission= ContextCompat.checkSelfPermission(getApplicationContext(),ACCESS_COARSE_LOCATION);
        if(hasLocationPermission== PackageManager.PERMISSION_GRANTED){
            mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(  new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null) {
                        Toast.makeText(getApplicationContext(), "your location is set to "+location.toString(), Toast.LENGTH_LONG).show();

                        double latitude=location.getLatitude();
                        double longitude=location.getLongitude();
                        double altitude=location.getAltitude();
                        retObject.setLongitude(longitude);
                        retObject.setLatitude(latitude);
                        retObject.setAltitude(altitude);
                        Log.d("Profile", "addItem:before return place long @ "+retObject.getLongitude()+" lat @"+retObject.getLatitude());

                    }

                }
            });
            Log.d("Profile", "addItem:before else place long @ "+retObject.getLongitude()+" lat @"+retObject.getLatitude());

        }else{
            Toast.makeText(getApplicationContext(),"Accept Permission",Toast.LENGTH_LONG).show();

        }
        Log.d("Profile", "addItem:return place long @ "+retObject.getLongitude()+" lat @"+retObject.getLatitude());
        
        

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
                        Log.d(TAG, "onResponse: starts");
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.d(TAG, "onResponse: "+response);
                            if(!obj.getBoolean("error")){
                                Toast.makeText(getApplicationContext(),"location set ",Toast.LENGTH_LONG).show();
                                finish();
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
                Log.d(TAG, "getParams: starts with "+locationObject.toString());
                Map<String, String> params = new HashMap<>();
                params.put("latitude", String.valueOf(latitude));
                params.put("longitude", String.valueOf(longitude));
                params.put("altitude", String.valueOf(altitude));
                return params;
            }

        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
}
