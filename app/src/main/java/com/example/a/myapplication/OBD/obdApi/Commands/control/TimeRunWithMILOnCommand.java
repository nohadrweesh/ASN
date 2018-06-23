package com.example.a.myapplication.OBD.obdApi.Commands.control;

import com.example.a.myapplication.OBD.obdApi.ObdCommand;
import com.example.a.myapplication.OBD.obdApi.enums.AvailableCommandNames;

public class TimeRunWithMILOnCommand extends ObdCommand {
    private int value = 0;

    public TimeRunWithMILOnCommand() {
        super("01 4D");
    }

    public TimeRunWithMILOnCommand(ObdCommand other) {
        super(other);
    }

    @Override
    protected void performCalculations() {
        // ignore first two bytes [01 4D] of the response
        value = buffer.get(2) * 256 + buffer.get(3);
    }

    @Override
    public String getFormattedResult() {
        final String hh = String.format("%02d", value / 3600);
        final String mm = String.format("%02d", (value % 3600) / 60);
        final String ss = String.format("%02d", value % 60);
        return String.format("%s:%s:%s", hh, mm, ss);
    }

    @Override
    public String getCalculatedResult() {
        return String.valueOf(value);
    }

    @Override
    public String getName() {
        return AvailableCommandNames.TIME_TRAVELED_MIL_ON.getValue();
    }

    @Override
    public String getResultUnit() {
        return "s";
    }
}
