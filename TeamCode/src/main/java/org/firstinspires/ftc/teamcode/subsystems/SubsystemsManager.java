package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.pedroPathing.Constants.follower;

import androidx.annotation.NonNull;

import com.pedropathing.control.PIDFController;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.math.MathFunctions;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.util.AllianceColor;

import java.util.function.Supplier;

public class SubsystemsManager {
    public static com.pedropathing.control.PIDFController headingPid;
    public Intake intake;
    public Shooter shooter;
    public Transfer transfer;
    public AllianceColor allianceColor;

    public IntakeState intakeState;
    public static ShooterState shooterState;
    private boolean goToMidField, goToFar, goToPark;
    private Pose gpThreshold;
    private Supplier<PathChain> closeShootPath, farShootPath, park;

    public SubsystemsManager(@NonNull AllianceColor allianceColor, @NonNull HardwareMap hwMap, @NonNull Gamepad gp) {
        this.allianceColor = allianceColor;
        headingPid = new PIDFController(Constants.followerConstants.getCoefficientsHeadingPIDF());

        if (follower == null) {
            follower = Constants.createFollower(hwMap);
            follower.setStartingPose(allianceColor.isRed() ? Constants.redStartPose : Constants.blueStartPose);
        }
        intake = new Intake(hwMap);
        shooter = new Shooter(hwMap, Constants.shooterCoefficients);
        transfer = new Transfer(hwMap);

        gpThreshold = new Pose(
                -gp.left_stick_x,
                -gp.left_stick_y,
                -gp.right_stick_x
        );

        closeShootPath = () -> follower.pathBuilder()
                .addPath(new Path(new BezierLine(follower::getPose, allianceColor.isRed() ? Constants.redCloseShoot : Constants.blueCloseShoot)))
                .setHeadingInterpolation(HeadingInterpolator.linearFromPoint(follower::getHeading, allianceColor.isRed() ? Constants.redCloseShoot.getHeading() : Constants.blueCloseShoot.getHeading(), 0.8))
                .build();

        farShootPath = () -> follower.pathBuilder()
                .addPath(new Path(new BezierLine(follower::getPose, allianceColor.isRed() ? Constants.farRedShoot : Constants.farBlueShoot)))
                .setHeadingInterpolation(HeadingInterpolator.linearFromPoint(follower::getHeading, allianceColor.isRed() ? Constants.farRedShoot.getHeading() : Constants.farBlueShoot.getHeading(), 0.8))
                .build();

        park = () -> follower.pathBuilder()
                .addPath(new Path(new BezierLine(follower::getPose, allianceColor.isRed() ? Constants.redPark : Constants.bluePark)))
                .setHeadingInterpolation(HeadingInterpolator.linearFromPoint(follower::getHeading,
                        allianceColor.isRed() ? Constants.redPark.getHeading() : Constants.bluePark.getHeading(), 0.8))
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
        double forward = -gp.left_stick_y;
        double strafe = -gp.left_stick_x;
        double turn = -gp.right_stick_x;

        if (!goToMidField && !goToFar && !goToPark) {
            follower.setTeleOpDrive(
                    forward != Math.abs(gpThreshold.getY()) - 0.03 ? forward : 0,
                    strafe != Math.abs(gpThreshold.getX()) - 0.03 ? strafe : 0,
                    turn != Math.abs(gpThreshold.getY()) - 0.03 ? turn : 0,
                    false,
                    allianceColor.isRed() ? 0 : Math.toRadians(180)
            );
        }
        else if (goToMidField && !goToFar && !goToPark) {
            headingPid.updatePosition(follower.getHeading());
            headingPid.setTargetPosition(getTargetHeading(follower.getPose().getX(), follower.getPose().getY(), allianceColor));
            follower.setTeleOpDrive(forward, strafe, headingPid.run(), false,
                    allianceColor.isRed() ? 0 : Math.toRadians(180));
        }

        if (gp.aWasPressed()) {
            goToMidField = !goToMidField;
            goToFar = false;
            goToPark = false;
            if (goToMidField) {
                //double forward = -gp.left_stick_y;
                //double strafe = -gp.left_stick_x;
            /*goToMidField = !goToMidField;
            goToFar = false;
            goToPark = false;

            if (goToMidField) {
                follower.breakFollowing();
                follower.followPath(closeShootPath.get());
                shooterState = ShooterState.CLOSE;
            } else {
                follower.breakFollowing();
                follower.startTeleopDrive(true);
            }*/
            }
            else {
                headingPid.reset();
            }

        } else if (gp.bWasPressed()) {
            goToFar = !goToFar;
            goToMidField = false;
            goToPark = false;

            if (goToFar) {
                follower.breakFollowing();
                follower.followPath(farShootPath.get());
                shooterState = ShooterState.FAR;
            } else {
                follower.breakFollowing();
                follower.startTeleopDrive(true);
            }
        } else if (gp.xWasPressed()) {
            goToFar = false;
            goToMidField = false;
            goToPark = !goToPark;

            if (goToPark) {
                follower.breakFollowing();
                follower.followPath(park.get());
            } else {
                follower.breakFollowing();
                follower.startTeleopDrive(true);
            }
        }
    }

    public void shooter(Gamepad gp) {
        Pose currentPose = follower.getPose();
        /*if (gp.yWasPressed()) {
            shooterState = ShooterState.CLOSE;
        } else if (gp.bWasPressed()) {
            shooterState = ShooterState.FAR;
        } else if (gp.xWasPressed()) {
            shooterState = ShooterState.EJECT;
        } else if (gp.aWasPressed()) {
            shooterState = ShooterState.IDLE;
        }

        if (shooterState == ShooterState.CLOSE) shooter.midFieldShoot();
        else if (shooterState == ShooterState.FAR) shooter.farShoot();
        else if (shooterState == ShooterState.EJECT) shooter.eject();
        else shooter.idle();*/
        shooter.adaptive(currentPose.getX(), currentPose.getY(), allianceColor);
    }

    public void intake(Gamepad gp) {
        if (gp.dpadUpWasPressed()) {
            intakeState = IntakeState.FEED;
        } else if (gp.dpadDownWasPressed()) {
            intakeState = IntakeState.EJECT;
        } else if (gp.dpadLeftWasPressed()) {
            intakeState = IntakeState.STOP;
        }

        if (intakeState == IntakeState.FEED) {
            intake.intake();
        } else if (intakeState == IntakeState.EJECT) {
            intake.eject();
        } else {
            intake.stop();
        }
    }

    public void transfer(Gamepad gp) {
        if (gp.dpad_up) {
            transfer.reload();
        } else transfer.feed();
    }

    public void emergencyResets(Gamepad gp) {
        if (gp.startWasPressed()) {
            follower.setPose(allianceColor.isRed() ? Constants.redStartPose : Constants.blueStartPose);
        }
    }

    public enum ShooterState {
        STOP, IDLE, CLOSE, FAR, EJECT
    }

    public enum IntakeState {
        STOP, EJECT, FEED
    }

    public static double getTargetHeading(double x, double y, org.firstinspires.ftc.teamcode.util.AllianceColor alliance) {
        if (alliance.isRed()) {
            return MathFunctions.normalizeAngle(Math.atan2(144-y, 141-x) + Math.toRadians(90));
        }
        else {
            double target = Math.atan2(144 - y, -x) + Math.toRadians(180);

            if (MathFunctions.normalizeAngle(target) > Math.PI) {
                return MathFunctions.normalizeAngle(target) - Math.PI * 2;
            }
            else if (MathFunctions.normalizeAngle(target) < -Math.PI){
                return MathFunctions.normalizeAngle(target) + Math.PI * 2;
            }
        }
        return 0.0;
    }
}
