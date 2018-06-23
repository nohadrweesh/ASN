package com.example.a.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class ObdExplainationActivity extends AppCompatActivity {

    private TextView t ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obd_explaination);
        Intent intent = getIntent();
        String s = intent.getStringExtra("message");

        t = (TextView) findViewById(R.id.textViewExplain);
        t.setMovementMethod(new ScrollingMovementMethod());
        t.setText(s);
    }
}
