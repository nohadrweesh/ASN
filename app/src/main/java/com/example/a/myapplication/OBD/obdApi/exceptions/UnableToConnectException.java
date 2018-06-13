package com.example.a.myapplication.OBD.obdApi.exceptions;

/**
 * Created by Ahmed on 3/27/2018.
 */

/**

 * Thrown when there is a "UNABLE TO CONNECT" message.

 *

 */

public class UnableToConnectException extends ResponseException {

    /**
     * <p>Constructor for UnableToConnectException.</p>
     */
    public UnableToConnectException() {
        super("UNABLE TO CONNECT");
    }
}