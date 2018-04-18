package com.example.a.myapplication.OBD.obdApi.Commands.protocol;

/**
 *Created by Ahmed on 3/27/2018.
 */
/**
 * Turn-off echo.
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