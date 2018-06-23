package com.example.a.myapplication.OBD.obdApi.Commands.pressure;

import com.example.a.myapplication.OBD.obdApi.enums.AvailableCommandNames;

public class AbsoluteEvapSystemVaporPressureCommand extends PressureCommand {
    public AbsoluteEvapSystemVaporPressureCommand() {
        super("01 53");
    }

    public AbsoluteEvapSystemVaporPressureCommand(PressureCommand other) {
        super(other);
    }

    @Override
    protected final int preparePressureValue() {
        int a = buffer.get(2);
        int b = buffer.get(3);
        return ((a * 256) + b) * 200;
    }


    @Override
    public String getName() {
        return AvailableCommandNames.ABS_EVAP_SYS_VAP_PRESSURE.getValue();
    }
}
