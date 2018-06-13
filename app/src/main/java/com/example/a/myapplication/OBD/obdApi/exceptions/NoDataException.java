package com.example.a.myapplication.OBD.obdApi.exceptions;

/**
 * Created by Ahmed on 3/27/2018.
 */


/**
 * Thrown when there is a "NO DATA" message.
 *
 */
public class NoDataException extends ResponseException {

    /**
     * <p>Constructor for NoDataException.</p>
     */
    public NoDataException() {
        super("NO DATA");
    }

}