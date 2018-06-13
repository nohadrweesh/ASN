package com.example.a.myapplication.OBD.obdApi.Commands.control;

import com.example.a.myapplication.OBD.obdApi.enums.AvailableCommandNames;
import com.example.a.myapplication.OBD.obdApi.ObdCommand;

/**
 * Created by Ahmed on 3/27/2018.*/

public class IgnitionMonitorCommand extends ObdCommand {

    private boolean ignitionOn = false;

    /**
     * Default ctor.
     */
    public IgnitionMonitorCommand() {
        super("AT IGN");
    }

    /**
     * Copy ctor.
     *
     * @param other a {@link IgnitionMonitorCommand} object.
     */
    public IgnitionMonitorCommand(IgnitionMonitorCommand other) {
        super(other);
    }

    @Override
    protected void performCalculations() {
        final String result = getResult();
        ignitionOn = result.equalsIgnoreCase("ON");
    }

    @Override
    public String getFormattedResult() {
        return getResult();
    }

    @Override
    public String getName() {
        return AvailableCommandNames.IGNITION_MONITOR.getValue();
    }

    @Override
    public String getCalculatedResult() {
        return getResult();
    }

    @Override
    protected void fillBuffer() {
    }

    public boolean isIgnitionOn() {
        return ignitionOn;
    }
}