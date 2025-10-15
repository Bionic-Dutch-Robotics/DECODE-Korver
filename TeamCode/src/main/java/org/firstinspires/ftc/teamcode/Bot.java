package org.firstinspires.ftc.teamcode;


import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.util.DataLogger;

import java.lang.Exception;

@SuppressWarnings("all")
public class Bot {
    //public Gamepad gamepad1;
    public boolean orbit;
    public Drivetrain dt;

    /**
     * Constructs a new Bot, consisting of a Pedro Follower, Intake,
     Drivetrain, DataLogger and Gamepad
     * @param gamepad1  takes `gamepad1` or `gamepad2`
     * @param hardwareMap   takes `hardwareMap`
     */
    public Bot(Gamepad gamepad, HardwareMap hwMap) throws Exception{
        try {
            dt = new Drivetrain(gamepad, hwMap, Constants.startPose);
            dt.update();
        }
        catch (RuntimeException ignored) {

        }

        if (dt == null) {   // Ends program and throws error to RC Phone if `dt` is not initialized properly
            //throw new RobotCoreException("Failed to Initialize Drivetrain", new Exception());
        }

        //gamepad1 = gamepad;
    }

    /**
     * Runs the Drivetrain and Follower for TeleOp
     */
    public void drivetrain(boolean orbit, Gamepad gamepad) {

        dt.runTeleOpDrive(
                -gamepad.left_stick_y,     //  Forward
                gamepad.left_stick_x,      //  Strafe
                gamepad.right_stick_x,      //  Rotation
                0.5,                        //  Drive Power Relative to Input
                orbit,                      //  Boolean: Should the robot orbit around the goal?
                dt.RED_GOAL,                 //  Pose object indicating where to orbit around
                gamepad
        );

        if (gamepad.aWasPressed()) {       //Toggle orbit
            orbit = !orbit;
        }

        dt.update();
    }
}