package com.example.a.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.a.myapplication.OBD.ObdConfigration.ObdConfig;
import com.example.a.myapplication.OBD.ObdData.obdLiveData;
import com.example.a.myapplication.OBD.obdApi.Commands.pressure.AbsoluteEvapSystemVaporPressureCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.pressure.BarometricPressureCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.pressure.EvapSystemVaporPressureCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.pressure.FuelPressureCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.pressure.FuelRailPressureCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.pressure.IntakeManifoldPressureCommand;
import com.example.a.myapplication.OBD.obdApi.ObdCommand;

import java.util.ArrayList;
import java.util.LinkedList;

public class PressureDataActivity extends AppCompatActivity {

    private obdLiveData mobObdLiveData = new obdLiveData();
    private TextView [] t = new TextView[6];


    /**
     * related to the thread updateing the ui
     **/
    private final static int start = 1;
    private final static int stop = 0;
    private int state;

    private final int loopFristNumber = 26;
    private final int loopLastNumber = 31;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pressure_data);

        t[0] = (TextView)findViewById(R.id.Text1);
        t[1] = (TextView)findViewById(R.id.Text2);
        t[2] = (TextView)findViewById(R.id.Text3);
        t[3] = (TextView)findViewById(R.id.Text4);
        t[4] = (TextView)findViewById(R.id.Text5);
        t[5] = (TextView)findViewById(R.id.Text6);


        state = start;

        // send the Pressure queue to the shared memory so worker thread run only desierd queue

        ArrayList<ObdCommand> cmds = ObdConfig.getCommands();

        ArrayList<ObdCommand> X  = new ArrayList<>();
        for (int i = loopFristNumber; i <= loopLastNumber; i++) {
            X.add(cmds.get(i));
        }
        mobObdLiveData.setQueuCommands(X);

        mobObdLiveData.setDataPlace(loopFristNumber,loopLastNumber);


        start();
    }

    @Override
    protected void onPause() {
        state = stop;
        super.onPause();

    }


    @Override
    protected void onResume() {
        super.onResume();
        state =start;
    }



    public void start() {

        final Handler h = new Handler();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                while (state == 1) {
                    LinkedList<String> l = new LinkedList<String>(mobObdLiveData.getData());
                    String s = "";
                    for (int i = loopFristNumber; i <= loopLastNumber; i++) {
                        s += l.get(i);
                        s += "\n";
                    }
                    final String [] finalS =s.split("\n");
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            for(int i = 0;i<finalS.length;i++)
                                t[i].setText(finalS[i]);
                        }
                    });

                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }

            }
        };
        Thread t = new Thread(r);
        t.start();

    }

    public void cv1(View view) {
        Intent i = new Intent(this, ObdExplainationActivity.class);

        ArrayList<ObdCommand> X= new ArrayList<>();
        X.add(new FuelPressureCommand());
        mobObdLiveData.setQueuCommands(X);
        mobObdLiveData.setDataPlace(loopFristNumber,loopFristNumber);

        String s = String.valueOf(loopFristNumber) + "~0~765~1~";


        s += getResources().getString(R.string.FuelPressureCommand);
        i.putExtra("message" , s);
        startActivity(i);

    }
    public void cv2(View view) {
        Intent i = new Intent(this, ObdExplainationActivity.class);

        ArrayList<ObdCommand> X= new ArrayList<>();
        X.add(new FuelRailPressureCommand());
        mobObdLiveData.setQueuCommands(X);
        mobObdLiveData.setDataPlace(loopFristNumber+1,loopFristNumber+1);

        String s = String.valueOf(loopFristNumber+1) + "~0~655350~1~";


        s += getResources().getString(R.string.FuelRailPressureCommand);
        i.putExtra("message" , s);
        startActivity(i);

    }
    public void cv3(View view) {
        Intent i = new Intent(this, ObdExplainationActivity.class);

        ArrayList<ObdCommand> X= new ArrayList<>();
        X.add(new BarometricPressureCommand());
        mobObdLiveData.setQueuCommands(X);
        mobObdLiveData.setDataPlace(loopFristNumber+2,loopFristNumber+2);

        String s = String.valueOf(loopFristNumber+2) + "~0~101~1~";


        s += getResources().getString(R.string.BarometricPressureCommand);
        i.putExtra("message" , s);
        startActivity(i);

    }
    public void cv4(View view) {
        Intent i = new Intent(this, ObdExplainationActivity.class);

        ArrayList<ObdCommand> X= new ArrayList<>();
        X.add(new IntakeManifoldPressureCommand());
        mobObdLiveData.setQueuCommands(X);
        mobObdLiveData.setDataPlace(loopFristNumber+3,loopFristNumber+3);

        String s = String.valueOf(loopFristNumber+3) + "~0~74~1~";


        s += getResources().getString(R.string.IntakeManifoldPressureCommand);
        i.putExtra("message" , s);
        startActivity(i);

    }
    public void cv5(View view) {

        Intent i = new Intent(this, ObdExplainationActivity.class);

        ArrayList<ObdCommand> X= new ArrayList<>();
        X.add(new AbsoluteEvapSystemVaporPressureCommand());
        mobObdLiveData.setQueuCommands(X);
        mobObdLiveData.setDataPlace(loopFristNumber+4,loopFristNumber+4);

        String s = String.valueOf(loopFristNumber+4) + "~0~0~1~";


        s += getResources().getString(R.string.AbsoluteEvapSystemVaporPressureCommand);
        i.putExtra("message" , s);
        startActivity(i);

    }
    public void cv6(View view) {

        //Toast.makeText(getApplicationContext(),getResources().getString(R.string.EvapSystemVaporPressureCommand),Toast.LENGTH_SHORT).show();

        Intent i = new Intent(this, ObdExplainationActivity.class);

        ArrayList<ObdCommand> X= new ArrayList<>();
        X.add(new EvapSystemVaporPressureCommand());
        mobObdLiveData.setQueuCommands(X);
        mobObdLiveData.setDataPlace(loopFristNumber+5,loopFristNumber+5);

        String s = String.valueOf(loopFristNumber+5) + "~0~0~1~";


        s += getResources().getString(R.string.EvapSystemVaporPressureCommand);
        i.putExtra("message" , s);
        startActivity(i);

    }

}
