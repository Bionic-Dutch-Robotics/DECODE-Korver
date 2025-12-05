package org.firstinspires.ftc.teamcode;

import static java.lang.Thread.sleep;

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

@Autonomous(name="Auto State Machinw")
public class AutoWithStateMachine extends OpMode {
    private States states;
    private Follower follower;
    private Shooter shooter;
    private Actions paths;
    private Transfer transfer;
    private double savedTime, currentTime;
    private Intake intake;
    private boolean shoot1Happened;
    private ElapsedTime time;
    @Override
    public void init() {
        states = States.START;
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(Constants.redStartPose);
        paths = new Actions(AllianceColor.Selection.RED);
        shooter = new Shooter(hardwareMap, Constants.shooterCoefficients);
        transfer = new Transfer(hardwareMap);
        intake = new Intake(hardwareMap);
        time = new ElapsedTime();
        shoot1Happened = false;
    }

    @Override
    public void start() {
        transfer.feed();
        time.reset();
    }

    @Override
    public void loop() {
        currentTime = time.time(TimeUnit.SECONDS);
        follower.update();
        telemetry.update();

        switch (states) {
            case START:
                follower.followPath(paths.shoot1);
                states = States.SHOOT_ZERO;
                shooter.idle();


            case SHOOT_ZERO:
                shooter.farShoot();
                if (!follower.isBusy()) {
                    transfer.reload();
                    if (!shoot1Happened) {
                        savedTime = time.time(TimeUnit.SECONDS);
                        shoot1Happened = true;
                    }
                    telemetry.addData("CTIME: ", currentTime);
                    telemetry.addData("savedTime: ", savedTime);

                    transfer.reload();
                    try {
                        sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    transfer.feed();
                    try {
                        sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    transfer.reload();
                    try {
                        sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    transfer.feed();

                    if (currentTime-savedTime > 0.25 && currentTime-savedTime < 0.75) {
                        transfer.feed();
                    }
                    else if (currentTime-savedTime > 0.75 && currentTime-savedTime < 1.25) {
                        transfer.reload();
                    }
                    else if (currentTime-savedTime > 1.25 && currentTime-savedTime < 1.5) {
                        transfer.feed();
                    }
                    else if (currentTime-savedTime > 1.5 && currentTime-savedTime < 2) {
                        transfer.reload();
                    }
                    else if (currentTime-savedTime > 2 && currentTime-savedTime < 2.25) {
                        transfer.feed();
                    }
                    else if (currentTime-savedTime > 2.25 && currentTime-savedTime < 2.75) {
                        transfer.reload();
                    }
                    else if (currentTime-savedTime > 2.75) {
                        states = States.INTAKE_ONE;
                        follower.followPath(paths.redIntakeRow1);
                    }
                }

            case INTAKE_ONE:
                telemetry.addLine(":)");
        }
    }

    private enum States {
        START,
        SHOOT_ZERO,
        INTAKE_ONE,
        SHOOT_ONE,
        INTAKE_TWO,
        SHOOT_TWO,

    }
}
