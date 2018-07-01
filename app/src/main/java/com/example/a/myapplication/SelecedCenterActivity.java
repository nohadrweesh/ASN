package com.example.a.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelecedCenterActivity extends AppCompatActivity {
    String centerId;
    String centerName;
    Button next_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_center);
        getIncomingIntent();
        setTitle(centerName);
        next_btn = (Button)findViewById(R.id.Next_btn);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MaintenanceTimeActivity.class);

                // let the new activity know which item we clicked on we use :
                intent.putExtra("center_id", centerId);
                intent.putExtra("center_name", centerName);
                startActivity(intent);
            }
        });
    }

    private void getIncomingIntent(){
        if(getIntent().hasExtra("center_id") && getIntent().hasExtra("center_name")){

            centerId = getIntent().getStringExtra("center_id");
            centerName = getIntent().getStringExtra("center_name");

        }
    }

}
