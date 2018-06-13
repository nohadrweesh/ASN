package com.example.a.myapplication.OBD.obdApi.exceptions;

/**
 * Created by Ahmed on 3/27/2018.
 */


/**
 * Thrown when there is "ERROR" in the response
 *
 */

public class UnknownErrorException extends ResponseException {

    /**
     * <p>Constructor for UnknownErrorException.</p>
     */
    public UnknownErrorException() {
        super("ERROR");
    }
}
