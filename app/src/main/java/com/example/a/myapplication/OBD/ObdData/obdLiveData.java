package com.example.a.myapplication.OBD.ObdData;

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

    private static final int commandsCount = 30;

    /*
     * defult const
     * @param x no need to it just to diffrentiat betwaan the two constructor
     * use this constructor  when creating instance of this class for the first time
     * **/

    public obdLiveData(int x) {
        data = new LinkedList<String>();
        commandResult = new HashMap<String, String>();



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

}


