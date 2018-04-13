package com.example.a.myapplication;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Speed on 13/04/2018.
 */

public class NeighborsUtils {
    private static final String TAG = "NeighborsUtils";
    private static Context mContext;
    private static NeighborsUtils mInstance;

    public List<Location>locations;
    public List<Integer>IDs;
    private NeighborsUtils(Context context){
        mContext=context;
        locations=new ArrayList<Location>();
        IDs=new ArrayList<Integer>();
    }
    public static NeighborsUtils getInstance(Context context){
        Log.d(TAG, "getInstance: starts");
        if(mInstance==null)
            mInstance=new NeighborsUtils(context);
        return mInstance;
    }
    public void getNeighbours(final  Location curr){

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_NEIGBOURS,
                //TODO: there are no neighbours retrieved with error msg= {"error":true,"message":"Required fields are missing"}
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {

                        try
                        {
                            JSONObject obj = new JSONObject(response);
                            //retJSON=new JSONObject(response);
                            Log.d(TAG, "onResponse: "+response);
                            if(!obj.getBoolean("error"))
                            {
                                //Toast.makeText(getApplicationContext(),"Retreived neighbours are  "+response,Toast.LENGTH_LONG).show();

                                Log.d(TAG, "onResponse: "+response);
                                //got neighbours
                                JSONObject result=obj.getJSONObject("result");
                                JSONObject users=result.getJSONObject("users");
                                Log.d(TAG, "onResponse: users "+users.toString());

                                parseUsersJson(users);
                            }

                                else
                            {
                                //Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                                Log.d(TAG, "onResponse: error "+obj.getString("message"));
                                }
                        }
                        catch (JSONException e)
                        {
                            Log.d(TAG, "onResponse: error"+e.getMessage());
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: starts "+error.toString());

                        //Toast.makeText(getApplicationContext(), "unknown error  error is  "+error.toString(), Toast.LENGTH_LONG).show();
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
                params.put("userID", String.valueOf(SharedPrefManager.getInstance(mContext).getUserId()));
                params.put("time", timeStamp);
                //params.put("carID", String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getCarId()));
                //params.put("locationTime", timeStamp);


                return params;
            }

        };

        RequestHandler.getInstance(mContext).addToRequestQueue(stringRequest);
    }

    private void parseUsersJson(JSONObject users){
        Log.d(TAG, "parseUsersJson: with users + "+users.toString());
        for (int i = 0; i < users.length(); i++) {
            JSONObject jsonobject = null;
            try {
                jsonobject = users.getJSONObject("user"+String.valueOf(i));
                double latitude=jsonobject.getDouble("latitude");
                double longitude=jsonobject.getDouble("longitude");
                //Integer id=jsonobject.getInt("userId");
                Location location=new Location("");
                location.setLongitude(longitude);
                location.setLatitude(latitude);
                locations.add(location);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}
