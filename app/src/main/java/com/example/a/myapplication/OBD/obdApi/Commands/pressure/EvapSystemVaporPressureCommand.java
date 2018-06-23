package com.example.a.myapplication.OBD.obdApi.Commands.pressure;

import com.example.a.myapplication.OBD.obdApi.Commands.pressure.PressureCommand;
import com.example.a.myapplication.OBD.obdApi.enums.AvailableCommandNames;

public class EvapSystemVaporPressureCommand extends PressureCommand {
    public EvapSystemVaporPressureCommand() {
        super("01 32");
    }

    public EvapSystemVaporPressureCommand(PressureCommand other) {
        super(other);
    }

    @Override
    protected final int preparePressureValue() {
        int a = buffer.get(2);
        int b = buffer.get(3);
        return ((a * 256) + b) /4000;
    }

    @Override
    public String getName() {
        return AvailableCommandNames.EVAP_SYSTEM_VAPOR_PRESSURE.getValue();
    }


}
