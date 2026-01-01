package org.firstinspires.ftc.teamcode.tests;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.drivetrain.autonomous.Actions;
import org.firstinspires.ftc.teamcode.util.AllianceColor;

@Autonomous(name="Path Test")
public class PathTest extends OpMode {
    private Actions paths;
    private Follower follower;
    @Override
    public void init() {
        paths = new Actions(AllianceColor.Selection.BLUE);
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(Constants.blueStartPose);
    }

    @Override
    public void start() {
        follower.followPath(paths.shoot1);
    }

    @Override
    public void loop() {
        if (time < 5) {
            follower.update();
            telemetry.addData("X", follower.getPose().getX());
            telemetry.addData("Y", follower.getPose().getY());
            telemetry.addData("Theta", follower.getHeading());
            telemetry.update();
        }
        else {
            follower.holdPoint(Constants.farBlueShoot);
        }
    }
}
