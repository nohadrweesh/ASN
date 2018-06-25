package com.example.a.myapplication.OBD.obdApi.Commands.EGR;

import com.example.a.myapplication.OBD.obdApi.PercentageObdCommand;
import com.example.a.myapplication.OBD.obdApi.enums.AvailableCommandNames;

public class ActualEnginePercentTorqueCommand extends PercentageObdCommand {
    public ActualEnginePercentTorqueCommand() {
        super("01 62");
    }

    public ActualEnginePercentTorqueCommand(PercentageObdCommand other) {
        super(other);
    }


    @Override
    public String getName() {
        return AvailableCommandNames.ACTUAL_ENGINE_PERCENT_TOURQ.getValue();
    }
}
