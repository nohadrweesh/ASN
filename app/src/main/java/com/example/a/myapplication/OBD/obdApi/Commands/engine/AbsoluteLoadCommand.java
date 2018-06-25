package com.example.a.myapplication.OBD.obdApi.Commands.engine;

/**
 * Created by Ahmed on 3/27/2018.
 */

import com.example.a.myapplication.OBD.obdApi.enums.AvailableCommandNames;
import com.example.a.myapplication.OBD.obdApi.PercentageObdCommand;

/**
 * <p>AbsoluteLoadCommand class.</p>
 *
 *LOAD_ABS = [air mass (g / intake stroke)] / [1.184 (g / intake stroke) *
 *cylinder displacement in liters]
 *Where:
 *- STP = Standard Temperature and Pressure = 25 °C, 29.92 in Hg (101.3 kPa)
 *BARO
 *- WOT = wide open throttle
 *
 * As you can probably see, this equation relies on the flow of air and basically engine displacement.
 * As it states in the body, this correlates with volumetric efficiency (how completely a cylinder fills with
 * air on the intake stroke) at WOT. This variable can be read from the ECU on PID $43. It is only required
 * by the standard on spark ignition systems.
 *
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