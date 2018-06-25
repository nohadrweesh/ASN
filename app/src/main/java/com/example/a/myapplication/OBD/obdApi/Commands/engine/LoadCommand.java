package com.example.a.myapplication.OBD.obdApi.Commands.engine;

/**
 * Created by Ahmed on 3/27/2018.
 */

import com.example.a.myapplication.OBD.obdApi.enums.AvailableCommandNames;
import com.example.a.myapplication.OBD.obdApi.PercentageObdCommand;

/**
 * Calculated Engine Load value.
 *
 * According to SAE International SAE J1979 / ISO 15031-5
 *(dated: 2014-08-11), calculated engine load is calculated by the following equation
 * Where:
 - STP = Standard Temperature and Pressure = 25 °C, 29.92 in Hg BARO,
 - SQRT = square root
 - WOT = wide open throttle
 - AAT = Ambient Air Temperature (in °C)
 *
 * There isn't a single sensor it uses to figure this out. For a gasoline (or spark ignition) engine,
 * it utilizes the Air Intake Sensor (IAT), Manifold Absolute Pressure (MAP)
 * sensor, Throttle Position Sensor (TPS), and Engine Coolant Temperature (ECT) sensor
 * to do calculations and to discover if the engine is at the ready point to make the calculations.
 *
 */
public class LoadCommand extends PercentageObdCommand {

    /**
     *<p>Constructor for LoadCommand.</p>
     */
    public LoadCommand() {
        super("01 04");
    }

    /**
     * <p>Constructor for LoadCommand.</p>
     *
     *@param other a object.
     */
    public LoadCommand(LoadCommand other) {
        super(other);
    }

    /*
     * (non-Javadoc)
     *
     * @see pt.lighthouselabs.obd.commands.ObdCommand#getName()
     */
    /**
     *{@inheritDoc}
     */
    @Override
    public String getName() {
        return AvailableCommandNames.ENGINE_LOAD.getValue();
    }

}