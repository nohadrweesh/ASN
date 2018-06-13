package com.example.a.myapplication.OBD.obdApi.Commands.pressure;
/*
 *Created by Ahmed on 3/27/2018.
 */
/**
 * <p>FuelRailPressureCommand class.</p>
 *
 */

import com.example.a.myapplication.OBD.obdApi.enums.AvailableCommandNames;
public class FuelRailPressureCommand extends PressureCommand {

    /**
     * <p>Constructor for FuelRailPressureCommand.</p>
     */
    public FuelRailPressureCommand() {
        super("01 23");
    }

    /**
     * <p>Constructor for FuelRailPressureCommand.</p>
     *
     * @param other  object.
     */
    public FuelRailPressureCommand(FuelRailPressureCommand other) {
        super(other);
    }

    /**
     * {@inheritDoc}
     * <p>
     * TODO describe of why we multiply by 3
     */
    @Override
    protected final int preparePressureValue() {
        int a = buffer.get(2);
        int b = buffer.get(3);
        return ((a * 256) + b) * 10;
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.FUEL_RAIL_PRESSURE.getValue();
    }

}