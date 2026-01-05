package org.firstinspires.ftc.teamcode.tests;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.control.PIDFController;
import com.pedropathing.follower.Follower;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.shooter.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.transfer.Kicker;

import java.util.function.Supplier;

@Configurable
@TeleOp(name="Shooter")
public class ShooterRegressionTest extends OpMode {
    public static Shooter shooter;
    public Intake intake;
    public Kicker transfer;
    public Follower follower;
    public static com.pedropathing.control.PIDFController headingPid;
    public static double shooterTarget;
    public boolean runIntake, goToHeading;
    public Supplier<PathChain> toGoal;

    @Override
    public void init() {
        headingPid = new PIDFController(Constants.followerConstants.getCoefficientsHeadingPIDF());
        shooter = new Shooter(hardwareMap, Constants.shooterCoefficients);
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(Constants.redStartPose);
        intake = new Intake(hardwareMap);
        transfer = new Kicker(hardwareMap);
        runIntake = false;
        goToHeading = false;
    }

    @Override
    public void start() {
        follower.startTeleopDrive(false);
    }

    @Override
    public void loop() {
        {
            if (gamepad1.dpadUpWasPressed()) {
                runIntake = !runIntake;
            }
            if (runIntake) {
                intake.intake();
            } else {
                intake.stop();
            }
        }
        {
            if (gamepad1.a) {
                //transfer.reload();
            }
            else {
                //transfer.feed();
            }
        }


        follower.update();
        if (!goToHeading) {
            follower.setTeleOpDrive(
                    -gamepad1.left_stick_y,
                    -gamepad1.left_stick_x,
                    -gamepad1.right_stick_x,
                    true
            );
        }
        else {
            headingPid.updatePosition(follower.getHeading());
            headingPid.setTargetPosition(getRedTargetHeading(141-follower.getPose().getX(), 141-follower.getPose().getY()));
            follower.setTeleOpDrive(
                    -gamepad1.left_stick_y,
                    -gamepad1.left_stick_x,
                    headingPid.run(),
                    false
            );
        }
        if (gamepad1.leftBumperWasPressed()) {
            goToHeading = !goToHeading;

            if (!goToHeading) {
                follower.breakFollowing();
                follower.startTeleopDrive(false);
            }
        }

        shooter.update(getRegressionVelocity(
                getDistance(144-follower.getPose().getX(), 144-follower.getPose().getY())
        ));


        telemetry.addData("bot X", follower.getPose().getX());
        telemetry.addData("bot Y: ", follower.getPose().getY());
        telemetry.addData("bot Heading: ", follower.getHeading());
        telemetry.addData("Target Velocity: ", getRegressionVelocity(
                getDistance(144-follower.getPose().getX(), 144-follower.getPose().getY())));
        telemetry.update();
    }

    public static double getRegressionVelocity (double distance) {
        return -0.000724792 * Math.pow(distance, 2) + 1.10181 * distance + 100.38172;
    }
    public static double getDistance(double x, double y) {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }
    public static double getRedTargetHeading(double x, double y) {
            return Math.atan2(y,x)+Math.toRadians(90);
    }
}