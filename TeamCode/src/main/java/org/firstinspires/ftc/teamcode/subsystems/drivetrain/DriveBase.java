package org.firstinspires.ftc.teamcode.subsystems.drivetrain;

import com.pedropathing.control.FilteredPIDFController;
import com.pedropathing.control.PIDFController;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

public class DriveBase {
    private Follower follower;

    public FilteredPIDFController xPid, yPid;
    public PIDFController headingPid;
    private Pose position;

    /**
     * Creates a new drivetrain
     * @param hwMap     A HardwareMap object from an OpMode or LinearOpMode
     */
    public DriveBase(HardwareMap hwMap) {
        follower = Constants.createFollower(hwMap);
    }

    /**
     * Moves the drivetrain in specified direction. Forward is the intake side.
     *
     */
    public void mecanumDrive(double forwardPower, double strafePower, double turnPower) {
        follower.setTeleOpDrive(
                forwardPower,
                strafePower,
                turnPower,
                true
        );
    }

    /**
     * Gets real-time position of the robot, in PedroPathing coordinates.
     */
    public Pose getPosition() {
        return position;
    }
}
