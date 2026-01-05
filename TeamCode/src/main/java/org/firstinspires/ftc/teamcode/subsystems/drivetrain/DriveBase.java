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
    public DriveBase(HardwareMap hwMap, Pose startPos) {
        follower = Constants.createFollower(hwMap);
        follower.setStartingPose(startPos);
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
    public void goToPosition(Pose target) {
        Path path = new Path(new BezierLine(follower.getPose(), target));
        follower.breakFollowing();
        follower.followPath(path);
    }
    public void stopFollowing() {
        follower.breakFollowing();
    }

    public void facePoint(Pose point) {
        double tangentAngle = Math.atan2(point.getX() - this.getPosition().getX(), point.getY() - this.getPosition().getY());
        headingPid.updatePosition(follower.getHeading());
        headingPid.setTargetPosition(tangentAngle);
    }

    /**
     * Gets real-time position of the robot, in PedroPathing coordinates.
     */
    public Pose getPosition() {
        return position;
    }
    public com.pedropathing.math.Vector getVelocity() {
        return follower.getVelocity();
    }
}
