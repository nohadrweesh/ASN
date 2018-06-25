package com.example.a.myapplication.OBD.obdApi.Commands.pressure;

/**
 * Created by Ahmed on 3/27/2018.
 * The Manifold Absolute Pressure (MAP) sensor is a key sensor because it senses engine load.
 * The sensor generates a signal that is proportional to the amount of vacuum in the intake manifold.
 * The engine computer then uses this information to adjust ignition timing and fuel enrichment.
 *
 * When the engine is working hard, intake vacuum drops as the throttle opens wide.
 * The engine sucks in more air, which requires more fuel to keep the air/fuel ratio in balance.
 * In fact, when the computer reads a heavy load signal from the MAP sensor,
 * it usually makes the fuel mixture go slightly richer than normal so the engine can produce more power.
 * At the same time, the computer will retard (back off) ignition timing slightly to prevent detonation (spark knock)
 * that can damage the engine and hurt performance.
 *
 * When conditions change and the vehicle is cruising along under light load, coasting or decelerating,
 * less power is needed from the engine. The throttle is not open very wide or may be closed causing
 * intake vacuum to increase. The MAP sensor senses this and the computer responds by leaning out the
 * fuel mixture to reduce fuel consumption and advances ignition timing to squeeze a little
 * more fuel economy out of the engine.
 *
 *
 * An intake manifold is a component that delivers either air or an air/fuel mixture to the cylinders
 * In fuel-injected engines, the intake manifold connects the throttle body to the intake ports.
 *
 * In addition to simply providing a path for air or air and fuel to move between a common
 * intake and the intake ports, intake manifolds also perform another important function. Due to the way that internal
 * combustion engines work, and the way that intake manifolds are “sealed” by the constant flow of air in one side and out the
 * other, the movement of the pistons in the engine is able to effectively create a partial vacuum inside the intake manifold.
 * When each piston moves downward on the intake stroke, it sucks air (or air and fuel) out of the intake manifold.
 * This creates a situation where the pressure inside the manifold is lower than the pressure outside of the manifold,
 * which results in a partial vacuum. This vacuum is then harnessed to perform a variety of different functions that
 * range all the way from climate controls to brake boosters.
 *
 *
 * Since manifold vacuum is produced by the normal operation of the engine, the level of vacuum can
 * also be used to diagnose certain engine problems. For instance, low vacuum might indicate a burnt valve,
 * incorrect valve or ignition timing, or a variety of other problems.
 *
 */

import com.example.a.myapplication.OBD.obdApi.enums.AvailableCommandNames;

/**
 * Intake Manifold Pressure
 *
 */
public class IntakeManifoldPressureCommand extends PressureCommand {

    /**
     * Default ctor.
     */
    public IntakeManifoldPressureCommand() {
        super("01 0B");
    }

    /**
     * Copy ctor.
     *
     * @param other object.
     */
    public IntakeManifoldPressureCommand(IntakeManifoldPressureCommand other) {
        super(other);
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.INTAKE_MANIFOLD_PRESSURE.getValue();
    }

}