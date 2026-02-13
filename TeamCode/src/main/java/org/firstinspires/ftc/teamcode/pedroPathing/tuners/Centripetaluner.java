package org.firstinspires.ftc.teamcode.pedroPathing.tuners;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

/**
 * This is the Centripetal Tuner OpMode. It runs the robot in a specified distance
 * forward and to the left. On reaching the end of the forward Path, the robot runs the backward
 * Path the same distance back to the start. Rinse and repeat! This is good for testing a variety
 * of Vectors, like the drive Vector, the translational Vector, the heading Vector, and the
 * centripetal Vector.
 *
 * @author Baron Henderson - 20077 The Indubitables
 * @author Anyi Lin - 10158 Scott's Bots
 * @author Aaron Yang - 10158 Scott's Bots
 * @author Harrison Womack - 10158 Scott's Bots
 * @author Atharv Gurnani - 13085 Bionic Dutch
 * @version 1.0, 3/13/2024
 */
@TeleOp(name="Centripetal Coefficient Tuner", group="tuners")
public class Centripetaluner extends OpMode {
    public  double DISTANCE = 20;
    private boolean forward = true;
    private Follower follower;

    private Path forwards;
    private Path backwards;

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(72, 72));
    }

    /**
     * This initializes the Follower and creates the forward and backward Paths.
     * Additionally, this initializes the Panels telemetry.
     */
    @Override
    public void init_loop() {
        telemetry.addLine("This will run the robot in a curve going " + DISTANCE + " inches to the left and the same number of inches forward.");
        telemetry.addLine("The robot will go continuously along the path.");
        telemetry.addLine("Make sure you have enough room.");
        telemetry.update();
        follower.update();
    }

    @Override
    public void start() {
        follower.startTeleopDrive(false);
        follower.activateAllPIDFs();
        forwards = new Path(new BezierCurve(new Pose(72,72), new Pose(Math.abs(DISTANCE) + 72,72), new Pose(Math.abs(DISTANCE) + 72,DISTANCE + 72)));
        backwards = new Path(new BezierCurve(new Pose(Math.abs(DISTANCE) + 72,DISTANCE + 72), new Pose(Math.abs(DISTANCE) + 72,72), new Pose(72,72)));

        backwards.setTangentHeadingInterpolation();
        backwards.reverseHeadingInterpolation();

        follower.followPath(forwards);
        Drawing.init();
        Drawing.drawRobot(follower.getPose());
    }

    /**
     * This runs the OpMode, updating the Follower as well as printing out the debug statements to
     * the Telemetry, as well as the Panels.
     */
    @Override
    public void loop() {
        follower.update();
        Drawing.drawDebug(follower);
        if (!follower.isBusy()) {
            if (forward) {
                forward = false;
                follower.followPath(backwards);
            } else {
                forward = true;
                follower.followPath(forwards);
            }
        }

        telemetry.addLine("Driving away from the origin along the curve?: " + forward);
        telemetry.update();
    }
}

