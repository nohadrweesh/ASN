package com.example.a.myapplication;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.a.myapplication.database.DatabaseHelper;
import com.example.a.myapplication.database.model.Offer;
import com.example.a.myapplication.dbUtils.MyDividerItemDecoration;
import com.example.a.myapplication.dbUtils.RecyclerTouchListener;
import com.example.a.myapplication.views.OffersAdapter;

import java.util.ArrayList;
import java.util.List;

public class OffersActivity extends AppCompatActivity {
    private static final String TAG = "OffersActivity";
    private OffersAdapter mAdapter;
    private List<Offer> offersList = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private TextView noOffersView;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coordinatorLayout =(CoordinatorLayout) findViewById(R.id.coordinator_layout);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        noOffersView = (TextView)findViewById(R.id.empty_offers_view);

        db = new DatabaseHelper(this);

        offersList.addAll(db.getAllOffers());
        Log.d(TAG, "onCreate: "+db.getAllOffers());



        mAdapter = new OffersAdapter(this, offersList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);

        toggleEmptyOffers();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            //TODO:ADDING A WAY TO USE THE OFFER OR SHOW THE WEBSITE
            @Override
            public void onClick(View view, final int position) {


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }


    private void toggleEmptyOffers() {


        if (db.getOffersCount() > 0) {
            noOffersView.setVisibility(View.GONE);
        } else {
            noOffersView.setVisibility(View.VISIBLE);
        }
    }
}