package com.example.a.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.example.a.myapplication.OBD.ObdData.obdLiveData;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.LinkedList;

public class ObdExplainationActivity extends AppCompatActivity {

    private obdLiveData mobObdLiveData = new obdLiveData();
    /**
     * related to the thread updateing the ui
     **/
    private final static int start = 1;
    private final static int stop = 0;
    private int state;

    private  int loopFristNumber = 0;
    private  int loopLastNumber = 0;

    private int fristThreshold = 0;
    private int secondThreshold = 0;


    private int lastreading=0;

    private TextView t ;


    private LineGraphSeries series ;
    private Double time = 0d;

    private GraphView graph ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obd_explaination);
        Intent intent = getIntent();
        String s = intent.getStringExtra("message");

        String [] messages = s.split("~");

        /**
         *messages[0] --> loop frist number = loop last number = command number
         *
         * messages[1] --> frist threshold
         * messages[2] ---> second threshold
         * messages[3] --> command explain
         */

        loopFristNumber = loopLastNumber = Integer.parseInt(messages[0]);
        fristThreshold = Integer.parseInt(messages[1]);
        secondThreshold=Integer.parseInt(messages[2]);
        state = start;

        t = (TextView) findViewById(R.id.textViewExplain);
        t.setMovementMethod(new ScrollingMovementMethod());
        t.setText(messages[3]);


        GraphView graph = (GraphView) findViewById(R.id.graph);
        series = new LineGraphSeries();
        graph.addSeries(series);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(80);
//        graph.getViewport().setMinY(-100);
//        graph.getViewport().setMaxY(100);




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

                    if(!dataasString[0].equals("")) {
                        for (int i = 0; i < dataasString.length; i++) {
                            dataasInt[i] = Integer.parseInt(dataasString[i]);

                        }
                    }
                    else{
                        dataasInt[0]=0;
                    }



                    h.post(new Runnable() {
                        @Override
                        public void run() {

                            time +=1d;
                            series.appendData(new DataPoint(time , dataasInt[0]),true,40);
                            //lastreading = dataasInt[0];
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
