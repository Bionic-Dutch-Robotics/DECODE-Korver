package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.util.DataLogger;

@SuppressWarnings("all")
public class Bot {
    public Gamepad gamepad1;
    public boolean orbit;
    public Drivetrain dt;
    public Intake intake;
    public Follower follower;
    public DataLogger logs;

    /**
     * Constructs a new Bot, consisting of a Pedro Follower, Intake, Drivetrain, DataLogger and Gamepad
     * @param gamepad1  takes `gamepad1` or `gamepad2`
     * @param hardwareMap   takes `hardwareMap`
     */
    public Bot (Gamepad gamepad1, HardwareMap hardwareMap) {
        follower = Constants.createFollower(hardwareMap);

        logs = new DataLogger("testLog1");
        logs.addField("Bot X");
        logs.addField("Bot Y");
        logs.addField("Bot Heading");
        logs.firstLine();

        intake = new Intake(hardwareMap);

        dt = new Drivetrain(gamepad1, hardwareMap, Constants.startPose);
        dt.update();
    }

    /**
     * Runs the Drivetrain and Follower for TeleOp
     */
    public void drivetrain(Pose power, boolean orbit) {

        dt.runTeleOpDrive(
                new Pose(
                    gamepad1.left_stick_x,
                    -gamepad1.left_stick_y,
                    gamepad1.right_stick_x
                ),
                0.5,
                orbit,
                dt.RED_GOAL);
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
            intake.eject();
        }
    }

    /**
     * Add robot debugging logs for later review
     */
    public void log() {
        logs.addField(dt.position.getX());
        logs.addField(dt.position.getY());
        logs.addField(dt.position.getHeading());
        logs.newLine();
    }
}