package org.firstinspires.ftc.teamcode.subsystems;

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
    public Follower fw;
    public Intake intake;
    public Shooter shooter;
    public Transfer transfer;
    public AllianceColor allianceColor;

    public IntakeState intakeState;
    public static ShooterState shooterState;
    private boolean goToMidField, goToFar;
    private Pose gpThreshold;
    private Supplier<PathChain> closeShootPath, farShootPath;

    public SubsystemsManager (@NonNull AllianceColor allianceColor, @NonNull HardwareMap hwMap, @NonNull Gamepad gp){
        this.allianceColor = allianceColor;

        fw = Constants.createFollower(hwMap);
        fw.setStartingPose(
                Constants.teleOpStartPose == null ? (allianceIsRed() ? Constants.redStartPose: Constants.blueStartPose) : Constants.teleOpStartPose
        );

        intake = new Intake(hwMap);
        shooter = new Shooter(hwMap, Constants.shooterCoefficients);
        transfer = new Transfer(hwMap);

        gpThreshold = new Pose(
                -gp.left_stick_x,
                -gp.left_stick_y,
                -gp.right_stick_x
        );

        closeShootPath = () -> fw.pathBuilder()
                .addPath(new Path(new BezierLine(fw::getPose, allianceIsRed() ? Constants.redCloseShoot : Constants.blueCloseShoot)))
                .setHeadingInterpolation(HeadingInterpolator.linearFromPoint(fw::getHeading, allianceIsRed() ? Constants.redCloseShoot.getHeading() : Constants.blueCloseShoot.getHeading(), 0.8))
                .build();

        farShootPath = () -> fw.pathBuilder()
                .addPath(new Path(new BezierLine(fw::getPose, allianceIsRed() ? Constants.farRedShoot : Constants.farBlueShoot)))
                .setHeadingInterpolation(HeadingInterpolator.linearFromPoint(fw::getHeading, allianceIsRed() ? Constants.farRedShoot.getHeading() : Constants.farBlueShoot.getHeading(), 0.8))
                .build();

        intakeState = IntakeState.STOP;
        shooterState = ShooterState.STOP;
    }

    public void start() {
        fw.startTeleopDrive(true);
    }
    public void drivetrain(Gamepad gp) {
        fw.update();

        if (!goToMidField && !goToFar) {
            double forward = -gp.left_stick_y;
            double strafe = -gp.left_stick_x;
            double turn = -gp.right_stick_x;

            fw.setTeleOpDrive(
                    forward != gpThreshold.getY() ? forward : 0,
                    strafe != gpThreshold.getX() ? strafe : 0,
                    turn != gpThreshold.getHeading() ? turn : 0,
                    true
            );
        }

        if (gp.aWasPressed()) {
            goToMidField = !goToMidField;
            goToFar = false;

            if (goToMidField) {
                fw.breakFollowing();
                fw.followPath(closeShootPath.get());
                shooterState = ShooterState.CLOSE;
            } else {
                fw.breakFollowing();
                fw.startTeleopDrive(true);
            }

        }
        else if (gp.bWasPressed()) {
            goToFar = !goToFar;
            goToMidField = false;

            if (goToFar) {
                fw.breakFollowing();
                fw.followPath(farShootPath.get());
                shooterState = ShooterState.FAR;
            }
            else {
                fw.breakFollowing();
                fw.startTeleopDrive(true);
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
            fw.setPose(allianceIsRed() ? Constants.redStartPose : Constants.blueStartPose);
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
