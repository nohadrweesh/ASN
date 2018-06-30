package com.example.a.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public class SingleAdActivity extends AppCompatActivity {
    int adPosition;

    //// AD VARIABLES ////
    // Ad View Elements
    TextView adOwner, adTitle, adDesc, adExpDate;
    ImageView adImg;
    LinearLayout expDate;

    // Ad Value-Holders
//    String owner, title, desc, expDate, imgURL;

    //// OWNER VARIABLES ////
    // Owner View Elements
    TextView owName, owSlogan, owServiceType, owAvailibility, owAdrs, owPhone, owEmail;
    ImageView owIcon;

    // Owner Value-Holders
    String name, slogan, serviceType, availibility, adrs, phone, email, iconURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_ad);

        Intent i = getIntent();
        adPosition = i.getIntExtra("adPosition", -1);

        // Bind Ad View Elements
        adOwner = (TextView) findViewById(R.id.adOwner);
        adTitle = (TextView) findViewById(R.id.adTitle);
        adDesc = (TextView) findViewById(R.id.adDesc);
        adExpDate = (TextView) findViewById(R.id.expDateValue);

        adImg = (ImageView) findViewById(R.id.adImg);
        expDate = (LinearLayout) findViewById(R.id.expDate);


        // Bind Owner View Elements
        owIcon = (ImageView) findViewById(R.id.icon);

        owName = (TextView) findViewById(R.id.name);
        owSlogan = (TextView) findViewById(R.id.slogan);
        owServiceType = (TextView) findViewById(R.id.serviceType);
        owAvailibility = (TextView) findViewById(R.id.availibilityValue);
        owAdrs = (TextView) findViewById(R.id.adrsValue);
        owPhone = (TextView) findViewById(R.id.phoneValue);
        owEmail = (TextView) findViewById(R.id.emailValue);

        viewAdInfo();
        fetchOwnerInfo();
    }

    private void viewOwnerInfo(){
        owName.setText(name);
        owSlogan.setText(slogan);
        owServiceType.setText(serviceType);
        owAvailibility.setText(availibility);
        owAdrs.setText(adrs);
        owEmail.setText(email);
        owPhone.setText(phone);

        Picasso.with(this).load(iconURL).into(owIcon);
    }
    private void viewAdInfo(){
        adOwner.setText(__AvailableAds.ads.get(adPosition).getOwnerName());
        adTitle.setText(__AvailableAds.ads.get(adPosition).getTitle());
        adDesc.setText(__AvailableAds.ads.get(adPosition).getDescription());

        Picasso.with(this).load(__AvailableAds.ads.get(adPosition).getImgURL()).into(adImg);

        if(__AvailableAds.ads.get(adPosition).getExpirationDate() != null){
            adExpDate.setText(__AvailableAds.ads.get(adPosition).getExpirationDate());
            expDate.setVisibility(View.VISIBLE);
        }
        else{
            expDate.setVisibility(View.GONE);
        }
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

                            name = obj.getString("name");
                            slogan = obj.getString("slogan");
                            serviceType = obj.getString("serviceType");
                            availibility = obj.getString("workingTimes");
                            adrs = obj.getString("adrs");
                            phone = obj.getString("phone");
                            email = obj.getString("email");
                            iconURL = obj.getString("iconURL");

                            viewOwnerInfo();

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
                params.put("ownerID", String.valueOf(__AvailableAds.ads.get(adPosition).getOwnerID()));
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void RmvAdBtnOnClick(View view) {
        __AvailableAds.ads.remove(adPosition);
        startActivity(new Intent(this, AdvertisementsActivity.class ));
        finish();
    }
}
