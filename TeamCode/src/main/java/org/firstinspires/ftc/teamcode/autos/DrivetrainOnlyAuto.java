package org.firstinspires.ftc.teamcode.autos;

import static org.firstinspires.ftc.teamcode.util.Hardware.dt;
import static org.firstinspires.ftc.teamcode.util.Hardware.intake;
import static org.firstinspires.ftc.teamcode.util.Hardware.shooter;
import static org.firstinspires.ftc.teamcode.util.Hardware.transfer;

import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.subsystems.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.util.AllianceColor;
import org.firstinspires.ftc.teamcode.util.MatchSettings;

@Disabled
@Autonomous(name="Drivetrain Only")
public class DrivetrainOnlyAuto extends OpMode {
    private Path path;
    private AutoState state;

    @Override
    public void init() {
        MatchSettings.initSelection(hardwareMap, new AllianceColor(AllianceColor.Selection.BLUE), gamepad1);
        MatchSettings.start();
    }

    @Override
    public void start() {
        path = new Path(
                new BezierCurve(
                        dt.follower.getPose(),
                        new Pose(45, 32),
                        new Pose(25,30,dt.follower.getPose().getHeading())
                )
        );
        path.setConstantHeadingInterpolation(Math.PI);
        dt.follower.followPath(path);
        state = AutoState.INTAKE;
    }

    @Override
    public void loop() {
        shooter.flywheel.adaptive(
                dt.follower.getPose().getX(),
                dt.follower.getPose().getY()
        );
        shooter.turret.loop(
                dt.follower.getPose()
        );
        dt.update();

        if (!dt.follower.isBusy() && state.equals(AutoState.INTAKE)) {
            intake.run();
            dt.follower.breakFollowing();
            Path path1 = new Path(
                    new BezierLine(
                            dt.follower.getPose(),
                            new Pose(50, 15)
                    )
            );
            path1.setConstantHeadingInterpolation(Math.PI);
            dt.follower.followPath(
                    path1
            );
            state = AutoState.SHOOT;
        }
        else if (!dt.follower.isBusy() && state.equals(AutoState.SHOOT)) {
            dt.follower.breakFollowing();
            intake.stop();
            transfer.fireSortedArtifacts();
        }
    }
    enum AutoState {
        INTAKE,
        SHOOT
    }
}
