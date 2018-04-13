package com.example.a.myapplication.services;

import android.util.Log;

import com.example.a.myapplication.SharedPrefManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;


public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {



    private static final String TAG = "MyFirebaseIdService";
    private static final String TOPIC_GLOBAL = "global";
    public MyFirebaseInstanceIdService() {
        //Log.d(TAG, "MyFirebaseInstanceIdService: STARTS");
    }

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "onTokenRefresh: token = "+refreshedToken);
        //Toast.makeText(getApplicationContext(),"token is "+refreshedToken,Toast.LENGTH_LONG).show();

        // now subscribe to `global` topic to receive app wide notifications
        FirebaseMessaging.getInstance().subscribeToTopic("global");

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.

        sendRegistrationToServer(refreshedToken);
    }

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(final String token) {
        // TODO: Implement this method to send token to your app server.
        SharedPrefManager.getInstance(getApplicationContext()).setToken(token);

//
//        StringRequest stringRequest = new StringRequest(
//                Request.Method.POST,
//                Constants.URL_TOKEN,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.d(TAG, "onResponse: starts with response "+response);
//
//                        try
//                        {
//                            JSONObject obj = new JSONObject(response);
//                            Log.d(TAG, "onResponse: "+response);
//
//                            if(!obj.getBoolean("error")){
//                                //Toast.makeText(getApplicationContext(),"token set  ",Toast.LENGTH_SHORT).show();
//                                Log.d(TAG, "onResponse: token set");
//                                //finish();
//                            }else{
//                                /*Toast.makeText(
//                                        getApplicationContext(),
//                                        obj.getString("message"),
//                                        Toast.LENGTH_LONG
//                                ).show();*/
//                                Log.d(TAG, "onResponse:  error "+obj.getString("message"));
//                            }
//                        } catch (JSONException e) {
//
//                            Log.d(TAG, "onResponse: error"+response);
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d(TAG, "onErrorResponse: starts + error "+error.toString());
//
//                        //Toast.makeText(getApplicationContext(), "unknown error  error is  "+error.toString(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//
//                Map<String, String> params = new HashMap<>();
//                params.put("token", String.valueOf(token));
//
//                params.put("userID", String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getUserId()));
//
//
//                return params;
//            }
//
//        };
//
//        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
}
