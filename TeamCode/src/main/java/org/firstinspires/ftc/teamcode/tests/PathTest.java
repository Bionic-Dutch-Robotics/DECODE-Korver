package org.firstinspires.ftc.teamcode.tests;

import com.pedropathing.follower.Follower;
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

@Autonomous(name="Path Test")
public class PathTest extends OpMode {
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
        path = new Path(new BezierLine(follower.getPose(), new Pose(72,72)));
        path.setHeadingInterpolation(HeadingInterpolator.linear(Math.PI, 0, 0.8));
        follower.followPath(path);
    }

    @Override
    public void loop() {
        follower.update();
        telemetry.addData("X", follower.getPose().getX());
        telemetry.addData("Y", follower.getPose().getY());
        telemetry.addData("Theta", follower.getHeading());
        telemetry.update();

        if (!follower.isBusy()) {
            follower.followPath(path);
        }
    }
}
