package com.example.a.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.a.myapplication.OBD.ObdConfigration.ObdConfig;
import com.example.a.myapplication.OBD.ObdData.obdLiveData;
import com.example.a.myapplication.OBD.obdApi.ObdCommand;

import java.util.ArrayList;
import java.util.LinkedList;

public class ControllDataActivity extends AppCompatActivity {
    private obdLiveData mobObdLiveData = new obdLiveData();
    private TextView [] t = new TextView[8];

    /**
     * related to the thread updateing the ui
     **/
    private final static int start = 1;
    private final static int stop = 0;
    private int state;

    private final int loopFristNumber = 0;
    private final int loopLastNumber = 7;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controll_data);


        t[0] = (TextView)findViewById(R.id.Text1);
        t[1] = (TextView)findViewById(R.id.Text2);
        t[2] = (TextView)findViewById(R.id.Text3);
        t[3] = (TextView)findViewById(R.id.Text4);
        t[4] = (TextView)findViewById(R.id.Text5);
        t[5] = (TextView)findViewById(R.id.Text6);
        t[6] = (TextView)findViewById(R.id.Text7);
        t[7] = (TextView)findViewById(R.id.Text8);


        state = start;


        // send the Controll queue to the shared memory so worker thread run only desierd queue

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
                        Thread.sleep(4000);
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
        String s = getResources().getString(R.string.VinCommand);
        i.putExtra("message" , s);
        startActivity(i);

    }


    public void cv2(View view) {
        Intent i = new Intent(this, ObdExplainationActivity.class);
        String s = getResources().getString(R.string.ModuleVoltageCommand);
        i.putExtra("message" , s);
        startActivity(i);

    }

    public void cv3(View view) {
        Intent i = new Intent(this, ObdExplainationActivity.class);
        String s = getResources().getString(R.string.TimingAdvanceCommand);
        i.putExtra("message" , s);
        startActivity(i);

    }

    public void cv4(View view) {
        Intent i = new Intent(this, ObdExplainationActivity.class);
        String s = getResources().getString(R.string.EquivalentRatioCommand);
        i.putExtra("message" , s);
        startActivity(i);

    }

    public void cv5(View view) {
        Intent i = new Intent(this, ObdExplainationActivity.class);
        String s = getResources().getString(R.string.DistanceMILOnCommand);
        i.putExtra("message" , s);
        startActivity(i);

    }

    public void cv6(View view) {
        Intent i = new Intent(this, ObdExplainationActivity.class);
        String s = getResources().getString(R.string.TimeRunWithMILOnCommand);
        i.putExtra("message" , s);
        startActivity(i);

    }

    public void cv7(View view) {
        Intent i = new Intent(this, ObdExplainationActivity.class);
        String s = getResources().getString(R.string.DistanceSinceCCCommand);
        i.putExtra("message" , s);
        startActivity(i);

    }

    public void cv8(View view) {
        Intent i = new Intent(this, ObdExplainationActivity.class);
        String s = getResources().getString(R.string.TimeSinceTroubleCodesClearedCommand);
        i.putExtra("message" , s);
        startActivity(i);


    }
}


