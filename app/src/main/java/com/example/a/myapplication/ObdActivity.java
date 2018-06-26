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

import com.example.a.myapplication.OBD.ObdConfigration.ObdConfig;
import com.example.a.myapplication.OBD.ObdData.obdLiveData;
import com.example.a.myapplication.OBD.ObdData.obdStart;
import com.example.a.myapplication.OBD.obdApi.ObdCommand;
import com.example.a.myapplication.OBD.obdConnection.Constants;
import com.example.a.myapplication.OBD.obdConnection.obdBlutoothManager;

import java.util.ArrayList;
import java.util.LinkedList;

public class ObdActivity extends AppCompatActivity {

    private final static int REQUEST_ENABLE_BT = 1;
    private CardView cv1;
    private BluetoothAdapter mBluetoothAdapter = null;

    private obdStart mobdStart = null;
    private TextView Textview2;


    // time wait between sending data to server 1000 * 60 * 5 = 5 min
    private static Thread t ;
    private static boolean serverThreadRunning = false;
    private final int dataToServer_sleepTime_InMillsec = 1000 * 60 * 1;
    private obdLiveData mobObdLiveData;
    private final int loopFristNumber = 0;
    private final int loopLastNumber = 51;

    private final static int start = 1;
    private final static int stop = 0;
    private int state;



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



        mobObdLiveData = new obdLiveData();


        Runnable r = new Runnable() {
            @Override
            public void run() {

                serverThreadRunning = true;

                //for testing
                //Textview2.setText("Connecting...  ");



                while (state==start) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(getApplicationContext(),"entering server thread",Toast.LENGTH_SHORT).show();
                    }
                });

                    ObdUtils obdUtils = ObdUtils.getInstance(getApplicationContext());


                    ArrayList<ObdCommand> x = new ArrayList<ObdCommand>(ObdConfig.getCommands());

                /*x.add(new EngineCoolantTemperatureCommand());
                x.add(new TroubleCodesCommand());
                x.add(new FuelTrimCommand(FuelTrim.LONG_TERM_BANK_1));
                x.add(new IntakeManifoldPressureCommand());
                */
                    mobObdLiveData.setDataPlace(loopFristNumber, loopLastNumber);

                    mobObdLiveData.setQueuCommands(x);

                    mobObdLiveData.setServer(true);

                    // spin till all commands run and return values
                    // remove ! in real case
                   // while (!mobObdLiveData.isServer());


                    LinkedList<String> l = new LinkedList<String>(mobObdLiveData.getData());

                    String s = "";
                    for (int i = loopFristNumber; i <= loopLastNumber; i++) {
                        s += l.get(i);
                        s += "\n";
                    }

                    String[] readings = s.split("\n");


                    // now we have something like  speed : 12 km/h
                    // lets split into key and value

                    ArrayList<String> keys = new ArrayList<String>();
                    ArrayList<String> values = new ArrayList<String>();

                    for (int i = 0; i < readings.length; i++) {

                        String[] d = readings[i].split(":");
                        d[0] = d[0].replaceAll(" ", "");
                        d[0] = d[0].replaceAll("/", "");
                        //d[0]=d[0].replaceAll("(","");
                        //d[0]=d[0].replaceAll(")","");
                        d[0] = d[0].replaceAll(",", "");
                        d[0] = d[0].replaceAll("'", "");

                        d[1] = d[1].replaceAll(" ", "");

                        keys.add(d[0]);
                        values.add(d[1]);

                    }
               /*
                Date currentTime = Calendar.getInstance().getTime();
                keys.add("time");
                values.add(currentTime.toString());
                */
                    obdUtils.setObdData(keys, values);

                    mobObdLiveData.returnprevQueue();

                    try {
                        Thread.sleep(dataToServer_sleepTime_InMillsec);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                serverThreadRunning=false;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(getApplicationContext(),"leaving server thread",Toast.LENGTH_SHORT).show();
                    }
                });

            }};


        //if(User agree to share his data )
        if(!serverThreadRunning) {
            t = new Thread(r);
            t.start();
        }

    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mobdStart.stopObdBluetoothManager();
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
                            state=start;
                            break;
                        case obdBlutoothManager.STATE_CONNECTING:
                            Textview2.setText("Connecting...  ");
                            state = start;
                            break;
                        case obdBlutoothManager.STATE_LISTEN:
                        case obdBlutoothManager.STATE_NONE:
                            Textview2.setText("notConnected ");
                            state = stop;
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

    public void Expert(View view) {
        Intent i = new Intent(this, ExpertDataActivity.class);
        startActivity(i);
    }

    public void TroubleCodes(View view) {
        Intent i = new Intent(this, TroubleCodesActivity.class);
        startActivity(i);
    }

    public void DriverMode(View view) {
        Intent i = new Intent(this, DriverModeActivity.class);
        startActivity(i);
    }



}
