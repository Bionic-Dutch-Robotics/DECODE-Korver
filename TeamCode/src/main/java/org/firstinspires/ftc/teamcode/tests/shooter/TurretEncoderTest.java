package org.firstinspires.ftc.teamcode.tests.shooter;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.AllianceColor;

import static org.firstinspires.ftc.teamcode.util.Hardware.dt;
import static org.firstinspires.ftc.teamcode.util.Hardware.shooter;
import org.firstinspires.ftc.teamcode.util.MatchSettings;

@TeleOp(name="Turret Encoder TEst")
public class TurretEncoderTest extends OpMode {
    @Override
    public void init() {
        MatchSettings.initSelection(hardwareMap, new AllianceColor(AllianceColor.Selection.BLUE), gamepad1);
        MatchSettings.start();
    }

    @Override
    public void loop() {
        telemetry.update();
        telemetry.addData("Encoder", shooter.turret.turret.getCurrentPosition());

        shooter.turret.loop(dt.getPose());
    }
}
