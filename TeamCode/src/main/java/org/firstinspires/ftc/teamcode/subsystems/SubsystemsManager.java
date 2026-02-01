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
    public double shooterMult = 1.0;
    public AllianceColor allianceColor;

    public IntakeState intakeState;
    public static ShooterState shooterState;
    private boolean autoAim, goToFar, goToPark, goToMidField;
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
        shooterState = ShooterState.AUTO_AIM;

        autoAim = false;
        goToFar = false;
        goToPark = false;
        goToMidField = false;
    }

    public void start() {
        follower.startTeleopDrive(true);
    }

    public void drivetrain(Gamepad gp) {
        follower.update();
        double forward = -gp.left_stick_y;
        double strafe = -gp.left_stick_x;
        double turn = -gp.right_stick_x;

        if (!autoAim && !goToFar && !goToPark && !goToMidField) {
            follower.setTeleOpDrive(
                    forward != Math.abs(gpThreshold.getY()) - 0.03 ? forward : 0,
                    strafe != Math.abs(gpThreshold.getX()) - 0.03 ? strafe : 0,
                    turn != Math.abs(gpThreshold.getY()) - 0.03 ? turn : 0,
                    false,
                    allianceColor.isRed() ? 0 : Math.toRadians(180)
            );
        }
        else if (autoAim && !goToFar && !goToPark && !goToMidField) {
            headingPid.updatePosition(follower.getHeading());
            headingPid.setTargetPosition(getTargetHeading(
                    follower.getPose().getX(),
                    follower.getPose().getY(),
                    allianceColor
            ));

            follower.setTeleOpDrive(forward, strafe, headingPid.run(), false,
                    allianceColor.isRed() ? 0 : Math.toRadians(180)
            );
        }

        if (gp.aWasPressed()) {
            autoAim = !autoAim;
            goToFar = false;
            goToPark = false;
            goToMidField = false;

            if (!autoAim) {
                headingPid.reset();
            }

        } else if (gp.bWasPressed()) {
            goToFar = !goToFar;
            autoAim = false;
            goToPark = false;
            goToMidField = false;

            if (goToFar) {
                follower.breakFollowing();
                follower.followPath(farShootPath.get());
            } else {
                follower.breakFollowing();
                follower.startTeleopDrive(true);
            }
        } else if (gp.xWasPressed()) {
            goToFar = false;
            autoAim = false;
            goToPark = !goToPark;
            goToMidField = false;

            if (goToPark) {
                follower.breakFollowing();
                follower.followPath(park.get());
            } else {
                follower.breakFollowing();
                follower.startTeleopDrive(true);
            }
        }
        else if (gp.yWasPressed()) {
                goToMidField = !goToMidField;
                goToFar = false;
                goToPark = false;
                autoAim = false;

            if (goToMidField) {
                follower.breakFollowing();
                follower.followPath(closeShootPath.get());
            } else {
                follower.breakFollowing();
                follower.startTeleopDrive(true);
            }
        }
    }

    public void shooter(Gamepad gp) {
        if (gp.leftBumperWasPressed()) {
            shooterState = (shooterState == ShooterState.AUTO_AIM? ShooterState.MANUAL : ShooterState.AUTO_AIM);
        }
        if (shooterState == ShooterState.AUTO_AIM) {

            Pose currentPose = follower.getPose();
            shooter.adaptive(currentPose.getX(), currentPose.getY(), allianceColor, shooterMult);

            if (gp.aWasPressed()) {
                shooterMult -= 0.025;
            }
            else if (gp.yWasPressed()) {
                shooterMult += 0.025;
            }
        }
        else if (shooterState == ShooterState.MANUAL) {
            double shooterPower = shooter.getTarget();
            if (gp.yWasPressed()) {
                shooterPower = shooter.getTarget() + 10;
            }
            else if (gp.aWasPressed()) {
                shooterPower = shooter.getTarget() - 10;
            }
            else if (gp.bWasPressed()) {
                shooterPower = shooter.getTarget() + 1;
            }
            else if (gp.xWasPressed()) {
                shooterPower = shooter.getTarget() - 10;
            }
            shooter.update(shooterPower);
        }
    }

    public void intake(Gamepad gp) {
        if (gp.dpadUpWasPressed()) {
            intakeState = IntakeState.FEED;
        } else if (gp.dpadDownWasPressed()) {
            intakeState = IntakeState.EJECT;
        } else if (gp.dpadLeftWasPressed()) {
            intakeState = IntakeState.STOP;
        }
        else if (gp.dpadRightWasPressed()) {
            intakeState = IntakeState.SLOW;
        }


        if (intakeState == IntakeState.FEED) {
            intake.intake();
        }
        else if (intakeState == IntakeState.EJECT) {
            intake.eject();
        }
        else if (intakeState == IntakeState.SLOW) {
            intake.custom(0.25);
        }
        else if (intakeState == IntakeState.STOP) {
            intake.stop();
        }
    }

    public void transfer(Gamepad gp) {
        if (gp.dpad_up) {
            transfer.reload();
        } else {
            transfer.feed();
        }
    }

    public void emergencyResets(Gamepad gp) {
        if (gp.startWasPressed()) {
            follower.setPose(allianceColor.isRed() ? Constants.redStartPose : Constants.blueStartPose);
        }
    }

    public enum ShooterState {
        STOP, AUTO_AIM, MANUAL
    }

    public enum IntakeState {
        STOP, EJECT, FEED, SLOW
    }

    public static double getTargetHeading(double x, double y, org.firstinspires.ftc.teamcode.util.AllianceColor alliance) {
        if (alliance.isRed()) {
            double target = MathFunctions.normalizeAngle(Math.atan2(135-y, 135-x) + Math.toRadians(90.5));

            if (MathFunctions.normalizeAngle(target) > Math.PI) {
                return MathFunctions.normalizeAngle(target) - Math.PI * 2;
            }
            else if (MathFunctions.normalizeAngle(target) < -Math.PI){
                return MathFunctions.normalizeAngle(target) + Math.PI * 2;
            }

            return target;
        }
        else {
            double target = Math.atan2(135 - y, 9-x) + Math.toRadians(90.5);

            if (MathFunctions.normalizeAngle(target) > Math.PI) {
                return MathFunctions.normalizeAngle(target) - Math.PI * 2;
            }
            else if (MathFunctions.normalizeAngle(target) < -Math.PI){
                return MathFunctions.normalizeAngle(target) + Math.PI * 2;
            }

            return target;
        }
    }
}
