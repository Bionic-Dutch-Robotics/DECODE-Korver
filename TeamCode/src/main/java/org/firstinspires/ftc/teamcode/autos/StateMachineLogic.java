package org.firstinspires.ftc.teamcode.autos;

import static org.firstinspires.ftc.teamcode.util.Hardware.dt;
import static org.firstinspires.ftc.teamcode.util.Hardware.intake;
import static org.firstinspires.ftc.teamcode.util.Hardware.transfer;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.util.AllianceColor;
import org.firstinspires.ftc.teamcode.util.MatchSettings;
import org.firstinspires.ftc.teamcode.util.Settings;


public class StateMachineLogic extends OpMode {
    private AutoState auto;
    private final AllianceColor alliance =new AllianceColor(AllianceColor.Selection.BLUE);
    @Override
    public void init() {
        MatchSettings.initSelection(hardwareMap, alliance, gamepad1);

    }

    @Override
    public void start() {
        MatchSettings.start();
        auto = AutoState.START;
    }

    @Override
    public void loop() {
        dt.follower.update();
        switch(auto) {
            case START:
                dt.follower.followPath(
                        new Path(
                                new BezierLine(
                                        Settings.Positions.Drivetrain.Blue.FAR_AUTO_START,
                                        Settings.Positions.Drivetrain.Blue.FAR_SHOOT
                                )
                        )
                );
                auto = AutoState.SHOOT_ONE;

            case SHOOT_ONE:
                if (!dt.follower.isBusy()) {
                    transfer.fireSortedArtifacts();
                    auto = AutoState.INTAKE_ONE;
                }

            case INTAKE_ONE:
                if (!transfer.kicker.isBusy()) {
                    transfer.cancelFire();
                    intake.run();
                    dt.follower.followPath(
                            new Path(
                                    new BezierLine(
                                            Settings.Positions.Drivetrain.Blue.FAR_SHOOT,
                                            new Pose(18, 35)
                                    )
                            )
                    );
                }
        }
    }

    @Override
    public void stop() {
        dt.follower.breakFollowing();
        dt.follower.startTeleopDrive(true);
        dt.follower.setTeleOpDrive(
                0,0,0,
        true
        );
        dt.follower.update();
    }


    enum AutoState {
        START,
                SHOOT_ONE,
                INTAKE_ONE
    }
}