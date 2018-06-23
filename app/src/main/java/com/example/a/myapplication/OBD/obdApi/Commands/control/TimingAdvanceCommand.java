package com.example.a.myapplication.OBD.obdApi.Commands.control;

/**
 * Created by Ahmed on 3/27/2018.
 */

import com.example.a.myapplication.OBD.obdApi.enums.AvailableCommandNames;
import com.example.a.myapplication.OBD.obdApi.PercentageObdCommand;

/**
 * Timing Advance
 *
 */
public class TimingAdvanceCommand extends PercentageObdCommand {

    /**
     * <p>Constructor for TimingAdvanceCommand.</p>
     *
     * It refers to the spark igniting the air-fuel mixture in the cylinder in relation
     * to the piston position in crankshaft degrees. The timing of the spark must change
     * with engine conditions for the engine to run efficiently.
     */
    public TimingAdvanceCommand() {
        super("01 0E");
    }

    /**
     * <p>Constructor for TimingAdvanceCommand.</p>
     *
     * @param other a object.
     */
    public TimingAdvanceCommand(TimingAdvanceCommand other) {
        super(other);
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.TIMING_ADVANCE.getValue();
    }

}