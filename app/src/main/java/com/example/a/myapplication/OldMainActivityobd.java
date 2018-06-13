package com.example.a.myapplication;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a.myapplication.OBD.ObdData.obdLiveData;
import com.example.a.myapplication.OBD.ObdData.obdStart;
import com.example.a.myapplication.OBD.obdConnection.Constants;
import com.example.a.myapplication.OBD.obdConnection.obdBlutoothManager;

import java.util.HashMap;
import java.util.Map;


// 26/3  this version is nicely working

/**
 * sending date perfectly
 * reciving data perfectly
 * next is what to send :D ?
 */


public class OldMainActivityobd extends AppCompatActivity {


    public Map<String, String> commandResult = new HashMap<String, String>();


    private final static int REQUEST_ENABLE_BT = 1;
    //    private obdBlutoothManager mobdBlutoothManager  = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    private String mConnectedDeviceName = null;
    //buffer to send data to obd by user ... to be deleted
    private StringBuffer mOutStringBuffer;

    private TextView Textview2;
    private ListView mConversationView;
    private ArrayAdapter<String> mConversationArrayAdapter;
    private TextView textView;


    private boolean isServiceBound;
    private SharedPreferences prefs;

  /*  private final Runnable mQueueCommands = new Runnable() {

        public void run() {
            mobdBlutoothManager.queueCommands();

            commandResult.clear();
            // run again in period defined in preferences as 4000ms
            new Handler().postDelayed(mQueueCommands, 4000);

        }};*/


    private obdStart mobdStart = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_main_activityobd);

        //check if device supports bluetooth
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "device dosenot support bluetooth", Toast.LENGTH_SHORT).show();
        }

        //check if bluetooth is enable
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        Textview2 = (TextView) findViewById(R.id.textView2);
        mConversationView = (ListView) findViewById(R.id.in);
        textView = (TextView) findViewById(R.id.edit_text_out);
        mobdStart = new obdStart();

        /*for testing only**/
        obdLiveData mobdLiveData = new obdLiveData();

        // mobdLiveData.setData(0,"main activity");


        onStart();
    }

    @Override
    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        // setupobdManager() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (!mobdStart.isRun()) {
            mobdStart.setupobdManager(getApplicationContext(), mHandler);
        }
        //setupobdManager();

    }

    @Override
    public void onResume() {
        super.onResume();

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mobdStart == null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            //if (mobdBlutoothManager.getState() == obdBlutoothManager.STATE_NONE) {
            /******for testing only  not used with obd realy *****/
            // Start the Bluetooth obd manager
            //    mobdBlutoothManager.start();
        }
    }


    /**
     * Sends a message.
     *
     * @param message A string of text to send.
     */
    /*private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mobdBlutoothManager.getState() != obdBlutoothManager.STATE_CONNECTED) {
            Toast.makeText(getApplicationContext(), "notConnectet inside sendMessage fn", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mobdBlutoothManager.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);

        }
    }*/
    public void on_click(View v) throws InterruptedException {

        // mobdBlutoothManager.UpdateHamdler();
        Intent i = new Intent(this, EngineDataActivity.class);
        startActivity(i);

        // i.putExtra("Inputstream",mobdBlutoothManager.getInstream());


        //  mobdBlutoothManager.queueCommands();
        // mobdBlutoothManager.issuecommands();
        // String message = textView.getText().toString();
        // sendMessage(message);


        /** print the log cat */
        //fakes delwa2ty 26/3
        /*
        try {
            Process process = Runtime.getRuntime().exec("logcat -d [MyBluetoothService]");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            StringBuilder log=new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line);
                log.append("\n");
            }
            TextView tv = (TextView)findViewById(R.id.tv);
            tv.setText(log.toString());
        } catch (IOException e) {
        }*/


    }


    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            OldMainActivityobd activity = OldMainActivityobd.this;
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case obdBlutoothManager.STATE_CONNECTED:
                            Textview2.setText("Connected  ");
                            break;
                        case obdBlutoothManager.STATE_CONNECTING:
                            Textview2.setText("Connectingggg  ");
                            break;
                        case obdBlutoothManager.STATE_LISTEN:
                        case obdBlutoothManager.STATE_NONE:
                            Textview2.setText("notConnected ");
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    mConversationArrayAdapter.add("Me:  " + writeMessage);
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != activity) {
                        Toast.makeText(activity, "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != activity) {
                        Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == this.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    mobdStart.setupobdManager(getApplicationContext(), mHandler);
                }

        }
    }


}

