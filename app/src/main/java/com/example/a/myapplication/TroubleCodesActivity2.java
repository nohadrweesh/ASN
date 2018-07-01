package com.example.a.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a.myapplication.OBD.ObdConfigration.ObdConfig;
import com.example.a.myapplication.OBD.ObdData.obdLiveData;
import com.example.a.myapplication.OBD.obdApi.ObdCommand;

import java.util.ArrayList;
import java.util.LinkedList;

public class TroubleCodesActivity2 extends AppCompatActivity {

    private obdLiveData mobObdLiveData = new obdLiveData();
    private TextView[] t = new TextView[2];

    /**
     * related to the thread updateing the ui
     **/
    private final static int start = 1;
    private final static int stop = 0;
    private int state;

    private  int loopFristNumber = 0;
    private  int loopLastNumber = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trouble_codes2);
        Intent intent = getIntent();
        int code = intent.getIntExtra("message",3);
        t[0] = (TextView) findViewById(R.id.textViewTc);

        Toast.makeText(getApplicationContext(),"Please wait while reading Trouble Codes...",Toast.LENGTH_LONG).show();
        loopFristNumber=loopLastNumber=code;
       // t.setText(String.valueOf(code));

        state = start;
        // send the truoble code number queue to the shared memory so worker thread run only desierd queue

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
                    if (count == 3) {
                        state = stop;
                    }
                    count++;

                    //lets get the response

                    final String ss = (String) t[0].getText();


                    String[] temp2 = ss.split("\n");
                    String errorstatment = "";

                    // response should be 3 trouble codes in one frame
                    String[] troublecode = temp2[0].split(":");
                    //now troublecode[1] contains the trouble codes response


                    boolean detectCode = false;
                    if (troublecode[1].equals("No Data")) {
                        detectCode = false;
                    }
                    //no trouble codes
                    else if (troublecode[1].equals("P0000")) {
                        detectCode = false;
                    } else if (troublecode[1].equals("P000")) {
                        detectCode = false;
                    } else if (troublecode[1].equals(" ")) {

                        detectCode = false;
                    }else if (troublecode[1].equals(" Not connected")){
                     detectCode = false;
                    }
                    else if (!troublecode[1].equals(" ")) {
                        detectCode = true;
                        errorstatment += "\n trouble code no " + troublecode[1] + " details:";

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
                    if(detectCode)
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            t[0].setText(ss + "\n"+finalErrorstatment);

                        }
                    });


                }
            }
        };
        Thread t = new Thread(r);
        t.start();
    }


}
