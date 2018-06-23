package com.example.a.myapplication.OBD.obdApi.Commands.TroubleCodes;

/**
 * Created by Ahmed on 3/27/2018.
 */

import com.example.a.myapplication.OBD.obdApi.*;
import com.example.a.myapplication.OBD.obdApi.enums.AvailableCommandNames;

/**
 * This command will for now read MIL (check engine light) state and number of
 * diagnostic trouble codes currently flagged in the ECU.
 * <p>
 * Perhaps in the future we'll extend this to read the 3rd, 4th and 5th bytes of
 * the response in order to store information about the availability and
 * completeness of certain on-board tests.
 *
 */
public class DtcNumberCommand extends ObdCommand {

    private int codeCount = 0;
    private boolean milOn = false;

    /**
     * Default ctor.
     */
    public DtcNumberCommand() {
        super("01 01");
    }

    /**
     * Copy ctor.
     *
     *
     */
    public DtcNumberCommand(DtcNumberCommand other) {
        super(other);
    }

    /** {@inheritDoc} */
    /***
     *Mode 1 PID 01[edit]
     A request for this PID returns 4 bytes of data, labeled A B C and D.
     The first byte(A) contains two pieces of information. Bit A7 (MSB of byte A, the first byte)
     indicates whether or not the MIL (check engine light) is illuminated.
     Bits A6 through A0 represent the number of diagnostic trouble codes currently flagged in the ECU.
     The second, third, and fourth bytes(B, C and D) give information about the availability and
     completeness of certain on-board tests. Note that test availability is indicated by set (1)
     bit and completeness is indicated by reset (0) bit.
     */

    @Override
    protected void performCalculations() {
        // ignore first two bytes [hh hh] of the response
        final int mil = buffer.get(2);
        milOn = (mil & 0x80) == 128;
        codeCount = mil & 0x7F;
    }

    /**
     * <p>getFormattedResult.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getFormattedResult() {
        final String res = milOn ? "MIL is ON" : "MIL is OFF";
        return res + codeCount + " codes";
    }

    /** {@inheritDoc} */
    @Override
    public String getCalculatedResult() {
        return String.valueOf(codeCount);
    }

    /**
     * <p>getTotalAvailableCodes.</p>
     *
     * @return the number of trouble codes currently flaggd in the ECU.
     */
    public int getTotalAvailableCodes() {
        return codeCount;
    }

    /**
     * <p>Getter for the field <code>milOn</code>.</p>
     *
     * @return the state of the check engine light state.
     */
    public boolean getMilOn() {
        return milOn;
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.DTC_NUMBER.getValue();
    }

}
