package com.example.a.myapplication.OBD.obdApi.Commands.pressure;

import com.example.a.myapplication.OBD.obdApi.enums.AvailableCommandNames;

/*
*
* The EVAP system is a fully closed system designed to maintain stable fuel tank pressures without allowing
* fuel vapors to escape to the atmosphere.
* Fuel vapor is normally created in the fuel tank as a result of evaporation.
* It is then transferred to the EVAP system charcoal canister when tank vapor pressures become excessive.
* When operating conditions can tolerate additional enrichment, these stored fuel vapors are purged
* into the intake manifold and added to the incoming air/fuel mixture.
 * */
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
