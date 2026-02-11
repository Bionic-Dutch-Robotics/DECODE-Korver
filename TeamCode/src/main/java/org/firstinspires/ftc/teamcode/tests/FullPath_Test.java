package org.firstinspires.ftc.teamcode.tests;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.drivetrain.autonomous.Actions;
import org.firstinspires.ftc.teamcode.util.AllianceColor;

@Autonomous(name="Full Path Test")
public class FullPath_Test extends OpMode {
    private Actions paths;
    private Follower follower;
    private Path path;
    @Override
    public void init() {
        paths = new Actions(AllianceColor.Selection.BLUE);
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(Constants.blueStartPose);
    }

    @Override
    public void start() {
        path = new Path(new BezierCurve(
                new Pose(56.500, 8.500),
                new Pose(81.200, 59.900),
                new Pose(33.500, 81.700),
                new Pose(28.200, 41.400),
                new Pose(20.700, 61.200),
                new Pose(16.000, 63.700)
        ));
        path.setConstantHeadingInterpolation(Math.toRadians(180));
        follower.followPath(path);
    }

    @Override
    public void loop() {
        follower.update();
        telemetry.addData("X", follower.getPose().getX());
        telemetry.addData("Y", follower.getPose().getY());
        telemetry.addData("Theta", follower.getHeading());
        telemetry.update();
    }
}
