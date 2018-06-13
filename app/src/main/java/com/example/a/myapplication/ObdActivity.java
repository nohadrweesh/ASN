package com.example.a.myapplication;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a.myapplication.OBD.ObdData.obdStart;
import com.example.a.myapplication.OBD.obdConnection.Constants;
import com.example.a.myapplication.OBD.obdConnection.obdBlutoothManager;

public class ObdActivity extends AppCompatActivity {

    private final static int REQUEST_ENABLE_BT = 1;
    private CardView cv1;
    private BluetoothAdapter mBluetoothAdapter = null;

    private obdStart mobdStart = null;
    private TextView Textview2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obd);

        cv1 = (CardView) findViewById(R.id.cvEngine);


        //check if device supports bluetooth
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "device dosenot support bluetooth", Toast.LENGTH_SHORT).show();
        }
        //check if bluetooth is enable
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        mobdStart = new obdStart();
        Textview2 = (TextView) findViewById(R.id.conect_txtView);

        onStart();
    }

    @Override
    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        // setupobdManager() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (!mobdStart.isRun()) {
            mobdStart.setupobdManager(getApplicationContext(), mHandler);
        }
        //setupobdManager();

        /****for testing only***/
        /*obdLiveData m = new obdLiveData(1);
        for(int i =0 ;i<33;i++)
        {
            m.setData(i,String.valueOf(i));
        }*/

    }


    @Override
    public void onResume() {
        super.onResume();

    }

    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            ObdActivity activity = ObdActivity.this;
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case obdBlutoothManager.STATE_CONNECTED:
                            Textview2.setText("Connected  ");
                            break;
                        case obdBlutoothManager.STATE_CONNECTING:
                            Textview2.setText("Connecting...  ");
                            break;
                        case obdBlutoothManager.STATE_LISTEN:
                        case obdBlutoothManager.STATE_NONE:
                            Textview2.setText("notConnected ");
                            break;
                    }
                    break;

            }
        }
    };


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == this.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    mobdStart.setupobdManager(getApplicationContext(), mHandler);
                }

        }
    }


    public void Engine(View view) {
        Intent i = new Intent(this, EngineDataActivity.class);
       //i.setComponent(new ComponentName("com.example.a.myapplication.OBD.ObdActivities", "EngineDataActivity.java"));
        startActivity(i);

    }

    public void Controll(View view) {
        Intent i = new Intent(this, ControllDataActivity.class);
        startActivity(i);

    }

    public void Fuel(View view) {
        Intent i = new Intent(this, FuelDataActivity.class);
        startActivity(i);

    }

    public void Pressure(View view) {
        Intent i = new Intent(this, PressureDataActivity.class);
        startActivity(i);

    }

    public void Temp(View view) {
        Intent i = new Intent(this, TempDataActivity.class);
        startActivity(i);
    }

    public void Speed(View view) {
        Intent i = new Intent(this, SpeedDataActivity.class);
        startActivity(i);
    }
}
