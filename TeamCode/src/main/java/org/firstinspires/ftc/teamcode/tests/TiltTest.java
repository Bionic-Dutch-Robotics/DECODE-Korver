package org.firstinspires.ftc.teamcode.tests;

import static org.firstinspires.ftc.teamcode.util.Hardware.dt;
import static org.firstinspires.ftc.teamcode.util.Hardware.shooter;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.AllianceColor;
import org.firstinspires.ftc.teamcode.util.MatchSettings;

@TeleOp(name="TilT Test")
public class TiltTest extends OpMode {
    private double servoPos = 0.0;
    private final AllianceColor alliance = new AllianceColor(AllianceColor.Selection.BLUE);
    @Override
    public void init() {
        MatchSettings.initSelection(hardwareMap, new AllianceColor(AllianceColor.Selection.BLUE), gamepad1);
        MatchSettings.start();
        dt.startTeleOpDrive();
    }

    @Override
    public void loop() {
        telemetry.update();
        telemetry.addData("Servo Pos", servoPos);
        if (gamepad1.aWasPressed()) {
            servoPos += 0.01;
        }
        else if (gamepad1.bWasPressed()) {
            servoPos -= 0.02;
        }

        shooter.tilt.setTilt(
                servoPos
        );
        shooter.flywheel.adaptive(dt.follower.getPose().getX(), dt.follower.getPose().getY());
        shooter.turret.loop(dt.getPose());
        dt.teleOpDrive(
                -gamepad1.left_stick_y,
                -gamepad1.left_stick_x,
                -gamepad1.right_stick_x
        );
        dt.update();
    }
}
