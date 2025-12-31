package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.pedroPathing.Constants.shooterCoefficients;
import static java.lang.Thread.sleep;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Transfer;

import java.util.ArrayList;
import java.util.HashMap;

@Autonomous(name="6 Ball RED")
public class Red6Ball extends OpMode {
    private enum AutoState {
        START,
        SHOOT_POS,
        SHOOT_THREE,
        ACCELERATE,
        TRANSFER
    }

    private AutoState autoState;
    private Shooter shooter;
    private Follower fw;
    private Intake intake;
    private Transfer transfer;
    private HashMap<String, Path> paths;
    private PathChain pathChain;
    private double savedTime;

    @Override
    public void init() {
        shooter = new Shooter(hardwareMap, shooterCoefficients);
        fw = Constants.createFollower(hardwareMap);
        intake = new Intake(hardwareMap);
        transfer = new Transfer(hardwareMap);
        autoState = AutoState.START;

        paths = new HashMap<>();

        paths.put("goToFarShoot", new Path(new BezierLine(Constants.redStartPose, Constants.farRedShoot)));
        paths.put("exitTriangle", new Path(new BezierLine(Constants.farRedShoot, Constants.farRedShoot)));
    }

    @Override
    public void loop() {
        switch (autoState) {
            case START:
                fw.followPath(paths.get("goToFarShoot"));

                transfer.feed();

                if (!fw.isBusy()) {
                    autoState = AutoState.SHOOT_POS;
                }

            case SHOOT_POS:
                autoState = AutoState.SHOOT_THREE;
                savedTime = time;

            case ACCELERATE:
                shooter.farShoot();

                if (shooter.shooter.getVelocity(AngleUnit.DEGREES) > 160 &&
                        shooter.shooter.getVelocity(AngleUnit.DEGREES) < 180) {
                    autoState = AutoState.TRANSFER;
                }

            case TRANSFER:
                intake.intake();
                transfer.reload();
                savedTime = time;

                if (time - savedTime > 5) {
                    transfer.feed();
                    intake.stop();
                    autoState = AutoState.ACCELERATE;
                }
        }
    }
}
