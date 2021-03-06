package com.example.a.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.a.myapplication.OBD.ObdConfigration.ObdConfig;
import com.example.a.myapplication.OBD.ObdData.obdLiveData;
import com.example.a.myapplication.OBD.obdApi.Commands.temperature.AirIntakeTemperatureCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.temperature.AmbientAirTemperatureCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.temperature.CatalystTemperatureCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.temperature.EngineCoolantTemperatureCommand;
import com.example.a.myapplication.OBD.obdApi.ObdCommand;
import com.example.a.myapplication.OBD.obdApi.enums.CatalystBank;

import java.util.ArrayList;
import java.util.LinkedList;

public class TempDataActivity extends AppCompatActivity {

    private obdLiveData mobObdLiveData = new obdLiveData();
    private TextView [] t = new TextView[7];

    /**
     * related to the thread updateing the ui
     **/
    private final static int start = 1;
    private final static int stop = 0;
    private int state;

    private final int loopFristNumber = 32;
    private final int loopLastNumber = 38;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_data);

        t[0] = (TextView)findViewById(R.id.Text1);
        t[1] = (TextView)findViewById(R.id.Text2);
        t[2] = (TextView)findViewById(R.id.Text3);
        t[3] = (TextView)findViewById(R.id.Text4);
        t[4] = (TextView)findViewById(R.id.Text5);
        t[5] = (TextView)findViewById(R.id.Text6);
        t[6] = (TextView)findViewById(R.id.Text7);


        state = start;


        // send the Temp queue to the shared memory so worker thread run only desierd queue

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
        X.add(new EngineCoolantTemperatureCommand());
        mobObdLiveData.setQueuCommands(X);
        mobObdLiveData.setDataPlace(loopFristNumber,loopFristNumber);

        String s = String.valueOf(loopFristNumber) + "~85~104~1~";


        s += getResources().getString(R.string.EngineCoolantTemperatureCommand);
        i.putExtra("message" , s);
        startActivity(i);

    }
    public void cv2(View view) {
        Intent i = new Intent(this, ObdExplainationActivity.class);

        ArrayList<ObdCommand> X= new ArrayList<>();
        X.add(new AirIntakeTemperatureCommand());
        mobObdLiveData.setQueuCommands(X);
        mobObdLiveData.setDataPlace(loopFristNumber+1,loopFristNumber+1);

        String s = String.valueOf(loopFristNumber+1) + "~0~0~1~";


        s += getResources().getString(R.string.AirIntakeTemperatureCommand);
        i.putExtra("message" , s);
        startActivity(i);

    }
    public void cv3(View view) {
        Intent i = new Intent(this, ObdExplainationActivity.class);

        ArrayList<ObdCommand> X= new ArrayList<>();
        X.add(new CatalystTemperatureCommand(CatalystBank.Catalyst_Temperature_Bank_1_Sensor_1));
        mobObdLiveData.setQueuCommands(X);
        mobObdLiveData.setDataPlace(loopFristNumber+2,loopFristNumber+2);

        String s = String.valueOf(loopFristNumber+2) + "~0~0~1~";


        s += getResources().getString(R.string.CatalystTemperatureCommand);
        i.putExtra("message" , s);
        startActivity(i);

    }
    public void cv4(View view) {
        Intent i = new Intent(this, ObdExplainationActivity.class);

        ArrayList<ObdCommand> X= new ArrayList<>();
        X.add(new CatalystTemperatureCommand(CatalystBank.Catalyst_Temperature_Bank_1_Sensor_2));
        mobObdLiveData.setQueuCommands(X);
        mobObdLiveData.setDataPlace(loopFristNumber+3,loopFristNumber+3);

        String s = String.valueOf(loopFristNumber+3) + "~0~0~1~";


        s += getResources().getString(R.string.CatalystTemperatureCommand);
        i.putExtra("message" , s);
        startActivity(i);

    }
    public void cv5(View view) {
        Intent i = new Intent(this, ObdExplainationActivity.class);

        ArrayList<ObdCommand> X= new ArrayList<>();
        X.add(new CatalystTemperatureCommand(CatalystBank.Catalyst_Temperature_Bank_2_Sensor_1));
        mobObdLiveData.setQueuCommands(X);
        mobObdLiveData.setDataPlace(loopFristNumber+4,loopFristNumber+4);

        String s = String.valueOf(loopFristNumber+4) + "~0~0~1~";


        s += getResources().getString(R.string.CatalystTemperatureCommand);
        i.putExtra("message" , s);
        startActivity(i);

    }
    public void cv6(View view) {
        Intent i = new Intent(this, ObdExplainationActivity.class);

        ArrayList<ObdCommand> X= new ArrayList<>();
        X.add(new CatalystTemperatureCommand(CatalystBank.Catalyst_Temperature_Bank_2_Sensor_2));
        mobObdLiveData.setQueuCommands(X);
        mobObdLiveData.setDataPlace(loopFristNumber+5,loopFristNumber+5);

        String s = String.valueOf(loopFristNumber+5) + "~0~0~1~";


        s += getResources().getString(R.string.CatalystTemperatureCommand);
        i.putExtra("message" , s);
        startActivity(i);

    }
    public void cv7(View view) {
        Intent i = new Intent(this, ObdExplainationActivity.class);

        ArrayList<ObdCommand> X= new ArrayList<>();
        X.add(new AmbientAirTemperatureCommand());
        mobObdLiveData.setQueuCommands(X);
        mobObdLiveData.setDataPlace(loopFristNumber+6,loopFristNumber+6);

        String s = String.valueOf(loopFristNumber+6) + "~0~50~1~";


        s += getResources().getString(R.string.AmbientAirTemperatureCommand);
        i.putExtra("message" , s);
        startActivity(i);

    }

}
