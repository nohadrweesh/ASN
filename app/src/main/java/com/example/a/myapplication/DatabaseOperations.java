package com.example.a.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.android.gms.wearable.DataMap.TAG;

/**
 * Created by user on 08/04/2018.
 */

public class DatabaseOperations {

    Context ctx;
    public void addLocationToDB(Context ctx, final LocationObject locationObject, final int carID, final int driverID)
    {
        Log.d(" addLocationToDB: ",locationObject.toString());
        final double latitude=locationObject.getLatitude();
        final double longitude=locationObject.getLongitude();
        final double altitude=locationObject.getAltitude();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.LOCATION_SET,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: starts with response "+response);
                        try
                        {
                            JSONObject obj = new JSONObject(response);
                            Log.d(TAG, "onResponse: "+response);
                            if(!obj.getBoolean("error"))
                            {}
                            else
                            {}
                        } catch (JSONException e)
                        {
                            Log.d(TAG, "onResponse: error"+response);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {Log.d(TAG, "onErrorResponse: starts");}
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
                params.put("userID", String.valueOf(driverID));
                params.put("carID", String.valueOf(carID));
                params.put("locationTime", timeStamp);

                return params;
            }
        };
        Log.d("DatabaseOperations: ",stringRequest.toString());
        RequestHandler.getInstance(ctx).addToRequestQueue(stringRequest);
    }


    public List<Car> getNeighboursFromDb(final int userID, final LocationObject curr)
    {
        List<Car> neighbourCars =new ArrayList<>();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_NEIGBOURS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        //TODO: handle response when neighbours are retrieved
//                        try
//                            {neighbourCars=getNeighboursFromJSON(response);}
//                        catch (JSONException e)
//                            {e.printStackTrace();}
                        Log.d(ContentValues.TAG, "onResponse: starts with response "+response);

                        try
                        {
                            JSONObject obj = new JSONObject(response);
                            //retJSON=new JSONObject(response);
                            Log.d(ContentValues.TAG, "onResponse: "+response);
                            if(!obj.getBoolean("error"))
                            {}
                            else
                            {}
                        }
                        catch (JSONException e)
                        {
                            Log.d(ContentValues.TAG, "onResponse: error"+response);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(ContentValues.TAG, "onErrorResponse: starts");
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
                Log.d(ContentValues.TAG, "getParams: starts with "+curr.toString());
                Log.d(ContentValues.TAG, "getParams: starts with time "+timeStamp);

                Map<String, String> params = new HashMap<>();
                params.put("latitude", String.valueOf(curr.getLatitude()));
                params.put("longitude", String.valueOf(curr.getLongitude()));
                params.put("altitude", String.valueOf(curr.getAltitude()));
                params.put("userID", String.valueOf(SharedPrefManager.getUserId()));
                //params.put("carID", String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getCarId()));
                //params.put("locationTime", timeStamp);
                return params;
            }
        };
        RequestHandler.getInstance(ctx).addToRequestQueue(stringRequest);

        return neighbourCars;
    }

    public List<Car> getNeighboursFromJSON(String response) throws JSONException
    {
        List <Car> nc=new ArrayList<>();
        JSONArray neighbourArray= new JSONArray(response);
        for (int i=0;i<neighbourArray.length();i++)
        {
            JSONObject neighbour=neighbourArray.getJSONObject(i);
            double latitude  =neighbour.getDouble("");
            double longitude =neighbour.getDouble("");
            double altitude  =neighbour.getDouble("");
            LocationObject neighbourPosition=new LocationObject(longitude,latitude,altitude);
            int neighbourCarID=neighbour.getInt("");
            int neighbourDriverID=neighbour.getInt("") ;
            Car neighbourCar=new Car(neighbourPosition,neighbourCarID,neighbourDriverID);
            nc.add(neighbourCar);
        }
        return nc;
    }
}
