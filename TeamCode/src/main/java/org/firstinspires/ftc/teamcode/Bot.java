package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Intake;

public class Bot {
    public Gamepad gamepad1;
    public boolean orbit;
    public Drivetrain dt;
    public Intake intake;
    public Follower follower;
    private final Pose startPose = new Pose(9,9,0);

    /**
     * Constructs a new Bot, consisting of a Pedro Follower, Intake, Drivetrain and Gamepad
     * @param gamepad1  takes `gamepad1` or `gamepad2`
     * @param hardwareMap   takes `hardwareMap`
     */
    public Bot (Gamepad gamepad1, HardwareMap hardwareMap) {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startPose);

        intake = new Intake(hardwareMap);

        dt = new Drivetrain(gamepad1, follower, startPose);
        dt.update();
    }

    /**
     * Runs the Drivetrain and Follower for TeleOp
     */
    public void drivetrain() {
        if (gamepad1.aWasPressed()) {
            orbit = !orbit;
        }

        dt.runTeleOpDrive(0.5, orbit, dt.RED_GOAL);
        dt.update();
    }

    /**
     * Runs the Intake for TeleOp
     */
    public void intake () {
        if (gamepad1.b) {
            intake.intake();
        }
        else if (gamepad1.x) {
            intake.reject();
        }
    }
}