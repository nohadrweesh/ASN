package com.example.a.myapplication.OBD.obdApi.Commands.protocol;

/**
 * Created by Ahmed on 3/27/2018.
 */
/**
 * Turn-off headers.
 *
 * H0: headers off       H1: headers on
 * this commands controll weather or not additional (headers) bytes of information are shown in the response from the vehicle
 * these are not shown by the elm327 but may be of interest (especially if you receive multiple
 * responses and wish to determine what module they were from
 *
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
