package com.example.a.myapplication.OBD.obdApi.Commands.engine;

import com.example.a.myapplication.OBD.obdApi.PercentageObdCommand;
import com.example.a.myapplication.OBD.obdApi.enums.AvailableCommandNames;

public class FuelInjectionTimingCommand extends PercentageObdCommand {
    public FuelInjectionTimingCommand() {
        super("01 5D");
    }

    public FuelInjectionTimingCommand(PercentageObdCommand other) {
        super(other);
    }

    /** {@inheritDoc} */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [hh hh] of the response
        int a = buffer.get(2);
        int b = buffer.get(3);
        percentage = (((a * 256.0f)+b) / 128.0f)-210.0f;
    }
    @Override
    public String getName() {
        return AvailableCommandNames.FUEL_INJECTION_TIMING.getValue();
    }
}
