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

public class TroubleCodesActivity extends AppCompatActivity {

    private obdLiveData mobObdLiveData = new obdLiveData();
    private TextView[] t = new TextView[2];

    /**
     * related to the thread updateing the ui
     **/
    private final static int start = 1;
    private final static int stop = 0;
    private int state;

    private final int loopFristNumber = 48;
    private final int loopLastNumber = 48;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trouble_codes);
        t[0] = (TextView) findViewById(R.id.Text1);


        state = start;


        // send the truoble code number queue to the shared memory so worker thread run only desierd queue

        ArrayList<ObdCommand> cmds = ObdConfig.getCommands();

        ArrayList<ObdCommand> X = new ArrayList<>();
        for (int i = loopFristNumber; i <= loopLastNumber; i++) {
            X.add(cmds.get(i));
        }

        mobObdLiveData.setQueuCommands(X);

        mobObdLiveData.setDataPlace(loopFristNumber, loopLastNumber);

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
        state = start;
    }

    public void start() {

        final Handler h = new Handler();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                int count = 0;
                while (state == 1) {
                    LinkedList<String> l = new LinkedList<String>(mobObdLiveData.getData());
                    String s = "";
                    for (int i = loopFristNumber; i <= loopLastNumber; i++) {
                        s += l.get(i);
                        s += "\n";
                    }
                    final String[] finalS = s.split("\n");
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < finalS.length; i++)
                                t[i].setText(finalS[i]);
                        }
                    });

                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // no need to keep reading the same value
                    if (count == 5) {
                        state = stop;
                    }
                    count++;
                }

            }
        };
        Thread t = new Thread(r);
        t.start();
    }


    public void TrubleCodes(View view) {
        Intent i = new Intent(this, TroubleCodesActivity2.class);
        i.putExtra("message", 49);
        startActivity(i);
    }

    public void PendingTroubleCodes(View view) {
        Intent i = new Intent(this, TroubleCodesActivity2.class);
        i.putExtra("message", 50);
        startActivity(i);
    }

    public void PerminantTroubleCodes(View view) {
        Intent i = new Intent(this, TroubleCodesActivity2.class);
        i.putExtra("message", 51);
        startActivity(i);
    }


    public void cv1(View view) {
        Intent i = new Intent(this, ObdExplainationActivity.class);

        String s = String.valueOf(loopFristNumber+8) + "~0~0~0~";
         s += getResources().getString(R.string.TroubleCodesCommand);
        s+="\n";
        s += getResources().getString(R.string.PendingTroubleCodesCommand);
        s+="\n";
        s+=getResources().getString(R.string.PermanentTroubleCodesCommand);
        i.putExtra("message", s);
        startActivity(i);
    }
}
