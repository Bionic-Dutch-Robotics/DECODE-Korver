package org.firstinspires.ftc.teamcode.tests;

import static org.firstinspires.ftc.teamcode.util.Hardware.dt;
import static org.firstinspires.ftc.teamcode.util.Hardware.intake;
import static org.firstinspires.ftc.teamcode.util.Hardware.shooter;
import static org.firstinspires.ftc.teamcode.util.Hardware.transfer;

import com.bylazar.field.Style;
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
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.pedroPathing.tuners.Drawing;
import org.firstinspires.ftc.teamcode.util.AllianceColor;
import org.firstinspires.ftc.teamcode.util.MatchSettings;
import org.firstinspires.ftc.teamcode.util.Settings;
import org.firstinspires.ftc.teamcode.util.control.Controller;

@Autonomous(name="Full Path Test")
public class FullPath_Test extends OpMode {
    private PathChain[] paths;
    private int index = 0;
    private boolean hasShot1 = false, hasShot2=false, hasShot3=false;
    private Controller manager = new Controller();
    private final Pose shootPos = new Pose(55, 15);
    private ElapsedTime shootTimer = new ElapsedTime(ElapsedTime.Resolution.SECONDS);
    private State state;
    @Override
    public void init() {
        MatchSettings.initSelection(hardwareMap, new AllianceColor(AllianceColor.Selection.BLUE), gamepad1);

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
                        0, 0.57, dt.follower,
                        () -> {
                            dt.follower.setMaxPower(0.25);
                            intake.run();
                        }
                )
        );
        paths[1].getPath(0).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180));
        paths[1].setCallbacks(
                new ParametricCallback(
                        0, 0.05, dt.follower,
                        () -> {
                            dt.follower.setMaxPower(1);
                            intake.custom(0.5);
                            dt.follower.followPath(
                                    paths[2]
                            );
                        }
                ),
                new ParametricCallback(
                        0, 0.95, dt.follower,
                        ()-> {
                            if (!hasShot1) {
                                transfer.runSlow();
                                hasShot1=true;
                            }

                        }
                )
        );

        state = State.SHOOT_0;
        manager.bind(
                () -> (state == State.SHOOT_0 && !hasShot1),
                transfer::runSlow
        );
        manager.bind(
                () -> (state == State.SHOOT_0 && shootTimer.time() > Settings.Positions.Transfer.RUN_TO_POS_TIME * 1.5) && hasShot1,
                () -> {
                    shootTimer.reset();
                    dt.follower.followPath(paths[0]);
                    state = State.INTAKE_1;
                }
        );
        manager.bind(
                () -> (state == State.INTAKE_1 && !dt.follower.isBusy()),
                () -> dt.follower.followPath(paths[1])
        );

        paths[2].getPath(0).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180));
        paths[2].setCallbacks(
                new ParametricCallback(
                        0, 0.6, dt.follower,
                        () -> {
                            dt.follower.setMaxPower(0.65);
                            intake.run();
                        }
                ),
                new ParametricCallback(
                        0, 1, dt.follower,
                        () -> {
                            dt.follower.followPath(paths[3]);
                        }
                )
        );

        paths[3].getPath(0).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180));
        paths[3].setCallbacks(
                new ParametricCallback(
                        0, 0, dt.follower,
                        () -> {
                            dt.follower.setMaxPower(1);
                            intake.stop();
                        }
                )
        );
        paths[4].getPath(0).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180));
        paths[4].setCallbacks(
                new ParametricCallback(
                        0, 0.6, dt.follower,
                        () -> {
                            dt.follower.setMaxPower(0.65);
                            intake.run();
                        }
                )
        );
        paths[5].getPath(0).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180));
        paths[5].setCallbacks(
                new ParametricCallback(
                        0, 0.0, dt.follower,
                        () -> {
                            dt.follower.setMaxPower(1);
                            intake.stop();
                        }
                )
        );
    }

    @Override
    public void init_loop() {
        MatchSettings.refreshMotif(telemetry);
        telemetry.update();


    }

    @Override
    public void start() {
        shootTimer.reset();
        MatchSettings.start();

        dt.follower.followPath(paths[0]);
    }

    @Override
    public void loop() {
        manager.update();


        shooter.flywheel.update(100);
        dt.follower.update();
        telemetry.addData("X", dt.follower.getPose().getX());
        telemetry.addData("Y", dt.follower.getPose().getY());
        telemetry.addData("Theta", dt.follower.getHeading());
        telemetry.update();
    }
}
    enum State {
        SHOOT_0,
        INTAKE_1,
        GO_TO_SHOOT_1,
        SHOOT_1,
        INTAKE_2,
        GO_TO_SHOOT_2,
        SHOOT_2,
        INTAKE_3,
        GO_TO_SHOOT_3,
        SHOOT_3
    }
