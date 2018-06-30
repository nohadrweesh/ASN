package com.example.a.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TrackersListActivity extends AppCompatActivity {

    static TextView msgTV;
    List<Driver> trackers;
    ArrayAdapter<String> adapter;
    int currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trackers_list);

        ListView lv = (ListView)this.findViewById(R.id.trackers_lv);
        ArrayList<String> trackersUsernames = new ArrayList<>() ;
        adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.tracker_item,R.id.trackerUsername_tv,trackersUsernames);
        lv.setAdapter(adapter);

        msgTV = findViewById(R.id.msg_tv2);


        trackers = new ArrayList<>();
        currentUserID=0;
        if(getIntent().hasExtra("currentUserID"))
            currentUserID = getIntent().getIntExtra("currentUserID",0);
        else
        {
            currentUserID = SharedPrefManager.getInstance(getApplicationContext()).getUserId();
        }

        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://asnasucse18.000webhostapp.com/RFTDA/FetchTrackers.php";
        RequestParams params = new RequestParams();
        Log.d("TrackersListActivity","current user ID "+currentUserID);
        params.put("receiverID",currentUserID);
        client.get(url,params, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try
                {
                    handleResponse(response);
                }
                catch (JSONException e)
                {
                    Log.d("TrackersListActivity","onSuccess json error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TrackersListActivity","onFailure");
                msgTV.setText("error in the request");
            }
        });



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Intent intent= new Intent(getApplicationContext(),TrackerProfileActivity.class);
                intent.putExtra("tracker",trackers.get(i));
                intent.putExtra("currentUserID",currentUserID);
                startActivity(intent);
            }
        });
    }

    private void handleResponse(JSONObject response) throws JSONException {
        Log.d("TrackersListActivity","handleResponse response is "+response);
        int trackersNumber = response.getJSONObject("result").getInt("TrackersNumber");
        if(trackersNumber == 0)
            msgTV.setText("No Trackers");
        else
        {
            adapter.clear();
            for (int i=0;i<trackersNumber;i++)
            {
                String key = "tracker"+Integer.toString(i+1);
                int senderID = response.getJSONObject("result").getJSONObject(key).getInt("trackerID");
                String senderUsername = response.getJSONObject("result").getJSONObject(key).getString("trackerUsername");
                String senderEmail = response.getJSONObject("result").getJSONObject(key).getString("trackerEmail");
                String senderToken = response.getJSONObject("result").getJSONObject(key).getString("trackerToken");
                String senderPhonenumber = response.getJSONObject("result").getJSONObject(key).getString("trackerPhonenumber");
                String senderStatus = response.getJSONObject("result").getJSONObject(key).getString("trackerStatus");
                Driver tracker =new Driver(senderID,senderUsername,senderEmail,senderToken,senderPhonenumber,senderStatus);
                adapter.add(senderUsername);
                Log.d("TrackersListActivity","handleResponse tracker is "+tracker.toString());
                trackers.add(tracker);

            }
        }

    }
}
