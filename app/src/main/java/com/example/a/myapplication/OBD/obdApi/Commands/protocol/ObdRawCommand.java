package com.example.a.myapplication.OBD.obdApi.Commands.protocol;

/**
 * Created by Ahmed on 3/27/2018.
 */
/*
 * This class allows for an unspecified command to be sent.
 */
public class ObdRawCommand extends ObdProtocolCommand {

    /**
     * <p>Constructor for ObdRawCommand.</p>
     *
     * @param command a {@link java.lang.String} object.
     */
    public ObdRawCommand(String command) {
        super(command);
    }

    /** {@inheritDoc} */
    @Override
    public String getFormattedResult() {
        return getResult();
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return "Custom command " + getCommandPID();
    }

}