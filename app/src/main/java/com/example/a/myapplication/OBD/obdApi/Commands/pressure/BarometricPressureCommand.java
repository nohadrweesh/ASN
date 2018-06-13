package com.example.a.myapplication.OBD.obdApi.Commands.pressure;

/**
 * Created by Ahmed on 3/27/2018.
 */

import com.example.a.myapplication.OBD.obdApi.enums.AvailableCommandNames;

/**
 * Barometric pressure.
 *
 */
public class BarometricPressureCommand extends PressureCommand {

    /**
     * <p>Constructor for BarometricPressureCommand.</p>
     */
    public BarometricPressureCommand() {
        super("01 33");
    }

    /**
     * <p>Constructor for BarometricPressureCommand.</p>
     *
     * @param other object.
     */
    public BarometricPressureCommand(PressureCommand other) {
        super(other);
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.BAROMETRIC_PRESSURE.getValue();
    }

}
