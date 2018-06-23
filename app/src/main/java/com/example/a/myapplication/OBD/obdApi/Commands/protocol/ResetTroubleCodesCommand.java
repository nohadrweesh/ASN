package com.example.a.myapplication.OBD.obdApi.Commands.protocol;

/**
 * Created by Ahmed on 3/27/2018.
 */

import com.example.a.myapplication.OBD.obdApi.ObdCommand;

/**
 * Reset trouble codes.
 *
 * Clear Diagnostic Trouble Codes and stored values
 *
 */
public class ResetTroubleCodesCommand extends ObdCommand {

    /*
     * <p>Constructor for ResetTroubleCodesCommand.</p>
     */
    public ResetTroubleCodesCommand() {
        super("04");
    }

    /** {@inheritDoc} */
    @Override
    protected void performCalculations() {

    }

    /** {@inheritDoc} */
    @Override
    public String getFormattedResult() {
        return getResult();
    }

    /** {@inheritDoc} */
    @Override
    public String getCalculatedResult() {
        return getResult();
    }


    /** {@inheritDoc} */
    @Override
    public String getName() {
        return getResult();
    }

}
