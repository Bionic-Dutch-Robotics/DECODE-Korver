package org.firstinspires.ftc.teamcode.tests;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.control.PIDFController;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Transfer;

import java.util.function.Supplier;

@Configurable
@TeleOp(name="Shooter")
public class shootertest extends OpMode {
    public static Shooter shooter;
    public Intake intake;
    public Transfer transfer;
    public Follower follower;
    public static com.pedropathing.control.PIDFCoefficients pidfCoefficients = new PIDFCoefficients(0.005, 0,0.000011,0);
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
        transfer = new Transfer(hardwareMap);
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
                transfer.reload();
            }
            else {
                transfer.feed();
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
            headingPid.setTargetPosition(getRedTargetHeading(144-follower.getPose().getX(), 144-follower.getPose().getY()));
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

        shooter.update(this.getRegressionVelocity(
                this.getDistance(144-follower.getPose().getX(), 144-follower.getPose().getY())));


        telemetry.addData("bot X", follower.getPose().getX());
        telemetry.addData("bot Y: ", follower.getPose().getY());
        telemetry.addData("bot Heading: ", follower.getHeading());
        telemetry.addData("Target Velocity: ", this.getRegressionVelocity(
                this.getDistance(144-follower.getPose().getX(), 144-follower.getPose().getY())));
        telemetry.update();
    }

    public double getRegressionVelocity (double distance) {
        return 0.000616291 * Math.pow(distance, 2) + 2.0547 * distance;
    }
    public double getDistance(double x, double y) {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }
    public double getRedTargetHeading(double x, double y) {
        return Math.atan2(y,x)+Math.toRadians(90);
    }
}