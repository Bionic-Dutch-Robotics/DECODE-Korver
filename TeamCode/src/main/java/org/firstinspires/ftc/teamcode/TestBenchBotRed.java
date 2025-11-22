package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.pedroPathing.Constants.shooterCoefficients;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.configurables.annotations.IgnoreConfigurable;
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
import org.firstinspires.ftc.teamcode.subsystems.Transfer;

import java.util.function.Supplier;

@Configurable
public class TestBenchBotRed {

    @IgnoreConfigurable
    public enum ShotPos {
        FAR,
        CLOSE,
        EJECT
    }
    public Follower fw;
    public Intake intake;
    public Transfer transfer;
    public static ShotPos shotPos;
    boolean runIntake;
    Shooter shooter;
    boolean runShooter;
    double forwardPower, strafePower, turnPower;
    private boolean goToMidField;
    private boolean goToFar;
    private Supplier<PathChain> closePathChain;
    private Supplier<PathChain> farPathChain;
    public TestBenchBotRed(HardwareMap hwMap) {
        fw = Constants.createFollower(hwMap);
        fw.setStartingPose(
               Constants.redStartPose
        );

        intake = new Intake(hwMap);

        shooter = new Shooter(hwMap, shooterCoefficients);

        fw.startTeleopDrive(true);

        runIntake = false;
        runShooter = false;

        shotPos = ShotPos.FAR;

        closePathChain = () -> fw.pathBuilder() //Lazy Curve Generation
                .addPath(new Path(new BezierLine(fw::getPose, new Pose(72,72,Math.PI/2))))
                .setHeadingInterpolation(HeadingInterpolator.linearFromPoint(fw::getHeading, Math.toRadians(135), 0.8))
                .build();

        farPathChain = () -> fw.pathBuilder()
                .addPath(new Path(new BezierLine(fw::getPose, Constants.farRedShoot)))
                .setHeadingInterpolation(HeadingInterpolator.linearFromPoint(fw::getHeading, Constants.farRedShoot::getHeading, 0.8))
                .build();

        goToMidField = false;
        goToFar = false;
    }

    public void drivetrain(Gamepad gp) {
        fw.update();

        if (!goToMidField) {
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

        if (goToMidField) {
            shooter.midFieldShoot();
        }
        else if (goToFar) {
            shooter.farShoot();
        }

        if (gp.dpadUpWasPressed()) {
            if (goToMidField) {
                fw.breakFollowing();
                fw.startTeleopDrive(true);
            }
            else {
                goToFar = false;
                //fw.followPath(closePathChain.get());
            }

            goToMidField = !goToMidField;

        }
        else if (gp.dpadDownWasPressed()) {
            if (goToFar) {
                fw.breakFollowing();
                fw.startTeleopDrive(true);
            }
            else{
                goToMidField = false;
                //fw.followPath(farPathChain.get());
            }

            goToFar = !goToFar;
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
            transfer.reload();
        }
        else {
            transfer.feed();
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
