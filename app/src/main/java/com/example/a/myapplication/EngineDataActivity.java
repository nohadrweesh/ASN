package com.example.a.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.a.myapplication.OBD.ObdData.obdLiveData;

import java.util.LinkedList;

public class EngineDataActivity extends AppCompatActivity {

    //private obdBlutoothManager mobdBlutoothManager  = null;
    //private InputStream in ;
    //private OutputStream out;

    private obdLiveData mobObdLiveData = new obdLiveData();
    private TextView t;

    /**
     * related to the thread updateing the ui
     **/
    private final static int start = 1;
    private final static int stop = 0;
    private int state;

    private final int loopFristNumber = 7;
    private final int loopLastNumber = 12;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engine_data);

        t = (TextView) findViewById(R.id.ttview);

        state = start;

        start();
    }

    @Override
    protected void onPause() {
        state = stop;
        super.onPause();

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
                    final String finalS = s;
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            t.setText(finalS);
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


    public void Click(View view) {
        mobObdLiveData.setData(0, "k");
    }
}
