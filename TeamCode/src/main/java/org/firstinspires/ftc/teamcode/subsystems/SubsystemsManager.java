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
    private AllianceColor allianceColor;

    private boolean runIntake, runShooter;
    private boolean goToMidField, goToFar;
    private Pose gpThreshold;
    private Supplier<PathChain> closeShootPath, farShootPath;

    public SubsystemsManager (@NonNull AllianceColor allianceColor, @NonNull HardwareMap hwMap, @NonNull Gamepad gp){
        this.allianceColor = allianceColor;

        fw = Constants.createFollower(hwMap);
        fw.setStartingPose(allianceIsRed() ? Constants.redStartPose : Constants.blueStartPose);

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

        runIntake = false;
        runShooter = false;
    }

    public void start() {
        fw.startTeleopDrive(true);
    }
    public void drivetrain(Gamepad gp) {
        double forward = -gp.left_stick_y;
        double strafe = -gp.left_stick_x;
        double turn = -gp.right_stick_x;

        fw.setTeleOpDrive(
                forward != gpThreshold.getY() ? forward : 0,
                strafe != gpThreshold.getX() ? strafe : 0,
                turn != gpThreshold.getHeading() ? turn : 0,
                true
        );

        if (gp.a) {
            goToMidField = !goToMidField;
            goToFar = false;

            fw.followPath(closeShootPath.get());
        }
    }

    public void shooter(Gamepad gp) {

    }

    public enum AllianceColor {
        BLUE, RED
    }

    private boolean allianceIsRed() {
        return allianceColor == AllianceColor.RED;
    }
}
