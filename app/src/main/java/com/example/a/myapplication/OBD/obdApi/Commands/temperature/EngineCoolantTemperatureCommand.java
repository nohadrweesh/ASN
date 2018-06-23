package com.example.a.myapplication.OBD.obdApi.Commands.temperature;

/**
 * Created by Ahmed on 3/27/2018.
 */

import com.example.a.myapplication.OBD.obdApi.enums.AvailableCommandNames;

/**
 * Engine Coolant Temperature.
 * Engine Coolant Temperature Sensor is a sensor that is screwed into the engine's
 * block or cylinder head and is used to determine the temperature of the engine coolant.
 * reading is sent to the vehicle's PCM/ECM (car's onboard computer) and is or can be used to
 * activate emission controls or turn the engine's cooling fan on.
 *
 *
 */
public class EngineCoolantTemperatureCommand extends TemperatureCommand {

    /**
     * <p>Constructor for EngineCoolantTemperatureCommand.</p>
     */
    public EngineCoolantTemperatureCommand() {
        super("01 05");
    }

    /**
     * <p>Constructor for EngineCoolantTemperatureCommand.</p>
     *
     * @param other  object.
     */
    public EngineCoolantTemperatureCommand(TemperatureCommand other) {
        super(other);
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.ENGINE_COOLANT_TEMP.getValue();
    }

}