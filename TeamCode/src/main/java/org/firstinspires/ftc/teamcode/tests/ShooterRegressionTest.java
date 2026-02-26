package org.firstinspires.ftc.teamcode.tests;

import static org.firstinspires.ftc.teamcode.util.Hardware.dt;
import static org.firstinspires.ftc.teamcode.util.Hardware.intake;
import static org.firstinspires.ftc.teamcode.util.Hardware.shooter;
import static org.firstinspires.ftc.teamcode.util.Hardware.transfer;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedroPathing.tuners.Drawing;
import org.firstinspires.ftc.teamcode.util.AllianceColor;
import org.firstinspires.ftc.teamcode.util.MatchSettings;

@Configurable
@TeleOp(name="Shooter")
public class ShooterRegressionTest extends OpMode {
    public static double comp;

    public boolean runIntake, goToHeading;

    @Override
    public void init() {
        MatchSettings.initSelection(hardwareMap, new AllianceColor(AllianceColor.Selection.BLUE), gamepad1);
        MatchSettings.start();
        dt.startTeleOpDrive();
        runIntake = false;
        goToHeading = false;
    }

    @Override
    public void start() {
        MatchSettings.start();
    }

    @Override
    public void loop() {
        {
            Drawing.drawDebug(dt.follower);
            if (gamepad1.dpadUpWasPressed()) {
                runIntake = !runIntake;
            }
            if (runIntake) {
                intake.run();
            } else {
                intake.stop();
            }
        }
        {
            if (gamepad1.aWasPressed()) {
                transfer.fireSortedArtifacts();
            }
        }

        if (gamepad1.leftBumperWasPressed()) {
            shooter.turret.setLiveOffset(-0.05);
        }
        else if (gamepad1.rightBumperWasPressed()) {
            shooter.turret.setLiveOffset(0.05);
        }

        if (gamepad1.leftTriggerWasPressed()) {
            shooter.flywheel.setVoltageComp(shooter.flywheel.getVoltageComp() + 0.02);
        }
        else if (gamepad1.rightTriggerWasPressed()) {
            shooter.flywheel.setVoltageComp(shooter.flywheel.getVoltageComp() - 0.02);
        }

        if (gamepad1.bWasPressed()) {
            intake.toggle();
        }


        dt.update();
        if (!goToHeading) {
            dt.follower.setTeleOpDrive(
                    -gamepad1.left_stick_y,
                    -gamepad1.left_stick_x,
                    -gamepad1.right_stick_x,
                    true
            );
        }

        shooter.tilt.setTilt(shooter.tilt.auto(
                shooter.flywheel.getDistance(
                        dt.getPose().getX(),
                        dt.getPose().getY()
                )
        ));

        shooter.flywheel.update(shooter.flywheel.getRegressionVelocity(
                shooter.flywheel.getDistance(
                        dt.getPose().getX(),
                        dt.getPose().getY()
                )
        ));

        shooter.turret.loop(
                dt.getPose()
        );


        telemetry.addData("bot X", dt.getPose().getX());
        telemetry.addData("bot Y: ", dt.getPose().getY());
        telemetry.addData("bot Heading: ", dt.getPose().getHeading());
        telemetry.addData("Target Velocity: ", shooter.flywheel.getRegressionVelocity(
                shooter.flywheel.getDistance(
                        dt.getPose().getX(),
                        dt.getPose().getY()
                )
        ));
        telemetry.addData("Turret Target", shooter.turret.getTargetRadians(dt.getPose()));
        telemetry.addData("Turret Pos", shooter.turret.convertTicksToRadians(shooter.turret.turret.getCurrentPosition()));
        telemetry.update();
    }
}