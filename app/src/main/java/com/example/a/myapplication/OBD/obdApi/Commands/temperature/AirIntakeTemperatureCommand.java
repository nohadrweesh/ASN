package com.example.a.myapplication.OBD.obdApi.Commands.temperature;

/**
 *Created by Ahmed on 3/27/2018.
 *
 * The Intake Air Temperature sensor (IAT) monitors the temperature
 * of the air entering the engine. The engine computer (PCM) needs this information to estimate
 * air density so it can balance air air/fuel mixture. Colder air is more dense than hot air,
 * so cold air requires more fuel to maintain the same air/fuel ratio. The PCM changes the air/fuel ratio
 * by changing the length (on time) of the injector pulses.
 *
 *
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
