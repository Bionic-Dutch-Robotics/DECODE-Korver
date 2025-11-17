package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.util.Settings;

import java.util.function.Supplier;

public class BOTT {
    public Follower fw;
    public Intake intake;
    boolean runIntake;
    public Shooter shooter;
    boolean runShooter;
    public Transfer transfer;
    double forwardPower, strafePower, turnPower;
    private boolean goTo;
    private Supplier<PathChain> pathChain;
    public BOTT(HardwareMap hwMap) {
        fw = Constants.createFollower(hwMap);
        fw.setStartingPose(
                Settings.Positions.Drivetrain.TELEOP_START
        );

        intake = new Intake(hwMap);
        shooter = new Shooter(hwMap, Settings.Positions.Shooter.shooterCoefficients);
        transfer = new Transfer(hwMap);

        fw.startTeleopDrive(true);

        runIntake = false;
        runShooter = false;


        pathChain = () -> fw.pathBuilder() //Lazy Curve Generation
                .addPath(new Path(new BezierLine(fw::getPose, Settings.Positions.Drivetrain.MIDFIELD_SHOOT)))
                .setHeadingInterpolation(HeadingInterpolator.linearFromPoint(fw::getHeading, Settings.Positions.Drivetrain.MIDFIELD_SHOOT.getHeading(), 0.8))
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

            strafePower = gp.left_stick_x;
            if (Math.abs(strafePower) < 0.05) {
                strafePower = 0;
            }

            turnPower = gp.right_stick_x;
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

        if (gp.aWasPressed()) {
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
        if (gp.yWasPressed()) {
            runShooter = !runShooter;
        }

        if (runShooter) {
            shooter.shootFar();
        }
        else if (gp.x) {
            shooter.eject();
        }
        else {
            shooter.stop();
        }
    }

    public void transfer(Gamepad gp) {
        if (gp.dpad_up) {
            transfer.reload();
        }
        else {
            transfer.feed();
        }
    }
}
