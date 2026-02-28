package org.firstinspires.ftc.teamcode.autos;

import static org.firstinspires.ftc.teamcode.util.Hardware.dt;
import static org.firstinspires.ftc.teamcode.util.Hardware.intake;
import static org.firstinspires.ftc.teamcode.util.Hardware.shooter;
import static org.firstinspires.ftc.teamcode.util.Hardware.transfer;
import static org.firstinspires.ftc.teamcode.util.Settings.Positions.Transfer.RUN_TO_POS_TIME;
import static org.firstinspires.ftc.teamcode.util.Settings.Positions.Transfer.SLOW_SHOOT_COEFFICIENT;

import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.pedropathing.paths.callbacks.ParametricCallback;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.util.AllianceColor;
import org.firstinspires.ftc.teamcode.util.Hardware;
import org.firstinspires.ftc.teamcode.util.MatchSettings;
import org.firstinspires.ftc.teamcode.util.Settings;
import org.firstinspires.ftc.teamcode.util.control.Controller;

@Autonomous(name="One Row Blue Auto", preselectTeleOp = "Blue TeleOp FR")
public class OneRow extends OpMode {
    private PathChain[] paths;
    private int index = 0;
    private boolean hasShot1 = false, hasShot2=false, hasShot3=false, hasShot4=false;
    private boolean hasFinishedIntakePath1=false, hasFinishedIntakePath2=false, hasFinishedIntakePath3=false;
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
                                    new Pose(20.700, 62.200),
                                    new Pose(19.000, 66.300)
                            ))
                    ),
                    new PathChain(      //1 --- Go to Shoot 1
                            new Path(
                                    new BezierCurve(
                                            new Pose(19.000, 66.300),
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
                            0, 0.27, dt.follower,
                            () -> {
                                dt.follower.setMaxPower(0.4);
                                intake.run();
                            }
                    ),
                    new ParametricCallback(
                            0, 0.97, dt.follower,
                            () -> {
                                dt.follower.followPath(paths[3]);
                            }
                    )
            );

            paths[1].setCallbacks(
                    new ParametricCallback(
                            0, 0.05, dt.follower,
                            () -> {
                                dt.follower.setMaxPower(1);
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
                                dt.follower.setMaxPower(0.5);
                                intake.run();
                            }
                    ),
                    new ParametricCallback(
                            0, 0.97, dt.follower,
                            () -> {
                                dt.follower.followPath(paths[3]);
                                dt.follower.setMaxPower(1);
                                hasFinishedIntakePath2=true;
                            }
                    )
            );
            paths[3].setCallbacks(
                    new ParametricCallback(
                            0, 1, dt.follower,
                            () -> hasIntook2 = true
                    )
            );

            paths[4].setCallbacks(
                    new ParametricCallback(
                            0, 0.2, dt.follower,
                            () -> {
                                dt.follower.setMaxPower(0.6);
                                intake.run();
                            }
                    ),
                    new ParametricCallback(
                            0, 0.97, dt.follower,
                            () -> {
                                hasFinishedIntakePath3=true;
                                dt.follower.setMaxPower(1);
                                dt.follower.followPath(
                                        paths[5]
                                );
                            }
                    )
            );

            paths[5].setCallbacks(
                    new ParametricCallback(
                            0, 1, dt.follower,
                            () -> {
                                hasIntook3 = true;
                            }
                    )
            );
        }       // Callbacks

        for (PathChain path : paths) {
            path.getPath(0).setConstantHeadingInterpolation(Math.PI);
        }

        manager.bind(
                () -> (!hasShot1 && shootTimer.time() > 1.5),
                () -> {
                    transfer.fireSortedArtifacts();
                    shootTimer.reset();
                    hasShot1 = true;
                }
        );

        manager.bind(
                () -> (hasShot1 && shootTimer.time() > RUN_TO_POS_TIME*6 && !hasFinishedIntakePath1),
                () -> {
                    dt.follower.followPath(paths[4]);
                    hasFinishedIntakePath1 = true;
                }
        );

        manager.bind(
                () -> (hasIntook1 && !hasShot2),
                () -> {
                    transfer.runSlow();
                    shootTimer.reset();
                    hasShot2 = true;
                    shooter.turret.setLiveOffset(-0.1);
                }
        );
    }

    @Override
    public void init_loop() {
        MatchSettings.refreshMotif(telemetry);
        telemetry.update();

        if (gamepad1.aWasPressed()) {
            shooter.flywheel.setVoltageComp(
                    shooter.flywheel.getVoltageComp() + 0.02
            );
        }
        else if (gamepad1.bWasPressed()) {
            shooter.flywheel.setVoltageComp(
                    shooter.flywheel.getVoltageComp() - 0.02
            );
        }

        if (gamepad1.leftStickButtonWasPressed()) {
            if (MatchSettings.allianceColor.isRed()) {
                dt.follower.setStartingPose(Settings.Positions.Drivetrain.Red.FAR_AUTO_START);
                MatchSettings.AutoToTeleOpCarryOver.turretEndRadians = 0.0;
            } else {
                dt.follower.setStartingPose(Settings.Positions.Drivetrain.Blue.FAR_AUTO_START);
                MatchSettings.AutoToTeleOpCarryOver.turretEndRadians = 0.0;
            }
        }

        telemetry.addData("Volt Comp",shooter.flywheel.getVoltageComp());
    }

    @Override
    public void start() {
        hardwareMap.get(Servo.class, "park").setPosition(0.65);
        shootTimer.reset();
        MatchSettings.start();
    }

    @Override
    public void loop() {
        intake.run();
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

    @Override
    public void stop() {
        Hardware.stop();
    }
}
