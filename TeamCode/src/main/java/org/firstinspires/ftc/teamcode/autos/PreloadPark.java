package org.firstinspires.ftc.teamcode.autos;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.shooter.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.transfer.Kicker;
import org.firstinspires.ftc.teamcode.subsystems.drivetrain.autonomous.Actions;
import org.firstinspires.ftc.teamcode.util.AllianceColor;

@Autonomous(name="Preload Park")
public class PreloadPark extends OpMode {
    public static final Actions paths = new Actions(AllianceColor.Selection.BLUE);
    private Follower follower;
    private boolean hasShotFirst, shoot;
    private Kicker transfer;
    private Shooter shooter;
    private Intake intake;
    private double savedTime;

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(Constants.blueStartPose);
        hasShotFirst = false;
        shoot= false;

        transfer = new Kicker(hardwareMap);
        shooter = new Shooter(hardwareMap);
        intake = new Intake(hardwareMap);
        //time = new ElapsedTime();

        savedTime = 0.0;
    }

    @Override
    public void start() {
        resetRuntime();
        //transfer.feed();
        follower.followPath(paths.shoot1);
    }

    @Override
    public void loop() {
        follower.update();
        shooter.farShoot();

        if (time > 3 && time < 3.5) {
            //transfer.reload();
            intake.custom(0.81);
        } else if (time > 3.5 && time < 4.5) {
            //transfer.feed();
            intake.stop();
        } else if (time > 4.5 && time < 5) {
            //transfer.reload();
            intake.custom(0.85);
        } else if (time > 5 && time < 6) {
            //transfer.feed();
            intake.stop();
        }
        else if (time > 6 && time < 6.5) {
            //transfer.reload();
            intake.custom(0.85);
        }
        else if (time > 6.5 && time < 7.5) {
            //transfer.feed();
            intake.stop();
        }
        else if (time > 7.5 && time < 7.65) {
            intake.run();
            follower.followPath(new Path(new BezierLine(
                    follower.getPose(),
                    new Pose(
                            30, 20
                    )
            )));
        }
        /*else if (!follower.isBusy() && time > 12 && time < 12.5) {
            run.custom(0.85);
            //transfer.reload();
        }
        else if (time > 12.5 && time < 13.5) {
            //transfer.feed();
            run.stop();
        }
        else if (time > 13.5 && time < 14) {
            //transfer.reload();
            run.custom(0.85);
        }
        else if (time > 14 && time < 15) {
            //transfer.feed();
            run.stop();
        }
        else if (time > 15 && time < 15.5) {
            //transfer.reload();
            run.custom(0.85);
        }
        else if (time > 15.5 && time < 16.5) {
            //transfer.feed();
            run.stop();
        }
        else if (time > 16.5 && time < 16.6) {
            run.custom(0.85);
            follower.followPath(paths.redIntakeRow2);
        }
        else if (time > 22 && time < 22.5) {
            //transfer.reload();
            run.custom(0.85);
        }
        else if (time > 22.5 && time < 23.5) {
            //transfer.feed();
            run.stop();
        }
        else if (time > 23.5 && time < 24) {
            //transfer.reload();
            run.custom(0.85);
        }
        else if (time > 24 && time < 24.5) {
            //transfer.feed();
            run.stop();
        }

        else if (time > 24.5 && time < 25) {
            run.run();
            follower.followPath(paths.goToLever);
        }*/
    }
}
