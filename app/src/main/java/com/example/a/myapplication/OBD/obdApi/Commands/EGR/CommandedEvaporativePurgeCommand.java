package com.example.a.myapplication.OBD.obdApi.Commands.EGR;

import com.example.a.myapplication.OBD.obdApi.PercentageObdCommand;
import com.example.a.myapplication.OBD.obdApi.enums.AvailableCommandNames;

public class CommandedEvaporativePurgeCommand extends PercentageObdCommand {
    public CommandedEvaporativePurgeCommand() {
        super("01 2E");
    }

    public CommandedEvaporativePurgeCommand(PercentageObdCommand other) {
        super(other);
    }

    @Override
    public String getName() {
        return AvailableCommandNames.COMMANDED_EVAPORATIVE_PURGE.getValue();
    }
}
