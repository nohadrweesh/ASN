package com.example.a.myapplication.OBD.obdApi.Commands.engine;

/**
 * Created by Ahmed on 3/27/2018.
 */

import com.example.a.myapplication.OBD.obdApi.enums.AvailableCommandNames;
import com.example.a.myapplication.OBD.obdApi.PercentageObdCommand;

/**
 * <p>AbsoluteLoadCommand class.</p>
 *
 */
public class AbsoluteLoadCommand extends PercentageObdCommand {

    /**
     * Default ctor.
     */
    public AbsoluteLoadCommand() {
        super("01 43");
    }

    /**
     * Copy ctor.
     *
     * @param other a object.
     */
    public AbsoluteLoadCommand(AbsoluteLoadCommand other) {
        super(other);
    }

    /** {@inheritDoc} */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [hh hh] of the response
        int a = buffer.get(2);
        int b = buffer.get(3);
        percentage = (a * 256 + b) * 100 / 255;
    }

    /**
     * <p>getRatio.</p>
     *
     * @return a double.
     */
    public double getRatio() {
        return percentage;
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.ABS_LOAD.getValue();
    }

}