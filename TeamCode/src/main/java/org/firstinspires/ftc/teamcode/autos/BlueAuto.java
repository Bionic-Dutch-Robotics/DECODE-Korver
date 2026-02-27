package org.firstinspires.ftc.teamcode.autos;

import static org.firstinspires.ftc.teamcode.util.Hardware.dt;
import static org.firstinspires.ftc.teamcode.util.Hardware.intake;
import static org.firstinspires.ftc.teamcode.util.Hardware.shooter;
import static org.firstinspires.ftc.teamcode.util.Hardware.transfer;
import static org.firstinspires.ftc.teamcode.util.Settings.Positions.Transfer.RUN_TO_POS_TIME;

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

@Autonomous(name="Real Blue Auto", preselectTeleOp = "Blue TeleOp FR")
public class BlueAuto extends OpMode {
    private PathChain[] paths;
    private int index = 0;
    private boolean hasShot1 = false, hasShot2=false, hasShot3=false, hashShot4=false;
    private boolean hasIntook1 = false, hasIntook2 = false, hasIntook3 = false;
    private Controller manager = new Controller();
    private final Pose shootPos = new Pose(55, 15);
    private ElapsedTime shootTimer = new ElapsedTime(ElapsedTime.Resolution.SECONDS);

    @Override
    public void init() {
        MatchSettings.initSelection(hardwareMap, new AllianceColor(AllianceColor.Selection.BLUE), gamepad1);

        {
            paths = new PathChain[]{
                    new PathChain(      //0 --- Intake 1
                            new Path(new BezierCurve(
                                    new Pose(56.500, 8.500),
                                    new Pose(81.200, 59.900),
                                    new Pose(33.500, 81.700),
                                    new Pose(28.200, 41.400),
                                    new Pose(20.700, 61.200),
                                    new Pose(16.000, 63.700)
                            ))
                    ),
                    new PathChain(      //1 --- Go to Shoot 1
                            new Path(
                                    new BezierCurve(
                                            new Pose(16.000, 63.700),
                                            new Pose(50.800, 63.900),
                                            shootPos
                                    )
                            )
                    ),
                    new PathChain(      //2 --- Intake 2
                            new Path(
                                    new BezierCurve(
                                            shootPos,
                                            new Pose(76.0, 89.50),
                                            new Pose(18.600, 83.600)
                                    )
                            )
                    ),
                    new PathChain(      //3 --- Go To Shoot 2
                            new Path(
                                    new BezierCurve(
                                            new Pose(18.600, 83.600),
                                            new Pose(44.200, 68.500),
                                            shootPos
                                    )
                            )
                    ),
                    new PathChain(      //4 --- Intake 3
                            new Path(
                                    new BezierCurve(
                                            shootPos,
                                            new Pose(64.100, 39.00),
                                            new Pose(17.600, 35.300)
                                    )
                            )
                    ),
                    new PathChain(      //5 --- Go to Shoot 3
                            new Path(
                                    new BezierLine(
                                            new Pose(17.600, 35.300),
                                            shootPos
                                    )
                            )
                    )
            };
        }       // Paths

        {
            paths[0].setCallbacks(
                    new ParametricCallback(
                            0, 0.57, dt.follower,
                            () -> {
                                dt.follower.setMaxPower(0.25);
                                intake.run();
                            }
                    ),
                    new ParametricCallback(
                            0, 1, dt.follower,
                            () -> {
                                dt.follower.followPath(paths[1]);
                            }
                    )
            );

            paths[1].setCallbacks(
                    new ParametricCallback(
                            0, 0.05, dt.follower,
                            () -> {
                                dt.follower.setMaxPower(1);
                                intake.custom(0.5);
                            }
                    ),
                    new ParametricCallback(
                            0, 0.97, dt.follower,
                            () -> {
                                hasIntook1 = true;
                            }
                    )
            );

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
            paths[3].setCallbacks(
                    new ParametricCallback(
                            0, 1, dt.follower,
                            () -> hasIntook1 = true
                    )
            );
        }       // Callbacks

        for (PathChain path : paths) {
            path.getPath(0).setConstantHeadingInterpolation(Math.PI);
        }

        manager.bind(
                () -> (!hasShot1),
                () -> {
                    transfer.fireSortedArtifacts();
                    shootTimer.reset();
                    hasShot1 = true;
                }
        );

        manager.bind(
                () -> (hasShot1 && shootTimer.time() > RUN_TO_POS_TIME*6),
                () -> {
                    dt.follower.followPath(paths[0]);
                }
        );

        manager.bind(
                () -> (hasIntook1),
                () -> {
                    transfer.runSlow();
                    shootTimer.reset();
                    hasShot2 = true;
                }
        );

        manager.bind(
                () -> (shootTimer.time() > RUN_TO_POS_TIME*6*1.5 && hasShot2),
                () -> {
                    dt.follower.followPath(paths[2]);
                }
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
    }

    @Override
    public void loop() {
        shooter.runLoop(
                dt.getPose(),
                dt.follower.getVelocity(),
                dt.follower.getAngularVelocity()
        );

        manager.update();


        dt.follower.update();
        telemetry.addData("X", dt.follower.getPose().getX());
        telemetry.addData("Y", dt.follower.getPose().getY());
        telemetry.addData("Theta", dt.follower.getHeading());
        telemetry.update();
    }
}
