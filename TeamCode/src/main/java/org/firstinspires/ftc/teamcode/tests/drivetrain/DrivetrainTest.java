package org.firstinspires.ftc.teamcode.tests.drivetrain;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.util.Settings;

@TeleOp(name="Drivetrain Test")
public class DrivetrainTest extends OpMode {

    public Follower follower;

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(Settings.Positions.Drivetrain.Blue.FAR_AUTO_START);
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

        telemetry.addData("Bot Strafe: ", follower.getPose().getX());
        telemetry.addData("Bot Forward: ", follower.getPose().getY());
        telemetry.addData("Bot Heading: ", follower.getHeading());
    }
}
