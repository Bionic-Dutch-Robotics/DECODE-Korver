package org.firstinspires.ftc.teamcode.tests.ivy;

import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.AllianceColor;
import org.firstinspires.ftc.teamcode.util.MatchSettings;


@TeleOp(name="Ivy Test")
public class IvyTest extends OpMode {
    @Override
    public void init() {
        Scheduler.reset();
        MatchSettings.initSelection(hardwareMap, new AllianceColor(AllianceColor.Selection.BLUE), gamepad1);
        Scheduler.schedule(new IvyCommand());
    }

    @Override
    public void start() {
        MatchSettings.start();
    }

    @Override
    public void loop() {
        Scheduler.execute();
    }
}
