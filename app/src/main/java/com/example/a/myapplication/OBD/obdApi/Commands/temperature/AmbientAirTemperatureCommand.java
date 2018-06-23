package com.example.a.myapplication.OBD.obdApi.Commands.temperature;

/**
 * Created by Ahmed on 3/27/2018.
 */

import com.example.a.myapplication.OBD.obdApi.enums.AvailableCommandNames;

/**
 * Ambient Air Temperature
 *
 * The Ambient Air Temperature (AAT) sensor converts ambient air temperature into
 * an electrical signal for the Powertrain Control Module (PCM). This input is used to modify air
 * conditioning system operation and display outside air temperature.
 *
 * The PCM receives this input and possibly two more; Intake Air Temperature (IAT) and Engine Coolant
 * Temperature Sensor (ECT). The PCM looks at the AAT sensor voltage and compares it to the IAT/ECT
 * sensor readings when the ignition switch is first turned on after a long cool down period.
 * This code is set if these inputs vary too much. It also looks at the voltage signals from these
 * sensors to determine if they are correct when the engine is fully warmed up. This code is usually
 * set due to electrical issues, but mechanical faults should not be dismissed. These mechanical issues
 * would include improper mounting location of the sensor, not mounting the sensor
 * (leaving it hanging by the wiring harness), etc.
 *
 */
public class AmbientAirTemperatureCommand extends TemperatureCommand {

    /**
     * <p>Constructor for AmbientAirTemperatureCommand.</p>
     */
    public AmbientAirTemperatureCommand() {
        super("01 46");
    }

    /*

     * <p>Constructor for AmbientAirTemperatureCommand.</p>
     *
     * @param other a {@link com.github.pires.obd.commands.temperature.TemperatureCommand} object.
     */
    public AmbientAirTemperatureCommand(TemperatureCommand other) {
        super(other);
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.AMBIENT_AIR_TEMP.getValue();
    }

}