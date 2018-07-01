package com.example.a.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a.myapplication.OBD.ObdConfigration.ObdConfig;
import com.example.a.myapplication.OBD.ObdData.obdLiveData;
import com.example.a.myapplication.OBD.obdApi.Commands.engine.AbsoluteLoadCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.engine.LoadCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.engine.MassAirFlowCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.engine.RPMCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.engine.RelativeAcceleratorPedalPositionCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.engine.ThrottlePositionCommand;
import com.example.a.myapplication.OBD.obdApi.ObdCommand;

import java.util.ArrayList;
import java.util.LinkedList;

public class EngineDataActivity extends AppCompatActivity {

    //private obdBlutoothManager mobdBlutoothManager  = null;
    //private InputStream in ;
    //private OutputStream out;

    private obdLiveData mobObdLiveData = new obdLiveData();
    private TextView [] t = new TextView[9];


    /**
     * related to the thread updateing the ui
     **/
    private final static int start = 1;
    private final static int stop = 0;
    private int state;

    private final int loopFristNumber = 8;
    private final int loopLastNumber = 16;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engine_data);

        t[0] = (TextView)findViewById(R.id.Text1);
        t[1] = (TextView)findViewById(R.id.Text2);
        t[2] = (TextView)findViewById(R.id.Text3);
        t[3] = (TextView)findViewById(R.id.Text4);
        t[4] = (TextView)findViewById(R.id.Text5);
        t[5] = (TextView)findViewById(R.id.Text6);
        t[6] = (TextView)findViewById(R.id.Text7);
        t[7] = (TextView)findViewById(R.id.Text8);
        t[8] = (TextView) findViewById(R.id.Text9);


        state = start;


        // send the engine queue to the shared memory so worker thread run only desierd queue

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
        X.add(new RPMCommand());
        mobObdLiveData.setQueuCommands(X);
        mobObdLiveData.setDataPlace(loopFristNumber,loopFristNumber);

        String s = String.valueOf(loopFristNumber) + "~0~0~1~";


         s += getResources().getString(R.string.RPMCommand);
        i.putExtra("message" , s);
        startActivity(i);

    }
    public void cv2(View view) {

        Toast.makeText(getApplicationContext(),getResources().getString(R.string.RuntimeCommand),Toast.LENGTH_SHORT).show();
        /*Intent i = new Intent(this, ObdExplainationActivity.class);

        ArrayList<ObdCommand> X= new ArrayList<>();
        X.add(new RuntimeCommand());
        mobObdLiveData.setQueuCommands(X);
        mobObdLiveData.setDataPlace(loopFristNumber+1,loopFristNumber+1);

        String s = String.valueOf(loopFristNumber+1) + "~0~0~0~";


         s += getResources().getString(R.string.RuntimeCommand);
        i.putExtra("message" , s);
        startActivity(i);
*/
    } public void cv3(View view) {

        Toast.makeText(getApplicationContext(),getResources().getString(R.string.OilTempCommand),Toast.LENGTH_SHORT).show();
      /*  Intent i = new Intent(this, ObdExplainationActivity.class);

        ArrayList<ObdCommand> X= new ArrayList<>();
        X.add(new OilTempCommand());
        mobObdLiveData.setQueuCommands(X);
        mobObdLiveData.setDataPlace(loopFristNumber+2,loopFristNumber+2);

        String s = String.valueOf(loopFristNumber+2) + "~0~0~1~";


         s += getResources().getString(R.string.OilTempCommand);
        i.putExtra("message" , s);
        startActivity(i);
*/
    } public void cv4(View view) {
        Intent i = new Intent(this, ObdExplainationActivity.class);

        ArrayList<ObdCommand> X= new ArrayList<>();
        X.add(new ThrottlePositionCommand());
        mobObdLiveData.setQueuCommands(X);
        mobObdLiveData.setDataPlace(loopFristNumber+3,loopFristNumber+3);

        String s = String.valueOf(loopFristNumber+3) + "~0~0~1~";


         s += getResources().getString(R.string.ThrottlePositionCommand);
        i.putExtra("message" , s);
        startActivity(i);

    } public void cv5(View view) {
        Intent i = new Intent(this, ObdExplainationActivity.class);

        ArrayList<ObdCommand> X= new ArrayList<>();
        X.add(new LoadCommand());
        mobObdLiveData.setQueuCommands(X);
        mobObdLiveData.setDataPlace(loopFristNumber+4,loopFristNumber+4);

        String s = String.valueOf(loopFristNumber+4) + "~0~0~1~";


        s += getResources().getString(R.string.LoadCommand);
        i.putExtra("message" , s);
        startActivity(i);

    } public void cv6(View view) {
        Intent i = new Intent(this, ObdExplainationActivity.class);

        ArrayList<ObdCommand> X= new ArrayList<>();
        X.add(new AbsoluteLoadCommand());
        mobObdLiveData.setQueuCommands(X);
        mobObdLiveData.setDataPlace(loopFristNumber+5,loopFristNumber+5);

        String s = String.valueOf(loopFristNumber+5) + "~0~0.95~1~";


         s += getResources().getString(R.string.AbsoluteLoadCommand);
        i.putExtra("message" , s);
        startActivity(i);

    } public void cv7(View view) {
        Intent i = new Intent(this, ObdExplainationActivity.class);

        ArrayList<ObdCommand> X= new ArrayList<>();
        X.add(new MassAirFlowCommand());
        mobObdLiveData.setQueuCommands(X);
        mobObdLiveData.setDataPlace(loopFristNumber+6,loopFristNumber+6);

        String s = String.valueOf(loopFristNumber+6) + "~0~0~1~";


        s += getResources().getString(R.string.MassAirFlowCommand);
        i.putExtra("message" , s);
        startActivity(i);

    } public void cv8(View view) {

        Toast.makeText(getApplicationContext(),getResources().getString(R.string.FuelInjectionTimingCommand),Toast.LENGTH_LONG).show();
        /*Intent i = new Intent(this, ObdExplainationActivity.class);

        ArrayList<ObdCommand> X= new ArrayList<>();
        X.add(new FuelInjectionTimingCommand());
        mobObdLiveData.setQueuCommands(X);
        mobObdLiveData.setDataPlace(loopFristNumber+7,loopFristNumber+7);

        String s = String.valueOf(loopFristNumber+7) + "~0~0~1~";


        s += getResources().getString(R.string.FuelInjectionTimingCommand);
        i.putExtra("message" , s);
        startActivity(i);
*/
    }
    public void cv9(View view) {

        Intent i = new Intent(this, ObdExplainationActivity.class);

        ArrayList<ObdCommand> X= new ArrayList<>();
        X.add(new RelativeAcceleratorPedalPositionCommand());
        mobObdLiveData.setQueuCommands(X);
        mobObdLiveData.setDataPlace(loopFristNumber+8,loopFristNumber+8);

        String s = String.valueOf(loopFristNumber+8) + "~0~0~1~";


        s += getResources().getString(R.string.RelativeAcceleratorPedalPositionCommand);
        i.putExtra("message" , s);
        startActivity(i);

    }
}
