package com.example.a.myapplication.OBD.obdApi.enums;

/**
 * Created by Ahmed on 3/27/2018.
 */

import java.util.HashMap;
import java.util.Map;
/**
 * Select one of the Fuel Trim percentage banks to access.
 *fuel trims are the percentage of change in fuel over time.
 * For the engine to operate properly, the air:fuel ratio needs to stay within a small window of 14.7:1.
 *
 *
 *If the o2 sensors inform the engine computer that the exhaust mixture is lean, the
 * computer adds fuel by lengthening injector pulse, or "on-time", to compensate.
 * Conversely, if the o2 sensors inform the computer that the exhaust is rich,
 * the computer shortens injector pulse, adding less fuel to compensate in order to bring
 * the rich condition down.
 *
 *
 * This change in fuel being added or taken away is called Fuel Trim.
 * Really, the oxygen sensors are what drive the fuel trim readings. Changes in o2 sensor voltages cause
 * a direct change in fuel. The short term fuel trim (STFT) refers to immediate changes in fuel occurring
 * several times per second. The long term fuel trims (LTFT) are driven by the short term fuel trims.
 * LTFT refers to changes in STFT but averaged over a longer period of time.
 * A negative fuel trim percentage indicates a taking away of fuel while a
 * positive percentage indicates an adding of fuel
 */

public enum FuelTrim {

    SHORT_TERM_BANK_1(0x06, "Short Term Fuel Trim Bank 1"),
    LONG_TERM_BANK_1(0x07, "Long Term Fuel Trim Bank 1"),
    SHORT_TERM_BANK_2(0x08, "Short Term Fuel Trim Bank 2"),
    LONG_TERM_BANK_2(0x09, "Long Term Fuel Trim Bank 2");

    /** Constant <code>map</code> */
    private static Map<Integer, FuelTrim> map = new HashMap<>();

    static {
        for (FuelTrim error : FuelTrim.values())
            map.put(error.getValue(), error);
    }

    private final int value;
    private final String bank;

    private FuelTrim(final int value, final String bank) {
        this.value = value;
        this.bank = bank;
    }

    /**
     * <p>fromValue.</p>
     *
     * @param value a int.
     * @return  object.
     */
    public static FuelTrim fromValue(final int value) {
        return map.get(value);
    }

    /**
     * <p>Getter for the field <code>value</code>.</p>
     *
     * @return a int
     */
    public int getValue() {
        return value;
    }

    /**
     * <p>Getter for the field <code>bank</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getBank() {
        return bank;
    }

    /**
     * <p>buildObdCommand.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public final String buildObdCommand() {
        return new String("01 0" + value);
    }

}