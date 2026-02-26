package org.firstinspires.ftc.teamcode.tests.ivy;

import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.AllianceColor;
import com.pedropathing.ivy.bindings.Bindings;
import org.firstinspires.ftc.teamcode.util.MatchSettings;


@TeleOp(name="Ivy Test")
public class IvyTest extends OpMode {
    @Override
    public void init() {
        MatchSettings.initSelection(hardwareMap, new AllianceColor(AllianceColor.Selection.BLUE), gamepad1);
        MatchSettings.start();
        Scheduler.schedule(new IvyCommand());
        Scheduler.execute();
    }

    @Override
    public void loop() {
    }
}
