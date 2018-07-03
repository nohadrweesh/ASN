package com.example.a.myapplication;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SingleAdActivity extends AppCompatActivity {
    private static String TAG = "SingleAdActivity";
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
    TextView owName, owSlogan, owServiceType, owAvailibility, owAdrs, owPhone, owEmail, aboutOwName;
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

        aboutOwName = (TextView) findViewById(R.id.aboutOwnerBtnOwnerName);
        aboutOwName.setText(__AvailableAds.ads.get(adPosition).getOwnerName());

        viewAdInfo();
//        fetchOwnerInfo();
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
        Log.d(TAG, "Fetch Owner Info");

        StringRequest stringRequest = new StringRequest(
                com.android.volley.Request.Method.POST,
                Constants.URL_GET_AD_OWNER_INFO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);

                            Log.d(TAG, "Fetch Owner Info Response : "+response);

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
                            Log.d(TAG, "JSON exception : "+e);
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Response Error : "+error);

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ownerID", String.valueOf(__AvailableAds.ads.get(adPosition).getOwnerID()));

                Log.d(TAG, "Fetch Owner Info Response Set Params ");

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
//        TextView owName, owSlogan, owServiceType, owAvailibility, owAdrs, owPhone, owEmail;
//        ImageView owIcon;

        Log.d(TAG, "Loading Owner Profile, Owner Slogan : "+slogan);

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
        Log.d(TAG, "More About Owner Btn Clicked");
        fetchOwnerInfo();
    }


    public void RmvAdBtnOnClick(View view) {
        __AvailableAds.ads.remove(adPosition);
        startActivity(new Intent(this, AdvertisementsActivity.class ));
        finish();
    }

}

