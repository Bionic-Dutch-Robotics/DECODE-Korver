package org.firstinspires.ftc.teamcode.teleops;

import static org.firstinspires.ftc.teamcode.util.Hardware.dt;
import static org.firstinspires.ftc.teamcode.util.Hardware.intake;
import static org.firstinspires.ftc.teamcode.util.Hardware.shooter;
import static org.firstinspires.ftc.teamcode.util.Hardware.transfer;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.AllianceColor;
import org.firstinspires.ftc.teamcode.util.Hardware;
import org.firstinspires.ftc.teamcode.util.MatchSettings;
import org.firstinspires.ftc.teamcode.util.control.bindings.MainController;
import org.firstinspires.ftc.teamcode.util.control.bindings.SubController;

@TeleOp(name="Blue TeleOp FR")
public class RealBlueTele extends OpMode {
    private MainController chetan;
    private SubController atharv;
    @Override
    public void init() {
        MatchSettings.initSelection(hardwareMap, new AllianceColor(AllianceColor.Selection.BLUE), gamepad1);
        chetan = new MainController(dt, intake, gamepad1, new AllianceColor(AllianceColor.Selection.BLUE));
        atharv = new SubController(transfer, shooter, gamepad2);
    }

    @Override
    public void init_loop() {
        telemetry.update();
        MatchSettings.refreshMotif(telemetry);
    }

    @Override
    public void start() {
        MatchSettings.start();
    }

    @Override
    public void loop() {
       dt.update();

       chetan.update();
       atharv.update();
    }

    @Override
    public void stop() {
        Hardware.stop();
    }
}