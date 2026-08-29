package org.firstinspires.ftc.teamcode.teleops;

import static org.firstinspires.ftc.teamcode.util.Hardware.dt;
import static org.firstinspires.ftc.teamcode.util.Hardware.intake;
import static org.firstinspires.ftc.teamcode.util.Hardware.shooter;
import static org.firstinspires.ftc.teamcode.util.Hardware.transfer;

import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.teleops.bindings.MainController;
import org.firstinspires.ftc.teamcode.teleops.bindings.SubController;
import org.firstinspires.ftc.teamcode.util.AllianceColor;
import org.firstinspires.ftc.teamcode.util.Hardware;
import org.firstinspires.ftc.teamcode.util.MatchSettings;

@TeleOp(name="Red TeleOp FR")
public class RealRedTele extends OpMode {
    private MainController chetan;
    private SubController atharv;
    @Override
    public void init() {
        Scheduler.reset();
        MatchSettings.initSelection(hardwareMap, new AllianceColor(AllianceColor.Selection.RED), gamepad1);
        chetan = new MainController(dt, intake, gamepad1, new AllianceColor(AllianceColor.Selection.RED));
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
        dt.startTeleOpDrive();
    }

    @Override
    public void loop() {
        dt.update();
        chetan.runDrive();
        atharv.runShooter(dt.follower);

        Scheduler.execute();
    }

    @Override
    public void stop() {
        Hardware.stop();
    }
}