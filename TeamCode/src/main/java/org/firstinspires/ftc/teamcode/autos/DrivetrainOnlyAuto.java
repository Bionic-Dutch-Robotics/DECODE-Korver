package org.firstinspires.ftc.teamcode.autos;

import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.subsystems.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.util.AllianceColor;

@Autonomous(name="Drivetrain Only")
public class DrivetrainOnlyAuto extends OpMode {
    private Path path;
    private Drivetrain dt;

    @Override
    public void init() {
        dt = new Drivetrain(hardwareMap, new AllianceColor(AllianceColor.Selection.BLUE), new Pose(-gamepad1.left_stick_x, -gamepad1.left_stick_y, -gamepad1.right_stick_x), new Pose(1.15, 1.15, 1.15));
    }

    @Override
    public void start() {
        path = new Path(
                new BezierCurve(
                        dt.follower.getPose(),
                        new Pose(75, 35),
                        new Pose(25,30,dt.follower.getPose().getHeading())
                )
        );
        path.setConstantHeadingInterpolation(Math.PI);
        dt.follower.followPath(path);
    }

    @Override
    public void loop() {
        dt.update();

        if (!dt.follower.isBusy()) {
            dt.follower.breakFollowing();
            Path path1 = new Path(
                    new BezierLine(
                            dt.follower.getPose(),
                            new Pose(50, 15)
                    )
            );
            path1.setConstantHeadingInterpolation(Math.PI);
            dt.follower.followPath(
                    path1
            );
        }
    }
}
