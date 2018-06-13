package com.example.a.myapplication.OBD.obdApi.Commands.engine;

/**
 * Created by Ahmed on 3/27/2018.
 */

import com.example.a.myapplication.OBD.obdApi.enums.AvailableCommandNames;
import com.example.a.myapplication.OBD.obdApi.PercentageObdCommand;

/**
 * Calculated Engine Load value.
 *
 */
public class LoadCommand extends PercentageObdCommand {

    /**
     *<p>Constructor for LoadCommand.</p>
     */
    public LoadCommand() {
        super("01 04");
    }

    /**
     * <p>Constructor for LoadCommand.</p>
     *
     *@param other a object.
     */
    public LoadCommand(LoadCommand other) {
        super(other);
    }

    /*
     * (non-Javadoc)
     *
     * @see pt.lighthouselabs.obd.commands.ObdCommand#getName()
     */
    /**
     *{@inheritDoc}
     */
    @Override
    public String getName() {
        return AvailableCommandNames.ENGINE_LOAD.getValue();
    }

}