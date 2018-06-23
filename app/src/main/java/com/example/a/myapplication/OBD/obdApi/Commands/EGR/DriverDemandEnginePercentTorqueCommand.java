package com.example.a.myapplication.OBD.obdApi.Commands.EGR;

import com.example.a.myapplication.OBD.obdApi.PercentageObdCommand;
import com.example.a.myapplication.OBD.obdApi.enums.AvailableCommandNames;

public class DriverDemandEnginePercentTorqueCommand extends PercentageObdCommand {
    public DriverDemandEnginePercentTorqueCommand() {
        super("01 61");
    }

    public DriverDemandEnginePercentTorqueCommand(PercentageObdCommand other) {
        super(other);
    }

    /** {@inheritDoc} */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [hh hh] of the response
        percentage = buffer.get(2) - 125;
    }



    @Override
    public String getName() {
        return AvailableCommandNames.DRIVE_DEMAND_ENGINE_PERCENT_TOURQE.getValue();
    }
}
