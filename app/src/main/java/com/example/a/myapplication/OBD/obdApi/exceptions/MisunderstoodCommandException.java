package com.example.a.myapplication.OBD.obdApi.exceptions;

/**
 * Created by Ahmed on 3/27/2018.
 */


/**
 * Thrown when there is a "?" message.
 *
 */
public class MisunderstoodCommandException extends ResponseException {
    /**
     * <p>Constructor for MisunderstoodCommandException.</p>
     */
    public MisunderstoodCommandException() {
        super("?");
    }

}
