package org.firstinspires.ftc.teamcode.autos;
import static org.firstinspires.ftc.teamcode.util.Hardware.dt;
import static org.firstinspires.ftc.teamcode.util.Hardware.intake;
import static org.firstinspires.ftc.teamcode.util.Hardware.shooter;
import static org.firstinspires.ftc.teamcode.util.Hardware.transfer;

import com.pedropathing.paths.callbacks.ParametricCallback;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.TelemetryManager;
import com.bylazar.telemetry.PanelsTelemetry;

import org.firstinspires.ftc.teamcode.util.AllianceColor;
import org.firstinspires.ftc.teamcode.util.MatchSettings;

import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.follower.Follower;
import com.pedropathing.paths.PathChain;
import com.pedropathing.geometry.Pose;


@Autonomous(name = "Blue Auto", preselectTeleOp = "Blue Far")
@Configurable
public class KylesAuto extends OpMode {
    private TelemetryManager panelsTelemetry;
    private Paths paths;
    private double shooterSpeed, tiltPos;
    private final AllianceColor alliance = new AllianceColor(AllianceColor.Selection.BLUE);

    private int currentPathIndex = -1;
    private PathChain[] pathSequence;
    private boolean intakeRunning = false;

    @Override
    public void init() {
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
        MatchSettings.initSelection(hardwareMap, alliance, new Pose(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x));

        // Create follower and set starting pose (matches the first point of Pickup1 path)
        //Pose startPose = new Pose(56.500, 8.500, Math.toRadians(180));
        shooterSpeed = 250.0;
        tiltPos = 0.0;

        paths = new Paths(dt.follower);

        // Define the sequence of paths to follow
        pathSequence = new PathChain[] {
                paths.Pickup1,
                paths.Shoot1,
                paths.Pickup2,
                paths.Shoot2,
                paths.Pickup3,
                paths.Shoot3,
                paths.Leave
        };

        panelsTelemetry.debug("Status", "Initialized");
        panelsTelemetry.update(telemetry);
    }

    @Override
    public void init_loop() {
        MatchSettings.refreshMotif(telemetry);
    }

    @Override
    public void start() {
        MatchSettings.start();

        // Start the first path
        dt.follower.followPath(pathSequence[currentPathIndex]);
    }

    @Override
    public void loop() {
        dt.follower.update();

        // ===== SUBSYSTEM CONTROL - Runs every loop =====
        handleSubsystems();

        // Execute autonomous sequence
        runAutonomousSequence();

        // Log telemetry
        panelsTelemetry.debug("Current Path", currentPathIndex);
        panelsTelemetry.debug("Intake Running", intakeRunning);
        panelsTelemetry.debug("X", dt.follower.getPose().getX());
        panelsTelemetry.debug("Y", dt.follower.getPose().getY());
        panelsTelemetry.debug("Heading", dt.follower.getPose().getHeading());
        panelsTelemetry.debug("Is Busy", dt.follower.isBusy());
        panelsTelemetry.update(telemetry);
    }

    private void handleSubsystems() {
        // Check if we're on a pickup path (indices 0, 2, 4 = Pickup1, Pickup2, Pickup3)
        if (currentPathIndex == 0 || currentPathIndex == 2 || currentPathIndex == 4) {
            intake.run();
            // Start intake if not already running
        } else {
            // Stop intake if running
            intake.stop();
        }

        // Check if we're on a shoot path (indices 1, 3, 5 = Shoot1, Shoot2, Shoot3)
        if (currentPathIndex == 1 || currentPathIndex == 3 || currentPathIndex == 5) {
            // Only run shooter/transfer when we've arrived at the shooting position
            if (!dt.follower.isBusy()) {
                // Spin up flywheel and update shooter
                shooter.runLoop(dt.follower.getPose().getX(), dt.follower.getPose().getY(), dt.follower.getPose().getHeading());
                shooter.flywheel.update(shooterSpeed);
                shooter.tilt.setTilt(tiltPos);

                // Fire sorted artifacts
                transfer.fireSortedArtifacts();
            }
        }
    }

    private void runAutonomousSequence() {
        // Check if the current path is finished, then start the next one
        if (!dt.follower.isBusy()) {
            currentPathIndex++;

            // If there are more paths to run, start the next one
            if (currentPathIndex < pathSequence.length) {
                dt.follower.followPath(pathSequence[currentPathIndex]);
            }
            // Otherwise, autonomous is complete (currentPathIndex >= pathSequence.length)
        }

        if (currentPathIndex == -1) {
            transfer.fireSortedArtifacts();
        }
    }

    public static class Paths {
        public PathChain Pickup1;
        public PathChain Shoot1;
        public PathChain Pickup2;
        public PathChain Shoot2;
        public PathChain Pickup3;
        public PathChain Shoot3;
        public PathChain Leave;

        public Paths(Follower follower) {
            Pickup1 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(56.500, 8.500),
                                    new Pose(81.200, 59.900),
                                    new Pose(33.500, 81.700),
                                    new Pose(28.200, 41.400),
                                    new Pose(20.700, 61.200),
                                    new Pose(16.000, 63.700)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                    .addCallback(
                            new ParametricCallback(
                                0,
                                    0.5,
                                    dt.follower,
                                    new runIntake()

                                    )
                    )
                    .build();

            Shoot1 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(16.000, 63.700),
                                    new Pose(50.800, 63.900),
                                    new Pose(48.300, 10.200)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                    .build();

            Pickup2 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(54.200, 79.600),
                                    new Pose(70.700, 93.500),
                                    new Pose(18.600, 83.600)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                    .build();

            Shoot2 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(18.600, 83.600),
                                    new Pose(44.200, 68.500),
                                    new Pose(48.300, 10.200)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                    .build();

            Pickup3 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(54.300, 79.400),
                                    new Pose(64.100, 42.900),
                                    new Pose(17.600, 35.300)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                    .build();

            Shoot3 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(17.600, 35.300),
                                    new Pose(48.300, 10.200)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                    .build();

            Leave = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(48.300, 10.200),
                                    new Pose(35.900, 12.200)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                    .build();
        }

        public static class runIntake implements Runnable {
            @Override
            public void run() {
                intake.run();
            }
        }
    }
}