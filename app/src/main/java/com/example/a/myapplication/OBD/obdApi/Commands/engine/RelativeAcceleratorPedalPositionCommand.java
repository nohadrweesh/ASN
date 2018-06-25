package com.example.a.myapplication.OBD.obdApi.Commands.engine;

import com.example.a.myapplication.OBD.obdApi.PercentageObdCommand;
import com.example.a.myapplication.OBD.obdApi.enums.AvailableCommandNames;

public class RelativeAcceleratorPedalPositionCommand extends PercentageObdCommand {
    public RelativeAcceleratorPedalPositionCommand() {
        super("01 5A");
    }

    public RelativeAcceleratorPedalPositionCommand(PercentageObdCommand other) {
        super(other);
    }

    @Override
    public String getName() {
        return AvailableCommandNames.RELATIVE_ACC_PEDAL_POS.getValue();
    }
}
