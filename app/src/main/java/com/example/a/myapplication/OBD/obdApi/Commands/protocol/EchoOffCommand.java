package com.example.a.myapplication.OBD.obdApi.Commands.protocol;

/**
 *Created by Ahmed on 3/27/2018.
 */
/**
 * Turn-off echo.
 * E0: echo off       E1: echo on
 * this commands controll weather or not the characters received on the RS232 port are echoed (retransmitted)
 * back to the host computer
 * Character echo can be used to confirm that characters sent to the ELM327 were received correctly
 * the default is E1 (echo on)
 *
 */
public class EchoOffCommand extends ObdProtocolCommand {

    /**
     * <p>Constructor for EchoOffCommand.</p>
     */

    public EchoOffCommand() {
        super("AT E0");
    }

    /**
     * <p>Constructor for EchoOffCommand.</p>
     *
     * @param other object.
     */
    public EchoOffCommand(EchoOffCommand other) {
        super(other);
    }

    /** {@inheritDoc} */
    @Override
    public String getFormattedResult() {
        return getResult();
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return "Echo Off";
    }

}