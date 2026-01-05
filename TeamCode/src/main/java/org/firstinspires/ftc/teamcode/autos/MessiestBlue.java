package org.firstinspires.ftc.teamcode.autos;
/*
import static org.firstinspires.ftc.teamcode.pedroPathing.Constants.follower;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.shooter.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.transfer.Transfer;
import org.firstinspires.ftc.teamcode.subsystems.drivetrain.autonomous.Actions;
import org.firstinspires.ftc.teamcode.util.AllianceColor;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import java.util.concurrent.TimeUnit;

@Autonomous(name ="Sleeps Auto Blue", preselectTeleOp = "Meet 2 BLUE")
public class MessiestBlue extends OpMode {
    public static final Actions paths = new Actions(AllianceColor.Selection.BLUE);
    private boolean hasShotFirst, shoot;
    private Transfer transfer;
    private Shooter shooter;
    private Intake intake;
    private double savedTime;

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(Constants.blueStartPose);
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
        shooter.update(Constants.farShootPower);
        telemetry.update();
        telemetry.addData("Bot X", follower.getPose().getX());
        telemetry.addData("Bot Y", follower.getPose().getY());
        telemetry.addData("Bot Theta", follower.getHeading());

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
        else if (time > 7.5 && time < 7.65) {
            intake.intake();
            follower.followPath(paths.redIntakeRow1);
        }
        else if (!follower.isBusy() && time > 12 && time < 12.5) {
            intake.custom(0.85);
            transfer.reload();
        }
        else if (time > 12.5 && time < 13.5) {
            transfer.feed();
            intake.stop();
        }
        else if (time > 13.5 && time < 14) {
            transfer.reload();
            intake.custom(0.85);
        }
        else if (time > 14 && time < 15) {
            transfer.feed();
            intake.stop();
        }
        else if (time > 15 && time < 15.5) {
            transfer.reload();
            intake.custom(0.85);
        }
        else if (time > 15.5 && time < 16.5) {
            transfer.feed();
            intake.stop();
        }
        else if (time > 16.5 && time < 16.6) {
            intake.custom(0.85);
            follower.followPath(paths.redIntakeRow2);
        }
        else if (time > 22 && time < 22.5) {
            transfer.reload();
            intake.custom(0.85);
        }
        else if (time > 22.5 && time < 23.5) {
            transfer.feed();
            intake.stop();
        }
        else if (time > 23.5 && time < 24) {
            transfer.reload();
            intake.custom(0.85);
        }
        else if (time > 24 && time < 24.5) {
            transfer.feed();
            intake.stop();
        }

        else if (time > 24.5 && time < 25) {
            intake.intake();
            follower.followPath(paths.goToLever);
        }
    }
    @Override
    public void stop() {
        follower.update();
        Constants.teleOpStartPose = follower.getPose().copy();
    }
}*/
