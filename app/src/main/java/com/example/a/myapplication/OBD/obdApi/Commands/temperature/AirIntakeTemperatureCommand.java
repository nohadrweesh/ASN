package com.example.a.myapplication.OBD.obdApi.Commands.temperature;

/**
 *Created by Ahmed on 3/27/2018.
 */

import com.example.a.myapplication.OBD.obdApi.enums.AvailableCommandNames;

/**
 * Temperature of intake air.
 *
 */
public class AirIntakeTemperatureCommand extends TemperatureCommand {

    /**
     * <p>Constructor for AirIntakeTemperatureCommand.</p>
     */
    public AirIntakeTemperatureCommand() {
        super("01 0F");
    }

    /**
     * <p>Constructor for AirIntakeTemperatureCommand.</p>
     *
     * @param other object.
     */
    public AirIntakeTemperatureCommand(AirIntakeTemperatureCommand other) {
        super(other);
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.AIR_INTAKE_TEMP.getValue();
    }

}
