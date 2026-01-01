package org.firstinspires.ftc.teamcode.subsystems.drivetrain;

import com.pedropathing.control.FilteredPIDFController;
import com.pedropathing.control.PIDFController;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
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
     * Moves the drivetrain in specified direction.
     *
     */
    public void teleOpDrive(double forwardPower, double strafePower, double turnPower) {
        follower.setTeleOpDrive(
                forwardPower,
                strafePower,
                turnPower,
                true
        );
    }

    public void lineToPose(Pose target) {
        follower.breakFollowing();
        follower.followPath(
                new Path(
                        new BezierLine(follower::getPose, target)
                )
        );
    }

    /**
     * Gets real-time position of the robot, in PedroPathing coordinates.
     */
    public Pose getPosition() {
        return position;
    }
}
