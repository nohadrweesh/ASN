package com.example.a.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
        //TODO:- username returns email ....userEmail returns password
        username =SharedPrefManager.getInstance(this).getUsername();
        userEmail =SharedPrefManager.getInstance(this).getUserEmail();
        userID =SharedPrefManager.getInstance(this).getUserId();
        carID =SharedPrefManager.getInstance(this).getCarId();

        textViewUsername = (TextView) findViewById(R.id.tv_username);
        textViewUserEmail = (TextView) findViewById(R.id.tv_useremail);


        textViewLongitude=(TextView)findViewById(R.id.longitude_tv);
        textViewLatitude=(TextView)findViewById(R.id.latitude_tv);
        textViewAltitude=(TextView)findViewById(R.id.altitude_tv);
        textViewNeighbours=(TextView)findViewById(R.id.tv_neigbours);
        textViewUserId=(TextView)findViewById(R.id.tv_userID);
        textViewCarId=(TextView)findViewById(R.id.tv_carID);



        Log.d("ProfileActivity: ","user ID "+userID+" username "+username+" user email "+userEmail+ " car ID "+carID+
                                        " longitude "+driverPosition.getLongitude()+" latitude "+driverPosition.getLatitude()+
                                        " altitude "+driverPosition.getAltitude());
        textViewUserEmail.setText(SharedPrefManager.getInstance(this).getToken());
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
            case R.id.OBD:

                startActivity(new Intent(this,ObdActivity.class));

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
        Log.d(TAG, "setLocation: with "+locationObject.toString());

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

                            if(!obj.getBoolean("error")){
                                Toast.makeText(getApplicationContext(),"location set  ",Toast.LENGTH_LONG).show();
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

    public List<Car> getNeighbours()  //this function is called from MapsActivity
    {
        return neighbourCars;
    }

    public void getNeighbours(View view){
//        userID=SharedPrefManager.getInstance(this).getUserId();
//        int carID=SharedPrefManager.getInstance(this).getCarId();
        LocationManipulating locationManipulating=new LocationManipulating(getApplicationContext());
        LocationObject currentLocation=locationManipulating.getLocation();
        setLocation(currentLocation);

        getNeighboursFromDb(currentLocation);
    }
    public void getNeighboursFromDb( final LocationObject curr){
        progressDialog.show();
        //final JSONObject retJSON;

        Log.d("ProfileActivity:"," get neighbours clicked");
        getNeighboursFromDb(userID,curr);
        Log.d("ProfileActivity:"," get neighbours from database finished");
    }


    public void getNeighboursFromDb(final int userID, final LocationObject curr){
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_NEIGBOURS,
                //TODO: there are no neighbours retrieved with error msg= {"error":true,"message":"Required fields are missing"}
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.d(TAG, "onResponse: starts with response "+response);
                        textViewNeighbours.setText(response);

                        progressDialog.dismiss();
                        try
                        {
                            JSONObject obj = new JSONObject(response);
                            //retJSON=new JSONObject(response);
                            Log.d(TAG, "onResponse: "+response);
                            if(!obj.getBoolean("error"))
                                {Toast.makeText(getApplicationContext(),"Retreived neighbours are  "+response,Toast.LENGTH_LONG).show();}
                            else
                                {Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();}
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
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "unknown error  error is  "+error.toString(), Toast.LENGTH_LONG).show();
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

    public void gotoGoogleMaps(View view)
    {
        Intent i=new Intent(ProfileActivity.this,MapsActivity.class);
        i.putExtra("userID",userID);
        startActivity(i);
    }

    //HELP

    public void urgentHelp(View view){
        Log.d(TAG, "urgent help: starts");

        final String currentTime = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_PROBLEM,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: starts with response "+response);
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.d(TAG, "onResponse: "+response);
                            if(!obj.getBoolean("error")){
                                Toast.makeText(getApplicationContext(),"Help sent ",Toast.LENGTH_LONG).show();
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
                Log.d(TAG, "getParams: starts with "+currentTime);
                Map<String,String> params = new HashMap<>();
                params.put("time",currentTime );
                return params;
            }

        };


    }

    public void Help(View view){

        Log.d(TAG, "urgent help: starts");
        final String currentTime = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        EditText et=(EditText)findViewById(R.id.help_edittxt);

        //final TextView tx=(TextView) findViewById(R.id.error);
        final String helpType = et.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_PROBLEM,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: starts with response "+response);
                        //tx.setText(response);
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.d(TAG, "onResponse: "+response);
                            if(!obj.getBoolean("error")){
                                Toast.makeText(getApplicationContext(),"Help sent ",Toast.LENGTH_LONG).show();
                                finish();
                            }else{
                                Toast.makeText(
                                        getApplicationContext(),
                                        obj.getString("message"),
                                        Toast.LENGTH_LONG
                                ).show();

                            }
                            Intent i = new Intent(getApplicationContext(), Error.class);
                            i.putExtra("RESPONSE", response);
                            startActivity(i);
                            finish();

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
                Log.d(TAG, "getParams: starts with "+currentTime+" , "+helpType);
                Map<String,String> params = new HashMap<>();
                params.put("type",helpType );
                params.put("time",currentTime );
                params.put("location","Yassmin" );
                params.put("driverID", String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getUserId()));
                params.put("carID", String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getCarId()));
                Log.d(TAG, "getParams: driverID");

                return params;
            }

        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }
    public  void openDriverMap(View view){
        startActivity(new Intent(this,DriverMapActivity.class));
    }

    public void sendNotification(View view){
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.NOTIFICATION_URL,
                //TODO: there are no neighbours retrieved with error msg= {"error":true,"message":"Required fields are missing"}
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.d(TAG, "onResponse: starts with response "+response);
                        textViewNeighbours.setText(response);

                        progressDialog.dismiss();
                        try
                        {
                            JSONObject obj = new JSONObject(response);
                            //retJSON=new JSONObject(response);
                            Log.d(TAG, "onResponse: "+response);
                            if(!obj.getBoolean("error"))
                            {Toast.makeText(getApplicationContext(),"Retreived neighbours are  "+response,Toast.LENGTH_LONG).show();}
                            else
                            {Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();}
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
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "unknown error  error is  "+error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Log.d(TAG, "getParams: starts with  ");

                Map<String, String> params = new HashMap<>();

                params.put("userID", String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getUserId()));
                params.put("token", "ehywsxKpdEk:APA91bGpUNSy8StDgwRtGDkcrHfDst0iWqjlibr68_f0_e_ArkQHgE-9PEunMS9vYnD6KwWkXrai6om5eL8KrRVLivIawpy5uAlsDlRItQzMujn9lEGHjawivHR2XPknY-18SCrRaJVT");

                params.put("title", "title");
                params.put("message", "simple message");

                //params.put("carID", String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getCarId()));
                //params.put("locationTime", timeStamp);


                return params;
            }

        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    public void goToTrackersActivity(View v)
    {
        Intent i = new Intent(getApplicationContext(),TrackersListActivity.class);
        i.putExtra("currentUserID",userID);
        startActivity(i);
    }

}
