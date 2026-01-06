package org.firstinspires.ftc.teamcode.tests;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@TeleOp(name="Drivetrain Test")
public class DrivetrainTest extends OpMode {

    public Follower follower;

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(Constants.blueStartPose);
    }

    @Override
    public void start() {
        follower.startTeleopDrive(false);
        //follower.teleOpLock(true, true, true);
    }

    @Override
    public void loop() {
        follower.update();
        telemetry.update();
        follower.setTeleOpDrive(
                -gamepad1.left_stick_y,
                -gamepad1.left_stick_x,
                -gamepad1.right_stick_x,
                true
        );

        telemetry.addData("Bot X: ", follower.getPose().getX());
        telemetry.addData("Bot Y: ", follower.getPose().getY());
        telemetry.addData("Bot Heading: ", follower.getHeading());
    }
}
