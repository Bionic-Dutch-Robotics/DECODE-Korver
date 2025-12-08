package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.pedroPathing.Constants.follower;

import androidx.annotation.NonNull;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import java.util.function.Supplier;

public class SubsystemsManager {
    public Intake intake;
    public Shooter shooter;
    public Transfer transfer;
    public AllianceColor allianceColor;

    public IntakeState intakeState;
    public static ShooterState shooterState;
    private boolean goToMidField, goToFar, goToPark;
    private Pose gpThreshold;
    private Supplier<PathChain> closeShootPath, farShootPath, park;

    public SubsystemsManager (@NonNull AllianceColor allianceColor, @NonNull HardwareMap hwMap, @NonNull Gamepad gp){
        this.allianceColor = allianceColor;

        intake = new Intake(hwMap);
        shooter = new Shooter(hwMap, Constants.shooterCoefficients);
        transfer = new Transfer(hwMap);

        gpThreshold = new Pose(
                -gp.left_stick_x,
                -gp.left_stick_y,
                -gp.right_stick_x
        );

        closeShootPath = () -> follower.pathBuilder()
                .addPath(new Path(new BezierLine(follower::getPose, allianceIsRed() ? Constants.redCloseShoot : Constants.blueCloseShoot)))
                .setHeadingInterpolation(HeadingInterpolator.linearFromPoint(follower::getHeading, allianceIsRed() ? Constants.redCloseShoot.getHeading() : Constants.blueCloseShoot.getHeading(), 0.8))
                .build();

        farShootPath = () -> follower.pathBuilder()
                .addPath(new Path(new BezierLine(follower::getPose, allianceIsRed() ? Constants.farRedShoot : Constants.farBlueShoot)))
                .setHeadingInterpolation(HeadingInterpolator.linearFromPoint(follower::getHeading, allianceIsRed() ? Constants.farRedShoot.getHeading() : Constants.farBlueShoot.getHeading(), 0.8))
                .build();

        park = () -> follower.pathBuilder()
                .addPath(new Path(new BezierLine(follower::getPose, allianceIsRed() ? Constants.redPark : Constants.bluePark)))
                .setHeadingInterpolation(HeadingInterpolator.linearFromPoint(follower::getHeading,
                        allianceIsRed() ? Constants.redPark.getHeading() : Constants.bluePark.getHeading(), 0.8))
                .build();

        intakeState = IntakeState.STOP;
        shooterState = ShooterState.STOP;

        goToMidField = false;
        goToFar = false;
        goToPark = false;
    }

    public void start() {
        follower.startTeleopDrive(true);
    }
    public void drivetrain(Gamepad gp) {
        follower.update();

        if (!goToMidField && !goToFar) {
            double forward = -gp.left_stick_y;
            double strafe = -gp.left_stick_x;
            double turn = -gp.right_stick_x;

            follower.setTeleOpDrive(
                    forward != Math.abs(gpThreshold.getY()) - 0.03 ? forward : 0,
                    strafe != Math.abs(gpThreshold.getX()) ? strafe : 0,
                    turn != Math.abs(gpThreshold.getY()) ? turn : 0,
                    true
            );
        }

        if (gp.aWasPressed()) {
            goToMidField = !goToMidField;
            goToFar = false;
            goToPark = false;

            if (goToMidField) {
                follower.breakFollowing();
                follower.followPath(closeShootPath.get());
                shooterState = ShooterState.CLOSE;
            } else {
                follower.breakFollowing();
                follower.startTeleopDrive(true);
            }

        }
        else if (gp.bWasPressed()) {
            goToFar = !goToFar;
            goToMidField = false;
            goToPark = false;

            if (goToFar) {
                follower.breakFollowing();
                follower.followPath(farShootPath.get());
                shooterState = ShooterState.FAR;
            }
            else {
                follower.breakFollowing();
                follower.startTeleopDrive(true);
            }
        }
        else if (gp.xWasPressed()) {
            goToFar = false;
            goToMidField = false;
            goToPark = !goToPark;

            if (goToPark) {
                follower.breakFollowing();
                follower.followPath(park.get());
            }
            else {
                follower.breakFollowing();
                follower.startTeleopDrive(true);
            }
        }
    }

    public void shooter(Gamepad gp) {
        if (gp.yWasPressed()) {
            shooterState = ShooterState.CLOSE;
        }
        else if (gp.bWasPressed()) {
            shooterState = ShooterState.FAR;
        }
        else if (gp.xWasPressed()) {
            shooterState = ShooterState.EJECT;
        }
        else if (gp.aWasPressed()) {
            shooterState = ShooterState.IDLE;
        }

        if (shooterState == ShooterState.CLOSE)         shooter.midFieldShoot();
        else if (shooterState == ShooterState.FAR)      shooter.farShoot();
        else if (shooterState == ShooterState.EJECT)    shooter.eject();
        else shooter.idle();
    }

    public void intake(Gamepad gp) {
        if (gp.dpadUpWasPressed()) {
            intakeState = IntakeState.FEED;
        }
        else if (gp.dpadDownWasPressed()) {
            intakeState = IntakeState.EJECT;
        }
        else if (gp.dpadLeftWasPressed()) {
            intakeState = IntakeState.STOP;
        }

        if (intakeState == IntakeState.FEED) {
            intake.intake();
        }
        else if (intakeState == IntakeState.EJECT) {
            intake.eject();
        }
        else {
            intake.stop();
        }
    }

    public void transfer(Gamepad gp) {
        if (gp.dpad_up) {
            transfer.reload();
        }
        else    transfer.feed();
    }

    public void emergencyResets(Gamepad gp) {
        if (gp.startWasPressed()) {
            follower.setPose(allianceIsRed() ? Constants.redStartPose : Constants.blueStartPose);
        }
    }

    public enum AllianceColor {
        BLUE, RED
    }
    public enum ShooterState {
        STOP, IDLE, CLOSE, FAR, EJECT
    }
    public enum IntakeState {
        STOP, EJECT, FEED
    }

    private boolean allianceIsRed() {
        return allianceColor == AllianceColor.RED;
    }
}
