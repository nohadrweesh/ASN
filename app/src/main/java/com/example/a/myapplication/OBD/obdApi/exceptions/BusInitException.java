package com.example.a.myapplication.OBD.obdApi.exceptions;

/**
 * Created by Ahmed on 3/27/2018.
 */


/**
 * Thrown when there is a "BUS INIT... ERROR" message
 *
 */

public class BusInitException extends ResponseException {
        /**
         * <p>Constructor for BusInitException.</p>
         */
        public BusInitException() {
            super("BUS INIT... ERROR");
        }

    }

