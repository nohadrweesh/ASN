package com.example.a.myapplication.OBD.obdApi.exceptions;

/**
 * Created by Ahmed on 3/27/2018.
 */

/**
 * Thrown when there are no numbers in the response and they are expected
 *
 */
public class NonNumericResponseException extends RuntimeException {
    /**
     * <p>Constructor for NonNumericResponseException.</p>
     *@param message a {@link java.lang.String} object.
     */
    public NonNumericResponseException(String message) {
        super("Error reading response: " + message);
    }
}
