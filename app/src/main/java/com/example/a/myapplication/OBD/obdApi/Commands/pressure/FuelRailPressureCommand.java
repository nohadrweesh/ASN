package com.example.a.myapplication.OBD.obdApi.Commands.pressure;
/*
 *Created by Ahmed on 3/27/2018.
 */
/**
 * <p>FuelRailPressureCommand class.</p>
 *
 *  On a car with fuel injection there can be two fuel pumps,
 * one high-pressure and one low-pressure.
 * The low-pressure pump moves the gas from the tank up to the
 * high pressure pump. The high pressure pump then raises the pressure to a
 * level that will be useful to the injectors.
 * The two PIDs reflect those two different systems.
 * PID  Description        Min       Max      Units
 * 0A   Fuel pressure        0       765      kPa (gauge)
 * 22   Fuel rail pressure   0     5,177.265  kPa
 * 23   Fuel rail pressure   0   655,350      kPa
 *
 *
 * These pressures are all controlled by regulators so that any of these PIDs should be fairly
 * constant and not affected by load in a well running engine.
 * Low fuel pressure would be the result of failure or clog in the fuel system.
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