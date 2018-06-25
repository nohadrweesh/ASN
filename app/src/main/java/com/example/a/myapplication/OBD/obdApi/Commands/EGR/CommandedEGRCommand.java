package com.example.a.myapplication.OBD.obdApi.Commands.EGR;

import com.example.a.myapplication.OBD.obdApi.PercentageObdCommand;
import com.example.a.myapplication.OBD.obdApi.enums.AvailableCommandNames;

public class CommandedEGRCommand extends PercentageObdCommand{


    public CommandedEGRCommand() {
        super("01 2C");
    }

    public CommandedEGRCommand(PercentageObdCommand other) {
        super(other);
    }

    @Override
    public String getName() {
        return AvailableCommandNames.COMMANDED_EGR.getValue();
    }
}
