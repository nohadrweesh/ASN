package com.example.a.myapplication.OBD.obdApi.Commands.EGR;

import com.example.a.myapplication.OBD.obdApi.PercentageObdCommand;
import com.example.a.myapplication.OBD.obdApi.enums.AvailableCommandNames;

public class HybridBatteryPackRemainingLifeCommand extends PercentageObdCommand {
    public HybridBatteryPackRemainingLifeCommand() {
        super("01 5B");
    }

    public HybridBatteryPackRemainingLifeCommand(PercentageObdCommand other) {
        super(other);
    }

    @Override
    public String getName() {
        return AvailableCommandNames.HYBRID_BATTERY_PACK_REMAINING_LIFE.getValue();
    }
}
