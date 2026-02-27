package org.firstinspires.ftc.teamcode.tests.drivetrain;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.util.Settings;

@Autonomous(name="Path Test")
public class PathTest extends OpMode {
    private Follower follower;
    private Path path;
    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(Settings.Positions.Drivetrain.Blue.FAR_AUTO_START);
    }

    @Override
    public void start() {
        path = new Path(new BezierLine(Settings.Positions.Drivetrain.Blue.FAR_AUTO_START, new Pose(72,72)));
        path.setHeadingInterpolation(HeadingInterpolator.linear(Math.PI, 0));
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
