package com.example.a.myapplication.OBD.ObdData;

import com.example.a.myapplication.OBD.obdApi.Commands.SpeedCommand;
import com.example.a.myapplication.OBD.obdApi.ObdCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


/***
 * class to store the data of obd commands
 * memebers are static as shared memory , to be accessed from diffrent activites
 * methods are syncronized ensure only one thread at a time can acsess the data
 */


public class obdLiveData {


    private static LinkedList<String> data;
    private static Map<String, String> commandResult;

    private static final int commandsCount = 52;

    //set dafault value very far so commands for obd configuration will return in diffrent place
    private static int loopFirstNumber = 60;
    private static int loopLastNumber =commandsCount;
    private static ArrayList<ObdCommand> obdCommands;

    private static ArrayList<ObdCommand> prevobdCommands;
    private static int prevloopFirstNumber = 0;
    private static int prevloopLastNumber =commandsCount;


    private static boolean Server = false;

    /*
     * defult const
     * @param x no need to it just to diffrentiat betwaan the two constructor
     * use this constructor  when creating instance of this class for the first time
     * **/

    public obdLiveData(ObdCommand command) {
        data = new LinkedList<String>();
        commandResult = new HashMap<String, String>();

        obdCommands = new ArrayList<ObdCommand>();
        prevobdCommands = new ArrayList<ObdCommand>();

        // for initialization
        // to prevent obdBluetoothManager connected thread from crashing
        obdCommands.add(command);





    }

    /**
     * empty constructor
     * use this const across diffrent threads in order to create inestance of this object
     * to access the shared memory
     */

    public obdLiveData() {

    }

    public synchronized Map<String, String> getCommandResult() {
        return commandResult;
    }

    public synchronized LinkedList<String> getData() {
        return data;
    }

    public synchronized void setData(int pos, String s) {

        data.set(pos, s);

           if(pos == 52)
        {
            // server queue has run completely
            Server = true;

            obdCommands.clear();
            obdCommands.add(new SpeedCommand());
            loopFirstNumber = 39;
            loopLastNumber = 39;
        }
        else {

            Server =false;
        }
    }

    public int getCommandsCount() {
        return commandsCount;
    }

    // for initialsing only
    // my be removed later
    public synchronized void addData( String s)
    {
        data.add(s);
    }



    /**
     * methods for adding the desired commands to run
     * */

    public synchronized void setQueuCommands(ArrayList<ObdCommand> l)
    {
        this.prevobdCommands = obdCommands;
        this.obdCommands = l;
    }

    public synchronized  ArrayList<ObdCommand> getObdCommands() {

        return obdCommands;
    }

    public synchronized void setDataPlace(int firstNumber , int LastNumber)
    {
        prevloopFirstNumber = loopFirstNumber;
        prevloopLastNumber = loopLastNumber;

        loopFirstNumber = firstNumber;
        loopLastNumber = LastNumber;

    }

    public synchronized int getDataFirstPalce()
    {
        return loopFirstNumber;
    }

    public synchronized int getDataLastPlace() {
        return loopLastNumber;

    }

    public synchronized static boolean isServer() {
        return Server;
    }

    public synchronized void setServer(boolean x )
    {
        this.Server = x;
    }
    public synchronized void returnprevQueue()
    {
        obdCommands = prevobdCommands;
        loopFirstNumber = prevloopFirstNumber;
        loopLastNumber = prevloopLastNumber;
    }
}



