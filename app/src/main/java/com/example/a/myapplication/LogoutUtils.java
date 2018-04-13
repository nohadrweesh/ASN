package com.example.a.myapplication;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Speed on 13/04/2018.
 */

public class LogoutUtils {
    private static final String TAG = "LogoutUtils";
    private static Context mContext;
    private static LogoutUtils mInstance;
    private LogoutUtils(Context context){
        mContext=context;
    }
    public static LogoutUtils getInstance(Context context){
        if(mInstance==null)
            mInstance=new LogoutUtils(context);
        return mInstance;
    }

    public void logout(){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_LOGOUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: starts with response "+response);

                        try
                        {
                            JSONObject obj = new JSONObject(response);
                            Log.d(TAG, "onResponse: "+response);

                            if(!obj.getBoolean("error")){
                                //Toast.makeText(mContext,"location set  ",Toast.LENGTH_LONG).show();
                                Log.d(TAG, "onResponse: location set");
                                //finish();
                            }else{
                                /*Toast.makeText(
                                        mContext,
                                        obj.getString("message"),
                                        Toast.LENGTH_LONG
                                ).show();*/
                                Log.d(TAG, "onResponse: error + "+obj.getString("message"));
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
                        Log.d(TAG, "onErrorResponse: starts "+error.toString());

                        // Toast.makeText(mContext, "unknown error  error is  "+error.toString(), Toast.LENGTH_LONG).show();

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.d(TAG, "getParams: starts");

                Map<String, String> params = new HashMap<>();

                params.put("userID", String.valueOf(SharedPrefManager.getInstance(mContext).getUserId()));


                return params;
            }

        };

        RequestHandler.getInstance(mContext).addToRequestQueue(stringRequest);

        SharedPrefManager.getInstance(mContext).logout();
    }
}
