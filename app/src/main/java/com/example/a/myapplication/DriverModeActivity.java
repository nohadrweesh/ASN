package com.example.a.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.cardiomood.android.controls.gauge.SpeedometerGauge;
import com.example.a.myapplication.OBD.ObdData.obdLiveData;
import com.example.a.myapplication.OBD.obdApi.Commands.SpeedCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.engine.RPMCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.temperature.EngineCoolantTemperatureCommand;
import com.example.a.myapplication.OBD.obdApi.ObdCommand;

import java.util.ArrayList;
import java.util.LinkedList;


public class DriverModeActivity extends AppCompatActivity {

    private SpeedometerGauge rpm;
    private SpeedometerGauge temp;
    private SpeedometerGauge speed;

    private obdLiveData mobObdLiveData = new obdLiveData();
    /**
     * related to the thread updateing the ui
     **/
    private final static int start = 1;
    private final static int stop = 0;
    private int state;

    private final int loopFristNumber = 55;
    private final int loopLastNumber = 57;

    private int lastrpm=0;
    private int lastspeed = 0;
    private int lasttemp =0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_mode);

        TextView myTextView = (TextView) findViewById(R.id.textView7);
        myTextView.setText ( (char) 0x00B0 + "C" );

        // Customize SpeedometerGauge
        rpm = (SpeedometerGauge) findViewById(R.id.rpm);
        speed = (SpeedometerGauge) findViewById(R.id.speed);
        temp = (SpeedometerGauge) findViewById(R.id.temp);


        // Add label converter
        rpm.setLabelConverter(new SpeedometerGauge.LabelConverter() {
            @Override
            public String getLabelFor(double progress, double maxProgress) {
                return String.valueOf((int) Math.round(progress));
            }
        });
        speed.setLabelConverter(new SpeedometerGauge.LabelConverter() {
            @Override
            public String getLabelFor(double progress, double maxProgress) {
                return String.valueOf((int) Math.round(progress));
            }
        });
        temp.setLabelConverter(new SpeedometerGauge.LabelConverter() {
            @Override
            public String getLabelFor(double progress, double maxProgress) {
                return String.valueOf((int) Math.round(progress));
            }
        });


        // configure value range and ticks
        rpm.setMaxSpeed(8);
        rpm.setMajorTickStep(1);
        rpm.setMinorTicks(1);
        rpm.setLabelTextSize(16);
        // Configure value range colors
        rpm.addColoredRange(0, 3, Color.GREEN);
        rpm.addColoredRange(3, 4, Color.YELLOW);
        rpm.addColoredRange(4,8, Color.RED);


        speed.setMaxSpeed(300);
        speed.setMajorTickStep(20);
        speed.setMinorTicks(1);
        speed.setLabelTextSize(16);
        // Configure value range colors
        speed.addColoredRange(20, 100, Color.GREEN);
        speed.addColoredRange(100, 140, Color.YELLOW);
        speed.addColoredRange(140, 300, Color.RED);

        temp.setMaxSpeed(130);
        temp.setMajorTickStep(10);
        temp.setMinorTicks(1);
        temp.setLabelTextSize(12);
        // Configure value range colors
        temp.addColoredRange(0, 95, Color.GREEN);
        temp.addColoredRange(95, 105, Color.YELLOW);
        temp.addColoredRange(105, 150, Color.RED);




        state = start;

        ArrayList<ObdCommand> X  = new ArrayList<>();
        X.add(new SpeedCommand());
        X.add(new RPMCommand());
        X.add(new EngineCoolantTemperatureCommand());

        mobObdLiveData.setQueuCommands(X);

        mobObdLiveData.setDataPlace(loopFristNumber,loopLastNumber);

        start();


/*
        rpm.setSpeed(0);
        for (int i = 0; i < 300; i += 20) {
           rpm.setSpeed(i, true);
        }
*/



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

                    String [] d = s.split("\n");

                    final int[] dataasInt = new int[d.length];
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
                        dataasInt[i]=Integer.parseInt(dataasString[i]);

                    }


                    h.post(new Runnable() {
                        @Override
                        public void run() {

                            if (lastspeed != dataasInt[0]) {
                                speed.setSpeed(dataasInt[0], true);
                                lastspeed = dataasInt[0];
                            }

                            if (lastrpm != dataasInt[1]) {
                               float d = (dataasInt[1])/1000.0f;
                                rpm.setSpeed(d, true);
                                lastrpm = dataasInt[1];
                            }
                            if (lasttemp != dataasInt[2]) {
                                temp.setSpeed(dataasInt[2], true);
                                lasttemp = dataasInt[2];
                            }

                        }
                    });

                    try {
                        Thread.sleep(40);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }

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
                return true;

            default:
                return false;

        }

    }
}



/*
    private final Handler mHandler = new Handler();
    private LineGraphSeries series ;
    private Double d = 4d;

    GraphView graph = (GraphView) findViewById(R.id.graph);


        series = new LineGraphSeries();
                graph.addSeries(series);

                graph.getViewport().setXAxisBoundsManual(true);
                graph.getViewport().setMinX(0);
                graph.getViewport().setMaxX(40);

                Runnable mTimer1;


                mTimer1 = new Runnable() {
@Override
public void run() {
        d +=1d;
        series.appendData(new DataPoint(d , d/2),true,40);
        mHandler.postDelayed(this, 300);
        }
        };
        mHandler.postDelayed(mTimer1, 300);
*/