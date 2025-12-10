package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Transfer;
import org.firstinspires.ftc.teamcode.subsystems.drivetrain.autonomous.Actions;
import org.firstinspires.ftc.teamcode.util.AllianceColor;

import java.util.concurrent.TimeUnit;

@Autonomous(name ="Close Red")
public class MessyRedClose extends OpMode {
    public static final Actions paths = new Actions(AllianceColor.Selection.RED);
    public static Path toCloseShoot = new Path(new BezierLine(
            Constants.redCloseStart,
            Constants.redCloseShoot
    ));
    private Follower follower;
    private boolean hasShotFirst, shoot;
    private Transfer transfer;
    private Shooter shooter;
    private Intake intake;
    private double savedTime;

    @Override
    public void init() {
        toCloseShoot.setLinearHeadingInterpolation(
                Constants.redCloseStart.getHeading(),
                Constants.redCloseShoot.getHeading()
        );

        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(Constants.redCloseStart);
        hasShotFirst = false;
        shoot = false;

        transfer = new Transfer(hardwareMap);
        shooter = new Shooter(hardwareMap, Constants.shooterCoefficients);
        intake = new Intake(hardwareMap);
        //time = new ElapsedTime();

        savedTime = 0.0;
    }
    @Override
    public void start() {
        resetRuntime();
        transfer.feed();
        follower.followPath(toCloseShoot);
    }

    @Override
    public void loop() {
        follower.update();
        shooter.midFieldShoot();

        if (time > 3 && time < 3.5) {
            transfer.reload();
            intake.custom(0.81);
        } else if (time > 3.5 && time < 4.5) {
            transfer.feed();
            intake.stop();
        } else if (time > 4.5 && time < 5) {
            transfer.reload();
            intake.custom(0.85);
        } else if (time > 5 && time < 6) {
            transfer.feed();
            intake.stop();
        }
        else if (time > 6 && time < 6.5) {
            transfer.reload();
            intake.custom(0.85);
        }
        else if (time > 6.5 && time < 7.5) {
            transfer.feed();
            intake.stop();
        }
    }
    @Override
    public void stop() {
        follower.update();
        Constants.teleOpStartPose = follower.getPose().copy();
    }
}
