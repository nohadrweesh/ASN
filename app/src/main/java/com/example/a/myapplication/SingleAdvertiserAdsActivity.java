package com.example.a.myapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SingleAdvertiserAdsActivity extends AppCompatActivity {

    private int ownerID;
    private String ownerName;
    private List<AdvertisementClass> ads;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private TextView ownerName_txtvw, aboutOwnerName_txtvw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_advertiser_ads);

        Intent i = getIntent();

        ownerID = i.getIntExtra("ownerID", -1);
        ownerName = i.getStringExtra("ownerName");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ownerName_txtvw = (TextView) findViewById(R.id.ownerName);
        aboutOwnerName_txtvw = (TextView) findViewById(R.id.aboutOwnerBtnOwnerName);

        ownerName_txtvw.setText(ownerName);
        aboutOwnerName_txtvw.setText(ownerName);

        ads = new ArrayList<>();

        loadRecyclerViewData();
    }

    private void loadRecyclerViewData(){
//        final AdvFragment self = this;

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading ads ... ");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_FETCH_ADS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
//                            JSONObject obj = new JSONObject(response);
                            JSONArray obj = new JSONArray(response);
                            Log.d("Fetch Ads Response", ""+response);


                            /*
                                Parse the JSON object here and fill the ads list
                             */

                            for(int i = 0; i < obj.length(); i++){
//                                JSONObject o = obj.getJSONObject(i);
                                JSONObject o = obj.getJSONObject(i);

                                    AdvertisementClass ad = new AdvertisementClass(
                                            o.getInt("ID"),
                                            ownerID,
                                            ownerName,
                                            o.getString("title"),
                                            o.getString("description"),
                                            o.getString("imgURL"),
//                                            "https://asnasucse18.000webhostapp.com/res/AdvertisersApp/AdsImgs/Test.jpg",
                                            o.getString("expirationDate")
                                    );
                                    ads.add(ad);
                                    __AvailableAds.ads.add(ad);
//                                    break;
                            }


                            // Define the adapter and bind it to the RecyclerView
                            adapter = new RecyclerViewAdapterClass(ads, getApplicationContext());
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ownerID", String.valueOf(ownerID));
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void fetchOwnerInfo(){
        StringRequest stringRequest = new StringRequest(
                com.android.volley.Request.Method.POST,
                Constants.URL_GET_AD_OWNER_INFO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);

                            loadOwnerProfile(
                                obj.getString("name"),
                                obj.getString("slogan"),
                                obj.getString("serviceType"),
                                obj.getString("workingTimes"),
                                obj.getString("adrs"),
                                obj.getString("phone"),
                                obj.getString("email"),
                                obj.getString("iconURL")
                            );

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ownerID", String.valueOf(ownerID));
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void loadOwnerProfile(
            String name,
            String slogan,
            String serviceType,
            String availibility,
            String adrs,
            String phone,
            String mail,
            String iconURL
    ){
        TextView owName, owSlogan, owServiceType, owAvailibility, owAdrs, owPhone, owEmail;
        ImageView owIcon;

        View ownerProfileView = getLayoutInflater().inflate(R.layout.advertiser_profile, null);

        owName = (TextView) ownerProfileView.findViewById(R.id.name);
        owSlogan = (TextView) ownerProfileView.findViewById(R.id.slogan);
        owServiceType = (TextView) ownerProfileView.findViewById(R.id.serviceType);
        owAvailibility = (TextView) ownerProfileView.findViewById(R.id.availibilityValue);
        owAdrs = (TextView) ownerProfileView.findViewById(R.id.adrsValue);
        owPhone = (TextView) ownerProfileView.findViewById(R.id.phoneValue);
        owEmail = (TextView) ownerProfileView.findViewById(R.id.emailValue);

        owIcon = (ImageView) ownerProfileView.findViewById(R.id.icon);


        owName.setText(name);
        owSlogan.setText(slogan);
        owServiceType.setText(serviceType);
        owAvailibility.setText(availibility);
        owAdrs.setText(adrs);
        owPhone.setText(phone);
        owEmail.setText(mail);

        Picasso.with(this).load(iconURL).into(owIcon);

        Dialog settingsDialog = new Dialog(this);
        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        settingsDialog.setContentView(ownerProfileView);
        settingsDialog.show();
    }
    public void aboutOwnerBtnOnClick(View view) {
        fetchOwnerInfo();
    }

}
