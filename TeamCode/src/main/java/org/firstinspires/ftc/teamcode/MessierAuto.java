package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Transfer;
import org.firstinspires.ftc.teamcode.subsystems.drivetrain.autonomous.Actions;
import org.firstinspires.ftc.teamcode.util.AllianceColor;

import java.util.concurrent.TimeUnit;

@Autonomous(name="TIME BASED")
public class MessierAuto extends OpMode {

    public boolean hasShotFirst, shoot;
    public Follower follower;
    public Shooter shooter;
    public Intake intake;
    public Transfer transfer;
    public double savedTime;
    public ElapsedTime time;
    public final Actions paths = new Actions(AllianceColor.Selection.RED);
    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(Constants.redStartPose);
        hasShotFirst = false;
        shoot= false;

        transfer = new Transfer(hardwareMap);
        shooter = new Shooter(hardwareMap, Constants.shooterCoefficients);
        intake = new Intake(hardwareMap);
        time = new ElapsedTime();

        savedTime = 0.0;
    }

    @Override
    public void start() {
        follower.followPath(paths.shoot1);
        time.reset();
    }

    @Override
    public void loop() {
        follower.update();
        shooter.farShoot();
        if (!follower.isBusy() && !hasShotFirst){       //RUNS ONCE
            savedTime = time.time(TimeUnit.SECONDS);
            shoot = true;
            hasShotFirst = true;
        }

        if (shoot) {
            double currentTime = time.time(TimeUnit.SECONDS);
            if (currentTime - savedTime > 1 && currentTime - savedTime < 1.75) {
                transfer.reload();
                intake.intake();
            }
            else if (currentTime - savedTime > 3.5 && currentTime - savedTime < 4) {
                transfer.feed();
            }
            else if (currentTime - savedTime > 4 && currentTime - savedTime < 4.25) {
                transfer.reload();
            }
            else if (currentTime - savedTime > 4.25 && currentTime - savedTime < 4.75) {
                transfer.feed();
            }
            else if (time.time(TimeUnit.SECONDS) - savedTime > 7.5) {
                shoot = false;
                follower.followPath(paths.redIntakeRow1);
            }
        }
        else {
            transfer.feed();
        }
    }
}
