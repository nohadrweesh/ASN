package com.example.a.myapplication.OBD.obdApi.Commands.protocol;

/**
 * Created by Ahmed on 3/27/2018.
 */
/*
 * Turns off line-feed.
 *
 */
public class LineFeedOffCommand extends ObdProtocolCommand {

    /**
     * <p>Constructor for LineFeedOffCommand.</p>
     */
    public LineFeedOffCommand() {
        super("AT L0");
    }

    /**
     * <p>Constructor for LineFeedOffCommand.</p>
     *
     * @param other object.
     */
    public LineFeedOffCommand(LineFeedOffCommand other) {
        super(other);
    }

    /** {@inheritDoc} */
    @Override
    public String getFormattedResult() {
        return getResult();
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return "Line Feed Off";
    }

}