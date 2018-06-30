package com.example.a.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;

public class CenterSelectionActivity extends AppCompatActivity {

    private static final String URL_CENTERS = Constants.URL_CENTERS;
    List<Center> CentersList;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_selection);

        recyclerView = findViewById(R.id.recylcerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CentersList = new ArrayList<>();
        loadCenters();

    }


    private void loadCenters() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CENTERS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject Center = array.getJSONObject(i);

                                //adding the product to product list
                                CentersList.add(new Center(
                                        Center.getString("id"),
                                        Center.getString("name"),
                                        Center.getString("location")
                                ));
                                Log.d(TAG, "getParams: starts with "+CentersList.get(i).getId() +CentersList.get(i).getName()+CentersList.get(i).getLocation());
                            }

                            CenterAdapter adapter = new CenterAdapter(CenterSelectionActivity.this, CentersList);
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
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);

    }
}
