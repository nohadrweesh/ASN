package com.example.a.myapplication.OBD.obdApi.Commands.pressure;

/**
 * Created by Ahmed on 3/27/2018.
 */
import com.example.a.myapplication.OBD.obdApi.enums.AvailableCommandNames;
/**
  <p>FuelPressureCommand class.</p>
 *
 */
public class FuelPressureCommand extends PressureCommand {

    /**
     * <p>Constructor for FuelPressureCommand.</p>
     */
    public FuelPressureCommand() {
        super("01 0A");
    }

    /**
     * <p>Constructor for FuelPressureCommand.</p>
     *
     * @param other  object.
     */
    public FuelPressureCommand(FuelPressureCommand other) {
        super(other);
    }

    /**
     * {@inheritDoc}
     * <p>
     * TODO describe of why we multiply by 3
     */
    @Override
    protected final int preparePressureValue() {
        return buffer.get(2) * 3;
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.FUEL_PRESSURE.getValue();
    }
}