package org.firstinspires.ftc.teamcode.subsystems.drivetrain;

import com.pedropathing.control.FilteredPIDFController;
import com.pedropathing.control.PIDFController;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.util.AllianceColor;

public class Drivetrain {
    public FilteredPIDFController xPid, yPid;
    public PIDFController headingPid;
    public Follower follower = null;

    /**
     * Creates a new drivetrain
     * @param hwMap     A HardwareMap object from an OpMode or LinearOpMode
     */
    public Drivetrain(HardwareMap hwMap, AllianceColor alliance) {
        if (follower == null) {
            follower = Constants.createFollower(hwMap);
            follower.setStartingPose(
                    alliance.isRed() ? Constants.redStartPose : Constants.blueStartPose
            );
        }
        //follower.teleOpLock(false, false, true);
    }

    public void startTeleOpDrive() {
        follower.startTeleopDrive(true);
    }

    /**
     * Moves the drivetrain in specified direction.
     */
    public void update() {
        follower.update();
    }
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
    public Pose getPose() {
        return follower.getPose();
    }
}
