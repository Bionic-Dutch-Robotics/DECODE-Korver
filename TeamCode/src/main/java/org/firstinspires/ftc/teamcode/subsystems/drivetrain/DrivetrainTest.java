package org.firstinspires.ftc.teamcode.subsystems.drivetrain;

import static org.firstinspires.ftc.teamcode.util.Hardware.dt;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.AllianceColor;
import org.firstinspires.ftc.teamcode.util.MatchSettings;

@TeleOp(name="DriTrain")
public class DrivetrainTest extends OpMode {
    @Override
    public void init() {
        MatchSettings.initSelection(hardwareMap, new AllianceColor(AllianceColor.Selection.BLUE), gamepad1);
        MatchSettings.start();
    }

    @Override
    public void start() {
        dt.startTeleOpDrive();
    }
    @Override
    public void loop() {
        telemetry.update();
        telemetry.addData("X", dt.follower.getPose().getX());
        telemetry.addData("Y", dt.follower.getPose().getY());
        telemetry.addData("Heading", dt.follower.getHeading());
        dt.update();
        dt.teleOpDrive(
                -gamepad1.left_stick_y,
                -gamepad1.left_stick_x,
                -gamepad1.right_stick_x
        );
    }
}
