package com.example.a.myapplication.OBD.obdApi.Commands.EGR;

import com.example.a.myapplication.OBD.obdApi.PercentageObdCommand;
import com.example.a.myapplication.OBD.obdApi.enums.AvailableCommandNames;

public class EGRErrorCommand extends PercentageObdCommand {
    public EGRErrorCommand() {
        super("01 2D");
    }

    public EGRErrorCommand(PercentageObdCommand other) {
        super(other);
    }

    @Override
    protected void performCalculations() {
        // ignore first two bytes [hh hh] of the response
        percentage = ((buffer.get(2) * 100.0f) / 128.0f) - 100.0f;
    }

    @Override
    public String getName() {
        return AvailableCommandNames.EGR_ERROR.getValue();
    }
}
