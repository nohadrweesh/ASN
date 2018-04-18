package com.example.a.myapplication.OBD.obdApi.Commands.protocol;

/**
 * Created by Ahmed on 3/27/2018.
 */
/**
 * Turn-off headers.
 *
 */
public class HeadersOffCommand extends ObdProtocolCommand {

    /*
     * <p>Constructor for HeadersOffCommand.</p>
     */
    public HeadersOffCommand() {
        super("ATH0");
    }

    /**
     * <p>Constructor for HeadersOffCommand.</p>
     *
     * @param other  object.
     */
    public HeadersOffCommand(HeadersOffCommand other) {
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
        return "Headers disabled";
    }


}
