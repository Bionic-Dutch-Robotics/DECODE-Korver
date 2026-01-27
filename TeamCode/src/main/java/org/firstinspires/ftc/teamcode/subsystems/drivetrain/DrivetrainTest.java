package org.firstinspires.ftc.teamcode.subsystems.drivetrain;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.AllianceColor;

@TeleOp(name="DriTrain")
public class DrivetrainTest extends OpMode {
    private Drivetrain dt;
    @Override
    public void init() {
        dt = new Drivetrain(hardwareMap, new AllianceColor(AllianceColor.Selection.BLUE), new Pose(
                -gamepad1.left_stick_x,
                -gamepad1.left_stick_y,
                -gamepad1.right_stick_x
        ), new Pose(1.15, 1.15, 1.15));
    }

    @Override
    public void start() {
        dt.startTeleOpDrive();
    }
    @Override
    public void loop() {
        dt.update();
        dt.teleOpDrive(
                -gamepad1.left_stick_y,
                -gamepad1.left_stick_x,
                -gamepad1.right_stick_x
        );
    }
}
