package org.firstinspires.ftc.teamcode.autos;


import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.callbacks.ParametricCallback;
import com.pedropathing.paths.callbacks.PathCallback;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.shooter.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.transfer.Kicker;
import org.firstinspires.ftc.teamcode.subsystems.drivetrain.autonomous.Actions;
import org.firstinspires.ftc.teamcode.util.AllianceColor;

import java.util.ArrayList;

@Autonomous(name="PRELOAD PARK RED", preselectTeleOp = "Meet 2 Red")
public class PreloadParkRED extends OpMode {
    public static final Actions paths = new Actions(AllianceColor.Selection.RED);
    private boolean hasShotFirst, shoot;
    private Kicker transfer;
    private Shooter shooter;
    private Intake intake;
    private double savedTime;
    public Follower follower;

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(Constants.redStartPose);
        hasShotFirst = false;
        shoot = false;

        transfer = new Kicker(hardwareMap);
        shooter = new Shooter(hardwareMap);
        intake = new Intake(hardwareMap);
        //time = new ElapsedTime();

        savedTime = 0.0;

        ArrayList<PathCallback> callbacks = new ArrayList<>();
        callbacks.add(0,
                new ParametricCallback(1,0.001, follower,
                        () -> {
                            follower.setMaxPower(0.2);
                            intake.run();
                        }));
        callbacks.add(
                1,
                new ParametricCallback(1,0.001, follower,
                        () -> {
                            follower.setMaxPower(1);
                            intake.custom(0.1);
                        })
        );
        paths.redIntakeRow1.setCallbacks(
                callbacks
        );
    }
    @Override
    public void start() {
        resetRuntime();
        ////transfer.feed();
        follower.followPath(paths.shoot1);
    }

    @Override
    public void loop() {
        follower.update();
        shooter.update(Constants.farShootPower * shooter.redPowerCoefficient);

        if (time > 3 && time < 3.5) {
            //transfer.reload();
            intake.custom(0.81);
        } else if (time > 3.5 && time < 4.5) {
            ////transfer.feed();
            intake.stop();
        } else if (time > 4.5 && time < 5) {
            //transfer.reload();
            intake.custom(0.85);
        } else if (time > 5 && time < 6) {
            ////transfer.feed();
            intake.stop();
        } else if (time > 6 && time < 6.5) {
            //transfer.reload();
            intake.custom(0.85);
        } else if (time > 6.5 && time < 7.5) {
            ////transfer.feed();
            intake.stop();
        }
        else if (time > 7.5 && time < 7.6) {
            follower.followPath(new Path(new BezierLine(follower.getPose(), new Pose(90, 20))));
        }
    }
}
