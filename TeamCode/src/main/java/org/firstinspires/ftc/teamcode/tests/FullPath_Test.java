package org.firstinspires.ftc.teamcode.tests;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.FinetunedBezierCurve;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.pedropathing.paths.callbacks.ParametricCallback;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.drivetrain.autonomous.Actions;
import org.firstinspires.ftc.teamcode.util.AllianceColor;

@Autonomous(name="Full Path Test")
public class FullPath_Test extends OpMode {
    private Follower follower;
    private PathChain[] paths;
    private int index = 0;
    private final Pose shootPos = new Pose(55, 15);
    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(Constants.blueStartPose);
    }

    @Override
    public void start() {
        paths = new PathChain[]{
                new PathChain(
                        new Path(new BezierCurve(
                                new Pose(56.500, 8.500),
                                new Pose(81.200, 59.900),
                                new Pose(33.500, 81.700),
                                new Pose(28.200, 41.400),
                                new Pose(20.700, 61.200),
                                new Pose(16.000, 63.700)
                        ))
                ),
                new PathChain(
                    new Path(
                            new BezierCurve(
                                    new Pose(16.000, 63.700),
                                    new Pose(50.800, 63.900),
                                    shootPos
                            )
                    )
                ),
                new PathChain(
                    new Path(
                            new BezierCurve(
                                    shootPos,
                                    new Pose(76.0, 89.50),
                                    new Pose(18.600, 83.600)
                            )
                    )
                ),
                new PathChain(
                    new Path(
                        new BezierCurve(
                                new Pose(18.600, 83.600),
                                new Pose(44.200, 68.500),
                                shootPos
                        )
                    )
                ),
                new PathChain(
                    new Path(
                            new BezierCurve(
                                    shootPos,
                                    new Pose(64.100, 39.00),
                                    new Pose(17.600, 35.300)
                            )
                    )
                ),
                new PathChain(
                    new Path(
                            new BezierLine(
                                    new Pose(17.600, 35.300),
                                    shootPos
                            )
                    )
                )
        };
        paths[0].getPath(0).setConstantHeadingInterpolation(Math.toRadians(180));
        paths[0].setCallbacks(
                new ParametricCallback(
                        0, 0.6, follower,
                        () -> follower.setMaxPower(0.55)
                )
        );
        paths[1].getPath(0).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180));
        paths[1].setCallbacks(
                new ParametricCallback(
                        0, 0, follower,
                        () -> follower.setMaxPower(1)
                )
        );
        paths[2].getPath(0).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180));
        paths[2].setCallbacks(
                new ParametricCallback(
                        0, 0.6, follower,
                        () -> follower.setMaxPower(0.65)
                )
        );
        paths[3].getPath(0).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180));
        paths[3].setCallbacks(
                new ParametricCallback(
                        0, 0, follower,
                        () -> follower.setMaxPower(1)
                )
        );
        paths[4].getPath(0).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180));
        paths[4].setCallbacks(
                new ParametricCallback(
                        0, 0.6, follower,
                        () -> follower.setMaxPower(0.65)
                )
        );
        paths[5].getPath(0).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180));
        paths[5].setCallbacks(
                new ParametricCallback(
                        0, 0.0, follower,
                        () -> follower.setMaxPower(1)
                )
        );
        follower.followPath(paths[0]);
    }

    @Override
    public void loop() {
        follower.update();
        telemetry.addData("X", follower.getPose().getX());
        telemetry.addData("Y", follower.getPose().getY());
        telemetry.addData("Theta", follower.getHeading());
        telemetry.update();

        if (gamepad1.aWasPressed()) {
            if (index <= 5)  index += 1;
            else            index = 0;
            follower.followPath(paths[index]);
        }
    }
}
