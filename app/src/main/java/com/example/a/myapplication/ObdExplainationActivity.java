package com.example.a.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
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

    private float fristThreshold = 0;
    private float secondThreshold = 0;


    private float lastreading=0;

    private TextView t ;


    private LineGraphSeries series0;
    private LineGraphSeries series ;
    private LineGraphSeries series2;
    private LineGraphSeries series3;
    private Double time = 0d;
    private int showgraph ;

    private GraphView graph ;

    @SuppressLint("WrongConstant")
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
         * messages[3]----> show graph flag 1= show ; zero = don't show
         * messages[4] --> command explain
         */

        loopFristNumber = loopLastNumber = Integer.parseInt(messages[0]);
        fristThreshold = Float.parseFloat(messages[1]);
        secondThreshold=Float.parseFloat(messages[2]);

        if(messages[3].equals("1")) {
            state = start;
        }

        t = (TextView) findViewById(R.id.textViewExplain);
        t.setMovementMethod(new ScrollingMovementMethod());
        t.setText(messages[4]);


        GraphView graph = (GraphView) findViewById(R.id.graph);
        if(messages[3].equals("0"))
        {
            graph.setVisibility(4);
        }
        series = new LineGraphSeries<DataPoint>();
        series0 = new LineGraphSeries<DataPoint>();

        series2=new LineGraphSeries<DataPoint>();
        series3=new LineGraphSeries<DataPoint>();

        graph.addSeries(series);
        graph.addSeries(series0);
        series0.setColor(Color.BLACK);



        if(secondThreshold==0){

        }
        else {

            /*graph.getSecondScale().addSeries(series2);
            graph.getSecondScale().setMinY(fristThreshold);
            graph.getSecondScale().setMaxY(secondThreshold);
            */
            series2.setColor(Color.RED);
            //graph.getGridLabelRenderer().setVerticalLabelsSecondScaleColor(Color.RED);

            //graph.getSecondScale().addSeries(series3);
            series3.setColor(Color.RED);
            graph.addSeries(series2);
            graph.addSeries(series3);

        }
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(100);


        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);

        graph.getViewport().setScalableY(true);
        graph.getViewport().setScrollableY(true);

        graph.getGridLabelRenderer().setHighlightZeroLines(true);
        graph.getGridLabelRenderer().setTextSize(18);


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

                    final float[] dataasInt = new float[d.length];
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
                            dataasInt[i] = Float.parseFloat(dataasString[i]);

                        }
                    }

                    else{
                        dataasInt[0] = 0;

                    }



                    h.post(new Runnable() {
                        @Override
                        public void run() {

                            time +=1d;
                            series.appendData(new DataPoint(time , dataasInt[0]),true,1000);

                            //we always draw zero line to keep it availabe
                            if(dataasInt[0]!=0)
                           series0.appendData(new DataPoint(time , 0),true,1000);


                            series2.appendData(new DataPoint(time,fristThreshold),true,1000);
                            series3.appendData(new DataPoint(time,secondThreshold),true,1000);
                            //lastreading = dataasInt[0];
                        }
                    });

                    try {
                        Thread.sleep(50);
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
            case '.':
            case '-':
                return true;

            default:
                return false;

        }

    }
}
