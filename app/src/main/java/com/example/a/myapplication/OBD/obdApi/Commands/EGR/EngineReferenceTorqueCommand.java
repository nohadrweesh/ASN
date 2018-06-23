package com.example.a.myapplication.OBD.obdApi.Commands.EGR;

import com.example.a.myapplication.OBD.obdApi.ObdCommand;
import com.example.a.myapplication.OBD.obdApi.enums.AvailableCommandNames;

public class EngineReferenceTorqueCommand extends ObdCommand {
    private int tourq = -1;
    public EngineReferenceTorqueCommand() {
        super("01 63");
    }

    public EngineReferenceTorqueCommand(ObdCommand other) {
        super(other);
    }

    @Override
    protected void performCalculations() {
        // ignore first two bytes [41 0C] of the response((A*256)+B)
        tourq = (buffer.get(2) * 256 + buffer.get(3));
    }

    @Override
    public String getFormattedResult() {
        return String.format("%d%s",tourq, getResultUnit());
    }


    /** {@inheritDoc} */
    @Override
    public String getResultUnit() {
        return "Nm";
    }

    @Override
    public String getCalculatedResult() {
        return String.valueOf(tourq);
    }

    @Override
    public String getName() {
        return AvailableCommandNames.ENGINE_REFRENCE_TOURQUE.getValue();
    }
}
