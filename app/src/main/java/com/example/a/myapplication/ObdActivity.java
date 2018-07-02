package com.example.a.myapplication;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a.myapplication.OBD.ObdConfigration.ObdConfig;
import com.example.a.myapplication.OBD.ObdData.obdLiveData;
import com.example.a.myapplication.OBD.ObdData.obdStart;
import com.example.a.myapplication.OBD.obdApi.Commands.TroubleCodes.TroubleCodesCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.fuel.FuelTrimCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.pressure.IntakeManifoldPressureCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.temperature.EngineCoolantTemperatureCommand;
import com.example.a.myapplication.OBD.obdApi.ObdCommand;
import com.example.a.myapplication.OBD.obdApi.enums.FuelTrim;
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
    private static Thread t;
    private static boolean serverThreadRunning = false;
    private final int dataToServer_sleepTime_InMillsec = 1000 * 60 * 0;
    private obdLiveData mobObdLiveData;
    private final int loopFristNumber = 0;
    private final int loopLastNumber = 51;

    private final static int start = 1;
    private final static int stop = 0;
    private int state;

    private static boolean userAgreeToShare=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obd);


        AlertDialog.Builder builder = new AlertDialog.Builder(ObdActivity.this);
        builder.setTitle("Terms of service");
        builder.setMessage(R.string.termsOfService);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                userAgreeToShare=true;
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                userAgreeToShare = false;
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


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


                while (state == start) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(getApplicationContext(), "entering server thread", Toast.LENGTH_SHORT).show();
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
                    try {
                        Thread.sleep(1000 * 60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


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
                serverThreadRunning = false;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(getApplicationContext(), "leaving server thread", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        };


        //if(User agree to share his data )
        if (userAgreeToShare) {
            if (!serverThreadRunning) {
                t = new Thread(r);
                t.start();
            }

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
        state = stop;
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
                            state = start;
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


    public void carStatus(View view) {

        ArrayList<ObdCommand> X = new ArrayList<>();

        X.add(new EngineCoolantTemperatureCommand());
        X.add(new FuelTrimCommand(FuelTrim.LONG_TERM_BANK_1));
        X.add(new IntakeManifoldPressureCommand());
        X.add(new TroubleCodesCommand());

//        final String errorstatment = "";
        final int[] carstatusint= new int[4];

        final Float carmeter =new Float(0);

        mobObdLiveData.setQueuCommands(X);

        mobObdLiveData.setDataPlace(70, 73);

        Runnable r = new Runnable() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Checking your car status ...",Toast.LENGTH_LONG).show();
                    }
                });

                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // lets read the first 3 response for now
                LinkedList<String> l = new LinkedList<String>(mobObdLiveData.getData());
                String s = "";
                for (int i = 70; i <= 72; i++) {
                    s += l.get(i);
                    s += "\n";
                }

                String [] d = s.split("\n");

                Float[] dataasInt = new Float[d.length];
                String [] dataasString = new String[d.length];

                for(int i =0 ; i<d.length;i++)
                {
                    String [] k = d[i].split(":");
                    // k[0] contains key
                    //k[1] contains value
                    char[] c = k[1].toCharArray();

                    String dummy= "";
                    for (char cc :c)
                    {
                        if(isNumber(cc))
                        {
                            dummy+=cc;
                        }

                    }
                    dataasString[i]=dummy;

                }

                for(int i =0;i<dataasString.length;i++)
                {
                    dataasInt[i]=Float.parseFloat(dataasString[i]);

                }

                //now dataasint[0] contains engine coolant
                //now dataasint[1] contains  long term fuel trim
                //now dataasint[2] contains intake manifold pressure


                String errorstatment ="";
                if(dataasInt[0]<=104 && dataasInt[0] >=90)
                {
                    carstatusint[0] = 5;
                }
                else if (dataasInt[0]>=104)
                {
                    carstatusint[0] = 0;
                    errorstatment+=" please check your water level, the coolant temperature is very high ";
                }
                else if(dataasInt[0]<90 && dataasInt[0] > 80)
                {
                    carstatusint[0] = 4;
                }

                else if (dataasInt[0]<80)
                {
                    carstatusint[0]=2;
                    errorstatment+=" please wait till your engine warm up";
                }

                // fuel trim
                if(dataasInt[1]<=8 && dataasInt[1]>=0)
                {
                    carstatusint[1]=5;
                }
                else if (dataasInt[1]>8 && dataasInt[1]<=10)
                {
                    carstatusint[1] = 2;
                    errorstatment+= "\n please check your fuel cycle, your fuel trim is higher then normal  ";
                }

                else if (dataasInt[1]>10)
                {
                    carstatusint[1]=0;
                    errorstatment+="\n Warning your fuel trim is very high, find someone to check your engine as soon as possible ";
                }


                // manifold pressure

                if(dataasInt[2]>=0 && dataasInt[2]<=75)
                {
                    carstatusint[2]=5;
                }

                if(dataasInt[2]>75)
                {
                    carstatusint[2]=0;
                    errorstatment+="\n Warning your manifold pressuer is higer than normal , find someone to check your engine as soon as possible";
                }

                // lets get trouble code response

                String temp3 = l.get(73);

                String[] temp2 = temp3.split("\n");

                // response should be 3 trouble codes in one frame
                String [] troublecode = temp2[0].split(":");
                //now troublecode[1] contains the trouble codes response


                carstatusint[3]=5;
                if(troublecode[1].equals("No Data"))
                {
                    carstatusint[3] = 5;
                }
                //no trouble codes
                else if (troublecode[1].equals("P0000"))
                {
                    carstatusint[3]=5;
                }
               else if (troublecode[1].equals("P000"))
                {
                    carstatusint[3]=5;
                }

                else if(troublecode[1].equals(" ")) {

                    carstatusint[3]=5;
                }
                else if (!troublecode[1].equals(" ")) {
                    carstatusint[3] = 0;
                    errorstatment += "\n you have trouble code no: " + troublecode[1];

                    troublecode[1] = troublecode[1].replaceAll(" ", "");
                    final char[] c = troublecode[1].toCharArray();
                    if (c.length >= 3) {
                        if (c[0] == 'B') {
                            errorstatment += "\n B: Body Systems (lighting, airbags, climate control system, etc.)";
                        } else if (c[0] == 'C') {
                            errorstatment += "\n C: Chassis Systems (anti-lock brake system, electronic suspension and steering systems, etc";
                        } else if (c[0] == 'P') {
                            errorstatment += "\n P: Powertrain Systems (engine, emission and transmission systems)";

                        } else if (c[0] == 'U') {
                            errorstatment += "\n U: Network Communication and Vehicle Integration Systems";

                        }


                        if (c[1] == '0') {
                            errorstatment += "\n 0: generic code";

                        } else if (c[1] == '1') {

                            errorstatment += "\n 1: Manufacturer Specific Code";
                        }
                        switch (c[2]) {
                            case '0':
                                errorstatment += "\n 0: Overall System";
                                break;
                            case '1':
                                errorstatment += "\n 1: Secondary Air Injection System";
                                break;
                            case '2':
                                errorstatment += "\n 2: Fuel System";
                                break;
                            case '3':
                                errorstatment += "\n 3: Ignition System";
                                break;
                            case '4':
                                errorstatment += "\n 4: Exhaust Monitoring System";
                                break;
                            case '5':
                                errorstatment += "\n 5: Idle Speed Control or Cruise Control";
                                break;
                            case '6':
                                errorstatment += "\n 6: Input / Output Signal from Control Units";
                                break;
                            case '7':
                                errorstatment += "\n 7: Transmission System";
                                break;
                            case '8':
                                errorstatment += "\n 8: Transmission System ";
                                break;
                            case '9':
                                errorstatment += "\n 9: Transmission System";
                                break;


                        }
                    }
                }

                    final String finalErrorstatment = errorstatment;
                    runOnUiThread(new Runnable() {
                        @Override

                        public void run() {
                        float carmeter = carstatusint[0]+carstatusint[1]+carstatusint[2]+carstatusint[3];


                            if(carmeter>15) {
                                float x = (carmeter / 20) * 100.0f;

                                AlertDialog.Builder builder = new AlertDialog.Builder(ObdActivity.this);
                                builder.setTitle("Car status");
                                builder.setMessage(String.valueOf(x)+"% \n"+"your car status is perfect"+"\n"+finalErrorstatment);
                                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }

                            else {

                                float x = (carmeter / 20) * 100.0f;

                                AlertDialog.Builder builder = new AlertDialog.Builder(ObdActivity.this);
                                builder.setTitle("Car status");
                                builder.setMessage(String.valueOf(x)+"% \n"+finalErrorstatment);
                                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        /*if(carmeter>15)
                        {
                            float x = (carmeter/20)*100.0f;
                           Toast.makeText(getApplicationContext(),String.valueOf(x)+"% \n"+
                                   "your car status is perfect",Toast.LENGTH_LONG).show();
                        }
                        else {

                            float x = (carmeter/20)*100.0f;
                            Toast.makeText(getApplicationContext(), String.valueOf(x)+"% \n" +
                                    finalErrorstatment,Toast.LENGTH_LONG).show();
                        }*/



                        }
                    });





            }
        };


        Thread t = new Thread(r);
        t.start();
    }


    private boolean isNumber(char s)
    {
        switch (s)
        {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            case '.':
                return true;

            default:
                return false;

        }

    }
}