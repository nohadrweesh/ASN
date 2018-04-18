package com.example.a.myapplication.OBD.obdApi.exceptions;

/**
 * Created by Ahmed on 3/27/2018.
 */


/**
 * Sent when there is a "STOPPED" message.
 *
 */

public class StoppedException extends ResponseException {
    /**
     * <p>Constructor for StoppedException.</p>
     */
    public StoppedException() {
        super("STOPPED");
    }
}
