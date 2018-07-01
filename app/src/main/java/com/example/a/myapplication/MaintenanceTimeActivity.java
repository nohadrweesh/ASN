package com.example.a.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MaintenanceTimeActivity extends AppCompatActivity {
    int carId;
    String centerName,centerId;
    Button pick_date,pick_time,done_btn;
    static final int DIALOG_ID = 0;
    int day,month,year,hour,min;
    String dateTime = "";
    TextView dateTimeText1,dateTimeText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_time);

        getIncomingIntent();
        setTitle(centerName);
        carId = SharedPrefManager.getInstance(this).getCarId();
        pick_date = (Button)findViewById(R.id.pick_date_btn);
        dateTimeText1 = (TextView) findViewById(R.id.DateTime_text1);
        dateTimeText2 = (TextView) findViewById(R.id.DateTime_text2);
        pick_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(999);
            }

        });

        showTimePickerDialog();
        done_btn = (Button)findViewById(R.id.done_btn);
        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();
                Intent intent = new Intent(getApplicationContext(), DriverMapActivity.class);
                startActivity(intent);

            }
        });
    }

    public void sendData(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_SEND_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            if(!obj.getBoolean("error")){
                                SharedPrefManager.getInstance(getApplicationContext())
                                        .setMaintenanceInfo(
                                                obj.getInt("carID"),
                                                obj.getInt("serviceCenterID"),
                                                obj.getString("maintenanaceTime")
                                        );
                                startActivity(new Intent(getApplicationContext(), SignIn.class));
                                finish();
                            }else{
                                Toast.makeText(
                                        getApplicationContext(),
                                        obj.getString("message"),
                                        Toast.LENGTH_LONG
                                ).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), Constants.URL_SEND_DATA+" "+error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("carID", String.valueOf(carId));
                params.put("serviceCenterID", centerId);
                params.put("maintenanaceTime", dateTime);

                return params;
            }
        };


        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    public void showTimePickerDialog(){
        pick_time = (Button)findViewById(R.id.pick_time_btn);
        pick_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DIALOG_ID);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        else if(id == DIALOG_ID){
            return new TimePickerDialog(this,myTimePickerListner,hour,min,true);
        }
        return null;
    }

    protected TimePickerDialog.OnTimeSetListener myTimePickerListner = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {

            showTime(i,i1);
        }
    };

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        String date = day +"/" + month +"/" +year;
        dateTime = year+"-"+month+"-"+day +" " +dateTime;
        String s = ((String)dateTimeText1.getText()).replaceAll("Pick Date",date);
        dateTimeText1.setText(s);
    }

    private void showTime(int h, int m) {
        String time = h +":" + m;
        dateTime = dateTime + h+":"+m;
        String s = ((String)dateTimeText2.getText()).replaceAll("Pick Time",time);
        dateTimeText2.setText(s);
    }


    private void getIncomingIntent(){
        if(getIntent().hasExtra("center_id") && getIntent().hasExtra("center_name")){

            centerId = getIntent().getStringExtra("center_id");
            centerName = getIntent().getStringExtra("center_name");

        }
    }
}
