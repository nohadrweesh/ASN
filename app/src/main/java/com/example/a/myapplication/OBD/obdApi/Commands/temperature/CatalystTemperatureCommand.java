package com.example.a.myapplication.OBD.obdApi.Commands.temperature;

import com.example.a.myapplication.OBD.obdApi.enums.CatalystBank;

public class CatalystTemperatureCommand extends TemperatureCommand{

    private final CatalystBank bank;

    public CatalystTemperatureCommand(final CatalystBank bank) {
        super(bank.buildObdCommand());
        this.bank = bank;
    }

    public CatalystTemperatureCommand() {
        this(CatalystBank.Catalyst_Temperature_Bank_1_Sensor_1);
    }


    protected void performCalculations() {
        // ignore first two bytes [hh hh] of the response
        int a = buffer.get(2);
        int b = buffer.get(3);
        temperature = (((a * 256.f) + b) /10.f )-40;

    }


    public final String getBank() {
        return bank.getBank();
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return bank.getBank();
    }

}
