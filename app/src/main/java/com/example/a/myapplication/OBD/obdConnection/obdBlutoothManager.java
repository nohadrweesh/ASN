package com.example.a.myapplication.OBD.obdConnection;
/**
 * this class starts the connection
 * returns the bluetooth socket
 */

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.a.myapplication.OBD.ObdData.obdLiveData;
import com.example.a.myapplication.OBD.obdApi.Commands.protocol.EchoOffCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.protocol.LineFeedOffCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.protocol.ObdResetCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.protocol.SelectProtocolCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.protocol.TimeoutCommand;
import com.example.a.myapplication.OBD.obdApi.ObdCommand;
import com.example.a.myapplication.OBD.obdApi.enums.AvailableCommandNames;
import com.example.a.myapplication.OBD.obdApi.enums.ObdProtocols;
import com.example.a.myapplication.OBD.obdApi.exceptions.UnsupportedCommandException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Ahmed on 3/4/2018.
 */

public class obdBlutoothManager {


    private static final String TAG = obdBlutoothManager.class.getSimpleName();
    private BluetoothAdapter mBluetoothAdapter;
    private AcceptThread mAcceptThread;
    private ConnectThread mConnectThread;
    private final Handler mHandler;
    private ConnectedThread mConnectedThread;
    private int mState;
    private int mNewState;

    //my data holder for holding obd command results
    private obdLiveData obdLiveData = new obdLiveData();


    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device


    /*constructor
     *
     */
    public obdBlutoothManager(Context context, Handler handler) {

        //create the bluetooth adapter

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mState = STATE_NONE;
        mNewState = mState;
        mHandler = handler;
        Log.d(TAG, "new connection");
    }


    /**
     * "Hint: If you are connecting to a Bluetooth serial board then try using the
     * <p>
     *     * <p>
     * are connecting to an Android peer then please generate your own unique
     * <p>
     * UUID."
     */


    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String NAME = "ahmed";


    /**
     * Update UI title according to the current state of the chat connection
     */

    private synchronized void updateUserInterfaceTitle() {
        mState = getState();
        Log.d(TAG, "updateUserInterfaceTitle() " + mNewState + " -> " + mState);
        mNewState = mState;

        // Give the new state to the Handler so the UI Activity can update
        mHandler.obtainMessage(Constants.MESSAGE_STATE_CHANGE, mNewState, -1).sendToTarget();
    }

    /**
     * Return the current connection state.
     */
    public synchronized int getState() {
        return mState;
    }

    /**
     * Start the chat service. Specifically start AcceptThread to begin a
     * session in listening (server) mode. Called by the Activity onResume()
     */
    /********for testing only no use for this method on obd really ****/
    public synchronized void start() {
        Log.d(TAG, "start");

        // Cancel any thread attempting to make a connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Start the thread to listen on a BluetoothServerSocket
        if (mAcceptThread == null) {
            mAcceptThread = new AcceptThread();
            mAcceptThread.start();

        }   // Update UI title
        updateUserInterfaceTitle();
    }


    /**
     * Start the ConnectThread to initiate a connection to a remote device.
     *
     * @param device The BluetoothDevice to connect
     */

    public synchronized void connect(BluetoothDevice device) {
        Log.d(TAG, "connect to: " + device);

        // Cancel any thread attempting to make a connection
        if (mState == STATE_CONNECTING) {
            if (mConnectThread != null) {
                mConnectThread.cancel();
                mConnectThread = null;
            }
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
        // Update UI title
        updateUserInterfaceTitle();
    }

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     *
     * @param socket The BluetoothSocket on which the connection was made

     */
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
        Log.d(TAG, "connected fn , to the socket");
        // Cancel the thread that completed the connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Cancel the accept thread because we only want to connect to one device
        if (mAcceptThread != null) {
            mAcceptThread.cancel();
            mAcceptThread = null;
        }

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.start();

        // Send the name of the connected device back to the UI Activity
        Message msg = mHandler.obtainMessage(Constants.MESSAGE_DEVICE_NAME);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DEVICE_NAME, device.getName());
        msg.setData(bundle);
        mHandler.sendMessage(msg);
        // Update UI title
        updateUserInterfaceTitle();


    }

    /**
     * Write to the ConnectedThread in an unsynchronized manner
     *
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     */
    public void write(byte[] out) {
        // Create temporary object
        ConnectedThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (mState != STATE_CONNECTED) return;
            r = mConnectedThread;
        }
        // Perform the write unsynchronized
        r.write(out);
    }


    /**
     * Stop all threads
     */
    public synchronized void stop() {
        Log.d(TAG, "stop");

        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        if (mAcceptThread != null) {
            mAcceptThread.cancel();
            mAcceptThread = null;
        }
        mState = STATE_NONE;
        // Update UI title
        updateUserInterfaceTitle();
    }


    /*find all devices
     * @return list of all devices
     *
     * */
    public Set<BluetoothDevice> findAllDevices() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        /*this pice of code is to be used later
         * */
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); //MAC address
            }
        }
        return pairedDevices;
    }

    public void queueCommands() {
        mConnectedThread.queueCommands();
    }

    public void issuecommands() throws InterruptedException {
        mConnectedThread.executeQueue();
    }


    /*return inputstream and outstream
     *
     * we want them to be static so to be accessed from diffrent activetis
     * */

    private static InputStream in;
    private static OutputStream out;

    public InputStream getInstream() {

        return in;
    }

    public OutputStream getOutstream() {
        return out;
    }







    /*server class
     * * */


    private class AcceptThread extends Thread {

        private final BluetoothServerSocket mmServerSocket;


        public AcceptThread() {
            // Use a temporary object that is later assigned to mmServerSocket
            // because mmServerSocket is final.
            BluetoothServerSocket tmp = null;
            try {
                // MY_UUID is the app's UUID string, also used by the client code.
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "Socket's listen() method failed", e);
            }
            mmServerSocket = tmp;
            mState = STATE_LISTEN;
        }


        public void run() {
            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned.
            while (mState != STATE_CONNECTED) {
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    Log.e(TAG, "Socket's accept() method failed", e);
                    break;
                }

                if (socket != null) {
                    // A connection was accepted. Perform work associated with
                    // the connection in a separate thread.
                    switch (mState) {
                        case STATE_LISTEN:
                        case STATE_CONNECTING:
                            // Situation normal. Start the connected thread.
                            connected(socket, socket.getRemoteDevice());
                            break;
                        case STATE_NONE:
                        case STATE_CONNECTED:
                            // Either not ready or already connected. Terminate new socket.
                            try {
                                socket.close();
                            } catch (IOException e) {
                                Log.e(TAG, "Could not close unwanted socket", e);
                            }
                            break;
                    }
                    try {
                        mmServerSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }


        // Closes the connect socket and causes the thread to finish.


        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }


    /*the client class
     * **/


    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            BluetoothSocket tmp = null;
            mmDevice = device;

            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mmSocket = tmp;
            mState = STATE_CONNECTING;
        }

        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            mBluetoothAdapter.cancelDiscovery();

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket.connect();
                Log.e(TAG, "connected client socket successfull");
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                try {
                    mmSocket.close();
                    Log.e(TAG, "close the client socket");
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
                return;
            }

            // Reset the ConnectThread because we're done
            synchronized (obdBlutoothManager.this) {
                mConnectThread = null;
            }

            // Start the connected thread
            connected(mmSocket, mmDevice);
        }


        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }
    }


    /**
     * the class resposible for writing and reading from socket
     * */

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private byte[] mmBuffer; // mmBuffer store for the stream


        private Long queueCounter = 0L;
        private BlockingQueue<ObdCommandJob> jobsQueue = new LinkedBlockingQueue<>();


        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams; using temp objects because
            // member streams are final.
            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating input stream", e);
            }
            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating output stream", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
            mState = STATE_CONNECTED;
            //static in and out stream to be accessed from diffrent activetis
            in = mmInStream;
            out = mmOutStream;


            //  obd Let's configure the connection.
            queueJob(new ObdCommandJob(new ObdResetCommand()));
            //Below is to give the adapter enough time to reset before sending the commands, otherwise the first startup commands could be ignored.
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            queueJob(new ObdCommandJob(new EchoOffCommand()));

            /*
             * Will send second-time based on tests.
             *
             * TODO this can be done w/o having to queue jobs by just issuing
             * command.run(), command.getResult() and validate the result.
             */
            queueJob(new ObdCommandJob(new EchoOffCommand()));
            queueJob(new ObdCommandJob(new LineFeedOffCommand()));
            queueJob(new ObdCommandJob(new TimeoutCommand(62)));
            // Get protocol from preferences
            queueJob(new ObdCommandJob(new SelectProtocolCommand(ObdProtocols.valueOf("AUTO"))));

            // Job for returning dummy data
//            queueJob(new ObdCommandJob(new AmbientAirTemperatureCommand()));


        }




        public void executeQueue() throws InterruptedException {
            int j = obdLiveData.getDataFirstPalce();
            Log.d(TAG, "Executing queue..");
            // while (!Thread.currentThread().isInterrupted()) {
            while (!jobsQueue.isEmpty()) {
                ObdCommandJob job = null;
                try {
                    //remove .take() which blocks the thread with .poll(time,unit) or with .remove()
                    job = jobsQueue.remove();
                    // log job
                    Log.d(TAG, "Taking job[" + job.getId() + "] from queue..");
                    if (job.getState().equals(ObdCommandJob.ObdCommandJobState.NEW)) {
                        Log.d(TAG, "Job state is NEW. Run it..");
                        job.setState(ObdCommandJob.ObdCommandJobState.RUNNING);
                        if (mmSocket.isConnected()) {
                            job.getCommand().run(mmSocket.getInputStream(), mmSocket.getOutputStream());
                        } else {
                            job.setState(ObdCommandJob.ObdCommandJobState.EXECUTION_ERROR);
                            Log.e(TAG, "Can't run command on a closed socket.");
                        }
                    } else
                        // log not new job
                        Log.e(TAG, "Job state was not new, so it shouldn't be in queue. BUG ALERT!");
                } catch (InterruptedException i) {
                    Thread.currentThread().interrupt();
                } catch (UnsupportedCommandException u) {
                    if (job != null) {
                        job.setState(ObdCommandJob.ObdCommandJobState.NOT_SUPPORTED);
                    }
                    Log.d(TAG, "Command not supported. -> " + u.getMessage());
                } catch (IOException io) {
                    if (job != null) {
                        if (io.getMessage().contains("Broken pipe"))
                            job.setState(ObdCommandJob.ObdCommandJobState.BROKEN_PIPE);
                        else
                            job.setState(ObdCommandJob.ObdCommandJobState.EXECUTION_ERROR);
                    }
                    Log.e(TAG, "IO error. -> " + io.getMessage());
                } catch (Exception e) {
                    if (job != null) {
                        job.setState(ObdCommandJob.ObdCommandJobState.EXECUTION_ERROR);
                    }
                    Log.e(TAG, "Failed to run command. -> " + e.getMessage());
                }

                if (job != null) {
                    ObdCommandJob job2 = job;

                    String cmdName = job2.getCommand().getName();
                    String cmdResult = "";
                    String cmdID = LookUpCommand(cmdName);
                    if (job.getState().equals(ObdCommandJob.ObdCommandJobState.EXECUTION_ERROR)) {
                        cmdResult = job.getCommand().getResult();
                    } else if (job.getState().equals(ObdCommandJob.ObdCommandJobState.BROKEN_PIPE)) {
                        cmdResult = "stop live data";
                        //stop live data
                        cancel();
                    } else if (job.getState().equals(ObdCommandJob.ObdCommandJobState.NOT_SUPPORTED)) {
                        cmdResult = "status_obd_no_support";
                    } else {
                        cmdResult = job.getCommand().getFormattedResult();
                    }

                    String msg = cmdName + ": " + cmdResult;
                    obdLiveData.setData(j, msg);
                    j = (j + 1); // % obdLiveData.getDataLastPlace();

                    /*Message
                            readMsg = mHandler.obtainMessage(
                            Constants.MESSAGE_READ,msg.getBytes().length, -1,
                            msg.getBytes());
                    readMsg.sendToTarget();*/

                }


            }


        }


        public void queueJob(ObdCommandJob job) {
            queueCounter++;
            job.setId(queueCounter);
            try {
                jobsQueue.put(job);
            } catch (InterruptedException e) {
                job.setState(ObdCommandJob.ObdCommandJobState.QUEUE_ERROR);
            }
        }

        public String LookUpCommand(String txt) {
            for (AvailableCommandNames item : AvailableCommandNames.values()) {
                if (item.getValue().equals(txt)) return item.name();
            }
            return txt;
        }


        private void queueCommands() {

            ArrayList<ObdCommand> commands = obdLiveData.getObdCommands();

            //for (ObdCommand Command : ObdConfig.getCommands()) {
              for( ObdCommand Command : commands){
                queueJob(new ObdCommandJob(Command));
            }
        }





        /*this was supposed to be running to listen for incoming messages
         * now we will use it as separet thread to run the commands
         * */

        public void run() {

            /* we frist execute queue in order to run the configration commandes
            *    that was added in the constructor of Connected Thread class
            **/
            try {
                executeQueue();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // start issuing commanads to ECU and store results

            while (mState == STATE_CONNECTED) {
                queueCommands();
                try {
                    executeQueue();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            /*mmBuffer = new byte[1024];
            int numBytes; // bytes returned from read()
            // Keep listening to the InputStream until an exception occurs.
            while (mState == STATE_CONNECTED) {
                try {
                    // Read from the InputStream.
                    numBytes = mmInStream.read(mmBuffer);
                    // Send the obtained bytes to the UI activity.
                    Message
                            readMsg = mHandler.obtainMessage(
                            Constants.MESSAGE_READ, numBytes, -1,
                            mmBuffer);
                    readMsg.sendToTarget();
                } catch (IOException e) {
                    Log.d(TAG, "Input stream was disconnected", e);
                    break;
                }
            }*/
        }

        // Call this from the main activity to send data to the remote device.
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);

                // Share the sent message with the UI activity.
                Message writtenMsg = mHandler.obtainMessage(
                        Constants.MESSAGE_WRITE, -1, -1, bytes
                );
                writtenMsg.sendToTarget();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when sending data", e);

                // Send a failure message back to the activity.
                Message writeErrorMsg =
                        mHandler.obtainMessage(Constants.MESSAGE_TOAST);
                Bundle bundle = new Bundle();
                bundle.putString("toast",
                        "Couldn't send data to the other device");
                writeErrorMsg.setData(bundle);
                mHandler.sendMessage(writeErrorMsg);
            }
        }

        // Call this method from the main activity to shut down the connection.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }

        }

    }
}



