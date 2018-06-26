package com.example.a.myapplication.OBD.ObdConfigration;

import com.example.a.myapplication.OBD.obdApi.Commands.EGR.ActualEnginePercentTorqueCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.EGR.CommandedEGRCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.EGR.CommandedEvaporativePurgeCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.EGR.DriverDemandEnginePercentTorqueCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.EGR.EGRErrorCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.EGR.EngineReferenceTorqueCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.EGR.EthanolPercentageCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.EGR.HybridBatteryPackRemainingLifeCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.SpeedCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.TroubleCodes.DtcNumberCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.TroubleCodes.PendingTroubleCodesCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.TroubleCodes.PermanentTroubleCodesCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.TroubleCodes.TroubleCodesCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.control.DistanceMILOnCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.control.DistanceSinceCCCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.control.EquivalentRatioCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.control.ModuleVoltageCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.control.TimeRunWithMILOnCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.control.TimeSinceTroubleCodesClearedCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.control.TimingAdvanceCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.control.VinCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.engine.AbsoluteLoadCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.engine.FuelInjectionTimingCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.engine.LoadCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.engine.MassAirFlowCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.engine.OilTempCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.engine.RPMCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.engine.RelativeAcceleratorPedalPositionCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.engine.RuntimeCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.engine.ThrottlePositionCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.fuel.AirFuelRatioCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.fuel.ConsumptionRateCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.fuel.FindFuelTypeCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.fuel.FuelLevelCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.fuel.FuelTrimCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.fuel.WidebandAirFuelRatioCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.pressure.AbsoluteEvapSystemVaporPressureCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.pressure.BarometricPressureCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.pressure.EvapSystemVaporPressureCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.pressure.FuelPressureCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.pressure.FuelRailPressureCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.pressure.IntakeManifoldPressureCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.temperature.AirIntakeTemperatureCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.temperature.AmbientAirTemperatureCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.temperature.CatalystTemperatureCommand;
import com.example.a.myapplication.OBD.obdApi.Commands.temperature.EngineCoolantTemperatureCommand;
import com.example.a.myapplication.OBD.obdApi.ObdCommand;
import com.example.a.myapplication.OBD.obdApi.enums.CatalystBank;
import com.example.a.myapplication.OBD.obdApi.enums.FuelTrim;

import java.util.ArrayList;

/**
 * Created by Ahmed on 3/27/2018.
 */

public class ObdConfig {

    public static ArrayList<ObdCommand> getCommands() {
        ArrayList<ObdCommand> cmds = new ArrayList<>();

        // Control

        /*0--->7 **/

        cmds.add(new VinCommand());
        cmds.add(new ModuleVoltageCommand());

        cmds.add(new TimingAdvanceCommand());
        cmds.add(new EquivalentRatioCommand());

        //cmds.add(new DtcNumberCommand());

        cmds.add(new DistanceMILOnCommand());
        cmds.add(new TimeRunWithMILOnCommand());


        cmds.add(new DistanceSinceCCCommand());
        cmds.add(new TimeSinceTroubleCodesClearedCommand());


      //  cmds.add(new TroubleCodesCommand());


        // Engine
        /**8---->16***/
        //AbsoluteLoadCommand
        //
        cmds.add(new RPMCommand());
        cmds.add(new RuntimeCommand());

        cmds.add(new OilTempCommand());
        cmds.add(new ThrottlePositionCommand());

        cmds.add(new LoadCommand());
        cmds.add(new AbsoluteLoadCommand());

        cmds.add(new MassAirFlowCommand());
        cmds.add(new FuelInjectionTimingCommand());

        cmds.add(new RelativeAcceleratorPedalPositionCommand());


        // Fuel
        /**17----> 25**/
        //

        cmds.add(new FuelLevelCommand());
        cmds.add(new FindFuelTypeCommand());


        cmds.add(new ConsumptionRateCommand());
        cmds.add(new AirFuelRatioCommand());

        cmds.add(new FuelTrimCommand(FuelTrim.LONG_TERM_BANK_1));
        cmds.add(new FuelTrimCommand(FuelTrim.LONG_TERM_BANK_2));

        cmds.add(new FuelTrimCommand(FuelTrim.SHORT_TERM_BANK_1));
        cmds.add(new FuelTrimCommand(FuelTrim.SHORT_TERM_BANK_2));

        cmds.add(new WidebandAirFuelRatioCommand());


        // cmds.add(new AverageFuelEconomyObdCommand());
        //cmds.add(new FuelEconomyCommand());
        //cmds.add(new FuelEconomyMAPObdCommand());
        // cmds.add(new FuelEconomyCommandedMAPObdCommand());




        // Pressure
        /**26---->31****/

        cmds.add(new FuelPressureCommand());
        cmds.add(new FuelRailPressureCommand());

        cmds.add(new BarometricPressureCommand());
        cmds.add(new IntakeManifoldPressureCommand());

        cmds.add(new AbsoluteEvapSystemVaporPressureCommand());
        cmds.add(new EvapSystemVaporPressureCommand());

        // Temperature
        /**32--->38***/
        cmds.add(new EngineCoolantTemperatureCommand());
        cmds.add(new AirIntakeTemperatureCommand());

        cmds.add(new CatalystTemperatureCommand(CatalystBank.Catalyst_Temperature_Bank_1_Sensor_1));
        cmds.add(new CatalystTemperatureCommand(CatalystBank.Catalyst_Temperature_Bank_2_Sensor_1));

        cmds.add(new CatalystTemperatureCommand(CatalystBank.Catalyst_Temperature_Bank_1_Sensor_2));
        cmds.add(new CatalystTemperatureCommand(CatalystBank.Catalyst_Temperature_Bank_2_Sensor_2));

        cmds.add(new AmbientAirTemperatureCommand());


        // Misc
        /****/
        //cmds.add(new SpeedCommand());


        //Expert
        /*39-->47**/
        cmds.add(new SpeedCommand());
        cmds.add(new HybridBatteryPackRemainingLifeCommand());


        cmds.add(new ActualEnginePercentTorqueCommand());
        cmds.add(new DriverDemandEnginePercentTorqueCommand());

        cmds.add(new EngineReferenceTorqueCommand());
        cmds.add(new CommandedEvaporativePurgeCommand());

        cmds.add(new CommandedEGRCommand());
        cmds.add(new EGRErrorCommand());

        cmds.add(new EthanolPercentageCommand());


        //Trouble Codes
        /*48-->51**/

        // /48
        cmds.add(new DtcNumberCommand());

        //49
        cmds.add(new TroubleCodesCommand());

        //50
        cmds.add(new PendingTroubleCodesCommand());

        //51
        cmds.add(new PermanentTroubleCodesCommand());


        // 52 duplicate command for speed
        cmds.add(new SpeedCommand());
        return cmds;

    }
}
