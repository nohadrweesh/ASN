package com.example.a.myapplication.OBD.obdApi.Commands.protocol;

/**
 * Created by Ahmed on 3/27/2018.
 */

/**
 * Warm-start the OBD connection.
 *
 * this command cause the elm327 to perform a complete reset similar to AT Z Command ,
 * but dose not include the power on LED Test
 */
public class ObdWarmstartCommand extends ObdProtocolCommand {

    /**
     * <p>Constructor for ObdWarmstartCommand.</p
     */
    public ObdWarmstartCommand() {
        super("AT WS");
    }

    /**
     * <p>Constructor for ObdWarmstartCommand.</p>
     *
     * @param other object.
     */
    public ObdWarmstartCommand(ObdWarmstartCommand other) {
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
        return "Warmstart OBD";
    }

}