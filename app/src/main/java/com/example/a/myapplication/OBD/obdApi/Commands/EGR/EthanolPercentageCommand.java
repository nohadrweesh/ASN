package com.example.a.myapplication.OBD.obdApi.Commands.EGR;

import com.example.a.myapplication.OBD.obdApi.PercentageObdCommand;
import com.example.a.myapplication.OBD.obdApi.enums.AvailableCommandNames;

public class EthanolPercentageCommand extends PercentageObdCommand {
    public EthanolPercentageCommand() {
        super("01 52");
    }

    public EthanolPercentageCommand(PercentageObdCommand other) {
        super(other);
    }

    @Override
    public String getName() {
        return AvailableCommandNames.ETHANOL_PERCENT.getValue();
    }
}
