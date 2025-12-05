package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
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

@Autonomous(name ="Sleeps Auto")
public class Messiest extends OpMode {
    public static final Actions paths = new Actions(AllianceColor.Selection.RED);
    private Follower follower;
    private boolean hasShotFirst, shoot;
    private Transfer transfer;
    private Shooter shooter;
    private Intake intake;
    private double savedTime;
    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(Constants.redStartPose);
        hasShotFirst = false;
        shoot= false;

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
        follower.followPath(paths.shoot1);
    }

    @Override
    public void loop() {
        follower.update();
        shooter.farShoot();
        intake.intake();

        if (time > 2 && time < 2.5) {
            transfer.reload();
        } else if (time > 2.5 && time < 3) {
            transfer.feed();
        } else if (time > 3 && time < 3.75) {
            transfer.reload();
        } else if (time > 2.75 && time < 4) {
            transfer.feed();
        }
        else if (time > 4 && time < 4.5) {
            transfer.reload();
        }
        else if (time > 4.5 && time < 5) {
            transfer.feed();
        }
        else if (time > 5 && time < 5.1) {
            follower.followPath(paths.redIntakeRow1);
        }
        else if (!follower.isBusy() && time > 10 && time < 1.5) {
            transfer.reload();
        }
        else if (time > 10.5 && time < 11) {
            transfer.feed();
        }
        else if (time > 11 && time < 11.5) {
            transfer.reload();
        }
        else if (time > 11.5 && time < 12) {
            transfer.feed();
        }
        else if (time > 10.1 && time < 10.15) {
            follower.followPath(paths.redIntakeRow2);
        }
        else if (!follower.isBusy() && time > 15 && time < 15.5) {
            transfer.reload();
        }
        else if (time > 15.5 && time < 16) {
            transfer.feed();
        }
        else if (time > 16.2 && time < 16.8) {
            transfer.reload();
        }

        else if (time > 16.1 && time < 16.11) {
            follower.followPath(paths.redIntakeRow3);
        }
    }
    @Override
    public void stop() {
        Constants.teleOpStartPose = follower.getPose();
    }
}
