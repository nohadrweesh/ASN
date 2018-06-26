package com.example.a.myapplication.OBD.obdApi.enums;

import java.util.HashMap;
import java.util.Map;

public enum CatalystBank {

    Catalyst_Temperature_Bank_1_Sensor_1(0x60,"Catalyst Temperature Bank 1, Sensor 1"),

    Catalyst_Temperature_Bank_2_Sensor_1(0x61,"Catalyst Temperature Bank 2, Sensor 1"),

    Catalyst_Temperature_Bank_1_Sensor_2(0x62,"Catalyst Temperature Bank 1, Sensor 2"),

    Catalyst_Temperature_Bank_2_Sensor_2(0x63,"Catalyst Temperature Bank 2, Sensor 2");



    /** Constant <code>map</code> */
    private static Map<Integer, CatalystBank> map = new HashMap<Integer, CatalystBank>();

    static {
        for (CatalystBank error : CatalystBank.values())
            map.put(error.getValue(), error);
    }

    private final int value;
    private final String bank;

    private CatalystBank(final int value, final String bank) {
        this.value = value;
        this.bank = bank;
    }



    public static CatalystBank fromValue(final int value) {
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
        return new String("01 " + value);
    }
}
