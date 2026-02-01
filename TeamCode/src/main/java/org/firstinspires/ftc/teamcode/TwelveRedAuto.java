package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.pedroPathing.Constants.follower;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.pedropathing.paths.callbacks.ParametricCallback;
import com.pedropathing.paths.callbacks.PathCallback;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.SubsystemsManager;
import org.firstinspires.ftc.teamcode.subsystems.Transfer;
import org.firstinspires.ftc.teamcode.subsystems.drivetrain.autonomous.Actions;
import org.firstinspires.ftc.teamcode.util.AllianceColor;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

//13.5V = 1.02Mult
//13.7V = 1.02Mult
@Autonomous(name ="12 Auto Red")
public class TwelveRedAuto extends OpMode {
    public static final Actions paths = new Actions(AllianceColor.Selection.RED);
    private boolean hasShotFirst, shoot;
    private Transfer transfer;
    private boolean isShootingFar = true;
    private double multiplier = 1.0;
    private Shooter shooter;
    private final AllianceColor alliance = new AllianceColor(AllianceColor.Selection.RED);
    private Intake intake;
    private double savedTime;
    private final double FLYWHEEL_SPINUP_TIME = 0.5;
    private final double KICK_TIME = 0.5;
    private final double START_TIME = 2.0;
    private final double PATH_TIME_1 = 7;

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(Constants.redStartPose);
        hasShotFirst = false;
        shoot = false;

        transfer = new Transfer(hardwareMap);
        shooter = new Shooter(hardwareMap, Constants.shooterCoefficients);
        intake = new Intake(hardwareMap);
        //time = new ElapsedTime();

        savedTime = 0.0;

        ArrayList<PathCallback> callbacks = new ArrayList<>();
        callbacks.add(0,
                new ParametricCallback(1,0.001, follower,
                        () -> {
                            follower.setMaxPower(0.35);
                            intake.intake();
                        }));
        callbacks.add(
                1,
                new ParametricCallback(1,0.001, follower,
                        () -> {
                            follower.setMaxPower(1);
                            intake.custom(0.35);
                        })
        );
        paths.redIntakeRow1.setCallbacks(
                callbacks
        );
    }

    @Override
    public void init_loop() {
        telemetry.addData("Multiplier", multiplier);
        telemetry.update();
        if (gamepad2.aWasPressed()) {
            multiplier -= 0.02;
        }
        else if (gamepad2.yWasPressed()) {
            multiplier += 0.02;
        }
    }
    @Override
    public void start() {
        resetRuntime();
        transfer.feed();
        follower.followPath(paths.shoot1);
    }

    @Override
    public void loop() {
        follower.update();
        if (isShootingFar) {
            shooter.update(shooter.getRegressionVelocity(shooter.getDistance(Constants.farRedShoot.getX(), Constants.farRedShoot.getY(), alliance), alliance) * multiplier);
        }
        else {
            shooter.update(shooter.getRegressionVelocity(shooter.getDistance(follower.getPose().getX(), follower.getPose().getY(), alliance), alliance) * multiplier);
        }

        if (time > START_TIME && time < START_TIME+FLYWHEEL_SPINUP_TIME) {
            transfer.reload();
            intake.custom(0.85);
        } else if (time > START_TIME+FLYWHEEL_SPINUP_TIME && time < START_TIME+FLYWHEEL_SPINUP_TIME+KICK_TIME) {
            transfer.feed();
            intake.stop();
        } else if (time > START_TIME+FLYWHEEL_SPINUP_TIME+KICK_TIME && time < START_TIME+FLYWHEEL_SPINUP_TIME*2+KICK_TIME) {
            transfer.reload();
            intake.custom(0.85);
        } else if (time > START_TIME+FLYWHEEL_SPINUP_TIME*2+KICK_TIME && time < START_TIME+FLYWHEEL_SPINUP_TIME*2+KICK_TIME*2) {
            transfer.feed();
            intake.stop();
        }
        else if (time > START_TIME+FLYWHEEL_SPINUP_TIME*2+KICK_TIME*2 && time < START_TIME+FLYWHEEL_SPINUP_TIME*3+KICK_TIME*2) {
            transfer.reload();
            intake.custom(0.85);
        }
        else if (time > START_TIME+FLYWHEEL_SPINUP_TIME*3+KICK_TIME*2 && time < START_TIME+FLYWHEEL_SPINUP_TIME*3+KICK_TIME*3) {
            transfer.feed();
            intake.stop();
        }
        else if (time > START_TIME+FLYWHEEL_SPINUP_TIME*3+KICK_TIME*3 && time < START_TIME+FLYWHEEL_SPINUP_TIME*3+KICK_TIME*3+0.3) {
            intake.custom(1);
            follower.followPath(paths.redIntakeRow1);
        }
        else if (time > START_TIME+FLYWHEEL_SPINUP_TIME*3+KICK_TIME*3+0.3+5 && time < START_TIME+FLYWHEEL_SPINUP_TIME*3+KICK_TIME*4+0.3+5) {
            intake.custom(0.85);
            transfer.reload();
        }
        else if (time > START_TIME+FLYWHEEL_SPINUP_TIME*3+KICK_TIME*4+0.3+5 && time < START_TIME+FLYWHEEL_SPINUP_TIME*3+KICK_TIME*5+0.3+5) {
            transfer.feed();
            intake.stop();
        }
        else if (time > START_TIME+FLYWHEEL_SPINUP_TIME*3+KICK_TIME*5+0.3+5 && time < START_TIME+FLYWHEEL_SPINUP_TIME*3+KICK_TIME*6+0.3+5) {
            transfer.reload();
            intake.custom(0.85);
        }
        else if (time > START_TIME+FLYWHEEL_SPINUP_TIME*3+KICK_TIME*6+0.3+5 && time < START_TIME+FLYWHEEL_SPINUP_TIME*4+KICK_TIME*6+0.3+5) {    //stoped here
            transfer.feed();
            intake.stop();
        }
        else if (time > START_TIME+FLYWHEEL_SPINUP_TIME*4+KICK_TIME*6+0.3+5&& time < START_TIME+FLYWHEEL_SPINUP_TIME*4+KICK_TIME*7+0.3+5) {
            transfer.reload();
            intake.custom(0.85);
        }
        else if (time > START_TIME+FLYWHEEL_SPINUP_TIME*4+KICK_TIME*7+0.3+5 && time < START_TIME+FLYWHEEL_SPINUP_TIME*5+KICK_TIME*7+0.3+5) {
            transfer.feed();
            intake.stop();
        }
        else if (time > START_TIME+FLYWHEEL_SPINUP_TIME*5+KICK_TIME*7+0.3+5 && time < START_TIME+FLYWHEEL_SPINUP_TIME*5+KICK_TIME*7+0.6+5) {
            intake.custom(1);
            PathChain path = new PathChain(
                    new Path(
                            new BezierCurve(
                                    Constants.farRedShoot,
                                    Actions.redRow2Control.minus(
                                            new Pose(
                                                    0,
                                                    0,
                                                    0
                                            )
                                    ),
                                    Actions.redRow2Target
                            )
                    ),
                    new Path(
                            new BezierLine(
                                    Actions.redRow2Target,
                                    new Pose(85,80)
                            )
                    )
            );
            path.firstPath().setLinearHeadingInterpolation(Constants.farRedShoot.getHeading(), 0);
            path.firstPath().setHeadingInterpolation(HeadingInterpolator.linear(
                    Constants.farRedShoot.getHeading(),
                    0,
                    0.5
            ));
            path.lastPath().setLinearHeadingInterpolation(0, SubsystemsManager.getTargetHeading(85, 80, alliance));
            follower.followPath(path);
        }
        else if (time > START_TIME+FLYWHEEL_SPINUP_TIME*5+KICK_TIME*7+0.6+11 && time < START_TIME+FLYWHEEL_SPINUP_TIME*5+KICK_TIME*8+0.6+11) {
            transfer.reload();
            intake.custom(0.85);
        }
        else if (time > START_TIME+FLYWHEEL_SPINUP_TIME*5+KICK_TIME*8+0.6+11 && time < START_TIME+FLYWHEEL_SPINUP_TIME*5+KICK_TIME*9+0.6+11) {
            transfer.feed();
            intake.stop();
        }
        else if (time > START_TIME+FLYWHEEL_SPINUP_TIME*5+KICK_TIME*9+0.6+11 && time < START_TIME+FLYWHEEL_SPINUP_TIME*6+KICK_TIME*9+0.6+11) {
            transfer.reload();
            intake.custom(0.85);
        }
        else if (time > START_TIME+FLYWHEEL_SPINUP_TIME*6+KICK_TIME*9+0.6+11 && time < START_TIME+FLYWHEEL_SPINUP_TIME*7+KICK_TIME*9+0.6+11) {
            transfer.feed();
            intake.stop();
        }
        else if (time > START_TIME+FLYWHEEL_SPINUP_TIME*7+KICK_TIME*9+0.6+11 && time < START_TIME+FLYWHEEL_SPINUP_TIME*7+KICK_TIME*10+0.6+11) {
            transfer.reload();
            intake.custom(0.85);
        }
        else if (time > START_TIME+FLYWHEEL_SPINUP_TIME*7+KICK_TIME*10+0.6+11 && time < START_TIME+FLYWHEEL_SPINUP_TIME*8+KICK_TIME*10+0.6+11) {
            transfer.feed();
            intake.stop();
        }
        else if (time > START_TIME+FLYWHEEL_SPINUP_TIME*8+KICK_TIME*10+0.6+11 && time < START_TIME+FLYWHEEL_SPINUP_TIME*8+KICK_TIME*10+0.9+11) {
            PathChain path = new PathChain(
                    new Path(
                            new BezierLine(
                                new Pose(
                                        85, 80
                                ),
                                new Pose(
                                        108, 80
                                )
                            )
                    ),
                    new Path(
                            new BezierLine(
                                    new Pose(100, 80),
                                    new Pose(85, 80)
                            )
                    )
            );
            path.firstPath()
                    .setHeadingInterpolation(
                            HeadingInterpolator.linear(
                                SubsystemsManager.getTargetHeading(85, 80, alliance),
                                0,
                                    0.3
                            )
                    );
            path.lastPath()
                    .setHeadingInterpolation(
                            HeadingInterpolator.linear(
                                    0,
                                    SubsystemsManager.getTargetHeading(85, 80, alliance),
                                    0.8
                            )
                    );

            path.setCallbacks(
                    new ParametricCallback (
                            0,
                            0.5,
                            follower,
                            () -> intake.custom(1)
                    ),
                    new ParametricCallback(
                            1,
                            0.25,
                            follower,
                            () -> intake.custom(0.35)
                    )
            );

            follower.followPath(path);
        }
        else if (time > START_TIME+FLYWHEEL_SPINUP_TIME*8+KICK_TIME*10+0.9+11 && time < START_TIME+FLYWHEEL_SPINUP_TIME*8+KICK_TIME*11+0.9+11) {
            transfer.reload();
            intake.custom(0.85);
        }
        else if (time > START_TIME+FLYWHEEL_SPINUP_TIME*8+KICK_TIME*11+0.9+11 && time < START_TIME+FLYWHEEL_SPINUP_TIME*8+KICK_TIME*12+0.9+11) {
            transfer.feed();
            intake.custom(0.85);
        }
        else if (time > START_TIME+FLYWHEEL_SPINUP_TIME*8+KICK_TIME*12+0.9+11 && time < START_TIME+FLYWHEEL_SPINUP_TIME*8+KICK_TIME*13+0.9+11) {
            transfer.reload();
            intake.custom(0.85);
        }
        else if (time > START_TIME+FLYWHEEL_SPINUP_TIME*8+KICK_TIME*13+0.9+11 && time < START_TIME+FLYWHEEL_SPINUP_TIME*8+KICK_TIME*14+0.9+11) {
            transfer.feed();
            intake.custom(0.85);
        }
    }
    @Override
    public void stop() {
        follower.update();
        Constants.teleOpStartPose = follower.getPose().copy();
    }
}
