package org.firstinspires.ftc.teamcode;

import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;

import java.util.function.Supplier;

public class TestBenchBotBlue {

    public enum ShotPos {
        FAR,
        CLOSE,
        EJECT
    }
    public Follower fw;
    public Intake intake;
    public static ShotPos shotPos;
    boolean runIntake;
    Shooter shooter;
    boolean runShooter;
    private PIDFCoefficients shooterCoefficients;
    double forwardPower, strafePower, turnPower;
    private boolean goTo;
    private Supplier<PathChain> pathChain;
    public TestBenchBotBlue(HardwareMap hwMap) {
        fw = Constants.createFollower(hwMap);
        fw.setStartingPose(
                Constants.blueStartPose
        );

        intake = new Intake(hwMap);
        shooterCoefficients = new PIDFCoefficients(0.1, 0.0, 0.003, 0.0);
        shooter = new Shooter(hwMap, shooterCoefficients);

        fw.startTeleopDrive(true);

        runIntake = false;
        runShooter = false;

        shotPos = ShotPos.FAR;

        pathChain = () -> fw.pathBuilder() //Lazy Curve Generation
                .addPath(new Path(new BezierLine(fw::getPose, new Pose(72,72))))
                .setHeadingInterpolation(HeadingInterpolator.linearFromPoint(fw::getHeading, Math.toRadians(45), 0.8))
                .build();

        goTo = false;
    }

    public void drivetrain(Gamepad gp) {
        fw.update();

        if (!goTo) {
            forwardPower = -gp.left_stick_y;
            if (Math.abs(forwardPower) < 0.05) {
                forwardPower = 0;
            }

            strafePower = -gp.left_stick_x;
            if (Math.abs(strafePower) < 0.05) {
                strafePower = 0;
            }

            turnPower = -gp.right_stick_x;
            if (Math.abs(turnPower) < 0.05) {
                turnPower = 0;
            }


            fw.setTeleOpDrive(
                    forwardPower,
                    strafePower,
                    turnPower,
                    true
            );
        }

        if (gp.dpadUpWasPressed()) {
            if (goTo) {
                fw.breakFollowing();
                fw.startTeleopDrive(true);
            }
            else {
                fw.followPath(pathChain.get());
            }

            goTo = !goTo;

        }
    }

    public void intake(Gamepad gp) {
        if (gp.aWasPressed()) {
            runIntake = !runIntake;
        }

        if (gp.b) {
            intake.eject();
        }
        else if (runIntake) {
            intake.intake();
        }
        else {
            intake.stop();
        }
    }

    public void shooter(Gamepad gp) {
        if (gp.dpad_left) {
            shooter.reload();
        }
        else {
            shooter.transfer();
        }


        if (gp.yWasPressed()) {
            runShooter = !runShooter;
        }

        if (runShooter) {
            shooter.midFieldShoot();
            shotPos = ShotPos.FAR;
        }
        else if (gp.x) {
            shooter.eject();
            shotPos = ShotPos.EJECT;
        }
        else {
            shooter.stop();
            shotPos = ShotPos.CLOSE;
        }
    }
}
