package com.example.a.myapplication.OBD.obdApi.Commands.protocol;

/**
 * Created by Ahmed on 3/27/2018.
 */

import com.example.a.myapplication.OBD.obdApi.enums.ObdProtocols;

/**
 * Select the protocol to use.
 *
 */
public class SelectProtocolCommand extends ObdProtocolCommand {

    private final ObdProtocols protocol;

    /**
     * <p>Constructor for SelectProtocolCommand.</p>
     *
     * @param protocol object.
     */
    public SelectProtocolCommand(final ObdProtocols protocol) {
        super("AT SP " + protocol.getValue());
        this.protocol = protocol;
    }

    /** {@inheritDoc} */
    @Override
    public String getFormattedResult() {
        return getResult();
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return "Select Protocol " + protocol.name();
    }

}