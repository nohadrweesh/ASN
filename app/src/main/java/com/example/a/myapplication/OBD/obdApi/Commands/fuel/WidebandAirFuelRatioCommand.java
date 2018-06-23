package com.example.a.myapplication.OBD.obdApi.Commands.fuel;

/**
 * Created by Ahmed on 3/27/2018.
 */

import com.example.a.myapplication.OBD.obdApi.ObdCommand;
import com.example.a.myapplication.OBD.obdApi.enums.AvailableCommandNames;

/**
 * Wideband AFR
 *
 * 01 34
 * Oxygen Sensor 1
 * AB: Fuel–Air Equivalence Ratio
 * CD: Current
 *
 * Oxygen sensors are usually located in the exhaust manifolds or exhaust piping.
 * They read data from the exhaust gasses leaving the engine.
 * This is helpful in determining if the engine is running correctly.
 * However, if you are trying to determine the AFR entering the engine,
 * which is what it sounds like, the oxygen sensors would not be very helpful.
 * A very high or very low reading could indicate that the intake AFR is not correct,
 * but getting an actual number would be difficult.
 *
 */
public class WidebandAirFuelRatioCommand extends ObdCommand {

    private float wafr = 0;

    /**
     * <p>Constructor for WidebandAirFuelRatioCommand.</p>
     */
    public WidebandAirFuelRatioCommand() {
        super("01 34");
    }

    /** {@inheritDoc} */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [01 44] of the response
        float A = buffer.get(2);
        float B = buffer.get(3);
        wafr = (((A * 256) + B) / 32768) * 14.7f;//((A*256)+B)/32768
    }

    /** {@inheritDoc} */
    @Override
    public String getFormattedResult() {
        return String.format("%.2f", getWidebandAirFuelRatio()) + ":1 AFR";
    }

    /** {@inheritDoc} */
    @Override
    public String getCalculatedResult() {
        return String.valueOf(getWidebandAirFuelRatio());
    }

    /**
     * <p>getWidebandAirFuelRatio.</p>
     *
     * @return a double.
     */
    public double getWidebandAirFuelRatio() {
        return wafr;
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.WIDEBAND_AIR_FUEL_RATIO.getValue();
    }

}