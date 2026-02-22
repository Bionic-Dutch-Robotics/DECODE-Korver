package org.firstinspires.ftc.teamcode.tests;

import static org.firstinspires.ftc.teamcode.util.Hardware.dt;
import static org.firstinspires.ftc.teamcode.util.Hardware.intake;
import static org.firstinspires.ftc.teamcode.util.Hardware.shooter;
import static org.firstinspires.ftc.teamcode.util.Hardware.transfer;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.control.PIDFController;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.pedroPathing.tuners.Drawing;
import org.firstinspires.ftc.teamcode.util.AllianceColor;
import org.firstinspires.ftc.teamcode.util.MatchSettings;

import java.util.function.Supplier;

@Configurable
@TeleOp(name="Shooter RED")
public class RedShooterTest extends OpMode {
    public static com.pedropathing.control.PIDFController headingPid;
    public static double shooterTarget;
    public boolean runIntake, goToHeading;
    public Supplier<PathChain> toGoal;

    @Override
    public void init() {
        MatchSettings.initSelection(hardwareMap, new AllianceColor(AllianceColor.Selection.RED), gamepad1);
        MatchSettings.start();
        dt.startTeleOpDrive();
        headingPid = new PIDFController(Constants.followerConstants.getCoefficientsHeadingPIDF());
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
                        dt.getPose().getY(),
                        new AllianceColor(AllianceColor.Selection.RED)
                )
        ));

        shooter.flywheel.update(shooter.flywheel.getRegressionVelocity(
                shooter.flywheel.getDistance(
                        dt.getPose().getX(),
                        dt.getPose().getY(),
                        new AllianceColor(AllianceColor.Selection.RED)
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
                        dt.getPose().getY(),
                        new AllianceColor(AllianceColor.Selection.RED)
                )
        ));
        telemetry.update();
        Drawing.drawDebug(dt.follower);
    }
}