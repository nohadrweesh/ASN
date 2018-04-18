package com.example.a.myapplication.OBD.ObdConfigration;

import com.example.a.myapplication.OBD.obdApi.Commands.SpeedCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.control.DistanceMILOnCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.control.DtcNumberCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.control.EquivalentRatioCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.control.ModuleVoltageCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.control.TimingAdvanceCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.control.TroubleCodesCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.control.VinCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.engine.LoadCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.engine.MassAirFlowCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.engine.OilTempCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.engine.RPMCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.engine.RuntimeCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.engine.ThrottlePositionCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.fuel.AirFuelRatioCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.fuel.ConsumptionRateCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.fuel.FindFuelTypeCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.fuel.FuelLevelCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.fuel.FuelTrimCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.fuel.WidebandAirFuelRatioCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.pressure.BarometricPressureCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.pressure.FuelPressureCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.pressure.FuelRailPressureCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.pressure.IntakeManifoldPressureCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.temperature.AirIntakeTemperatureCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.temperature.AmbientAirTemperatureCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.temperature.EngineCoolantTemperatureCommand;
import com.example.a.myapplication.OBD.obdApi.ObdCommand;
import com.example.a.myapplication.OBD.obdApi.enums.FuelTrim;

import java.util.ArrayList;

/**
 * Created by Ahmed on 3/27/2018.
 */

public class ObdConfig {

    public static ArrayList<ObdCommand> getCommands() {
        ArrayList<ObdCommand> cmds = new ArrayList<>();

         // Control
        cmds.add(new ModuleVoltageCommand());
        cmds.add(new EquivalentRatioCommand());
        cmds.add(new RPMCommand());
        cmds.add(new DistanceMILOnCommand());
        cmds.add(new DtcNumberCommand());
        cmds.add(new TimingAdvanceCommand());
        cmds.add(new TroubleCodesCommand());
        cmds.add(new VinCommand());


        // Engine

        cmds.add(new LoadCommand());
        cmds.add(new RPMCommand());
        cmds.add(new RuntimeCommand());
        cmds.add(new MassAirFlowCommand());
        cmds.add(new ThrottlePositionCommand());


        // Fuel
        cmds.add(new FindFuelTypeCommand());
        cmds.add(new ConsumptionRateCommand());
        // cmds.add(new AverageFuelEconomyObdCommand());
        //cmds.add(new FuelEconomyCommand());
        cmds.add(new FuelLevelCommand());
        // cmds.add(new FuelEconomyMAPObdCommand());
        // cmds.add(new FuelEconomyCommandedMAPObdCommand());
        cmds.add(new FuelTrimCommand(FuelTrim.LONG_TERM_BANK_1));
        cmds.add(new FuelTrimCommand(FuelTrim.LONG_TERM_BANK_2));
        cmds.add(new FuelTrimCommand(FuelTrim.SHORT_TERM_BANK_1));
        cmds.add(new FuelTrimCommand(FuelTrim.SHORT_TERM_BANK_2));
        cmds.add(new AirFuelRatioCommand());
        cmds.add(new WidebandAirFuelRatioCommand());
        cmds.add(new OilTempCommand());


        // Pressure
        cmds.add(new BarometricPressureCommand());
        cmds.add(new FuelPressureCommand());
        cmds.add(new FuelRailPressureCommand());
        cmds.add(new IntakeManifoldPressureCommand());


        // Temperature
        cmds.add(new AirIntakeTemperatureCommand());
        cmds.add(new AmbientAirTemperatureCommand());
        cmds.add(new EngineCoolantTemperatureCommand());



        // Misc
        cmds.add(new SpeedCommand());



        return cmds;

    }
}
