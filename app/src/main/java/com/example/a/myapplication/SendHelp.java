package com.example.a.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


public class SendHelp extends AppCompatActivity {

    private static int userID;
    private static String username;
    private static String Phone;
    private static int Lng;
    private static int Lat;
    private static int Atit;
    private static String ProblemType;
    private static String ProblemMessage;
    private TextView TextViewUserName,
            TextViewAcceptorLng, TextViewAcceptorLat, TextViewAcceptorAtit, TextViewPhone, TextViewProblemType, TextViewProblemMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_help);
        Intent i = getIntent();
        userID    = i.getIntExtra(   "userID", -1);
        username  = i.getStringExtra("username");
        Phone = i.getStringExtra("phone"   );
        Lng   = i.getIntExtra(   "lng" , -1);
        Lat   = i.getIntExtra(   "lat" , -1);
        Atit  = i.getIntExtra(   "atit", -1);
        ProblemType = i.getStringExtra("problemtype");
        ProblemMessage = i.getStringExtra("problemmessage");

        TextViewUserName  = (TextView) findViewById(R.id.name);
        TextViewPhone = (TextView) findViewById(R.id.phone);
        TextViewAcceptorLng   = (TextView) findViewById(R.id.lng);
        TextViewAcceptorLat   = (TextView) findViewById(R.id.lat);
        TextViewAcceptorAtit  = (TextView) findViewById(R.id.atit);
        TextViewProblemType = (TextView) findViewById(R.id.type);
        TextViewProblemMessage = (TextView) findViewById(R.id.msg);

        TextViewUserName.setText (username);
        TextViewPhone.setText(Phone);
        TextViewAcceptorLng.setText  (String.valueOf(Lng) );
        TextViewAcceptorLat.setText  (String.valueOf(Lat)  );
        TextViewAcceptorAtit.setText (String.valueOf(Atit) );
        TextViewProblemType.setText(ProblemType);
        TextViewProblemMessage.setText(ProblemMessage);
    }
}
