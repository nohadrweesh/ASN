package com.example.a.myapplication.OBD.obdApi.Commands.protocol;

/**
 * Created by Ahmed on 3/27/2018.
 */

/**
 * Reset the OBD connection.
 *
 */
public class ObdResetCommand extends ObdProtocolCommand {

    /**
     * <p>Constructor for ObdResetCommand.</p>
     */
    public ObdResetCommand() {
        super("AT Z");
    }

    /**
     * <p>Constructor for ObdResetCommand.</p>
     *
     * @param other  object.
     */
    public ObdResetCommand(ObdResetCommand other) {
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
        return "Reset OBD";
    }

}
