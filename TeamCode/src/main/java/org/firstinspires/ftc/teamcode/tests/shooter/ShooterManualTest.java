package org.firstinspires.ftc.teamcode.tests.shooter;

import static org.firstinspires.ftc.teamcode.util.Hardware.dt;
import static org.firstinspires.ftc.teamcode.util.Hardware.intake;
import static org.firstinspires.ftc.teamcode.util.Hardware.shooter;
import static org.firstinspires.ftc.teamcode.util.Hardware.transfer;

import com.pedropathing.control.PIDFController;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.util.AllianceColor;
import org.firstinspires.ftc.teamcode.util.MatchSettings;

@TeleOp(name="SHOOTER Manual")
public class ShooterManualTest extends OpMode {
    //public Follower follower;
    public PIDFController shooterPidf;
    //public Intake run;
    //public PIDFController headingPid;
    public boolean goToHeading;
    public double shooterPower;
    private double tiltAngle = 0.1;
    @Override
    public void init() {
        MatchSettings.initSelection(hardwareMap, new AllianceColor(AllianceColor.Selection.BLUE), gamepad1);
        shooterPower = 50;
    }

    @Override
    public void init_loop() {
        telemetry.update();
        MatchSettings.refreshMotif(telemetry);
    }
    @Override
    public void start() {
        MatchSettings.start();
        dt.follower.startTeleopDrive(false);
    }
    @Override
    public void loop() {
        shooter.tilt.setTilt(tiltAngle);
        if (gamepad1.leftBumperWasPressed()) {
            tiltAngle += 0.025;
        }
        else if (gamepad1.rightBumperWasPressed()) {
            tiltAngle -= 0.025;
        }

        if (gamepad1.dpadLeftWasPressed()) {
            shooter.turret.setLiveOffset(0.05);
        }
        else if (gamepad1.dpadRightWasPressed()) {
            shooter.turret.setLiveOffset(-0.05);
        }
        dt.update();
            dt.teleOpDrive(
                    -gamepad1.left_stick_y,
                    -gamepad1.left_stick_x,
                    -gamepad1.right_stick_x
            );

        Pose predictedPose = shooter.getPredictedPose(
                dt.getPose(),
                dt.follower.getVelocity(),
                dt.follower.getAngularVelocity()
        );

        shooter.turret.loop(predictedPose);
        //shooter.flywheel.update(shooterPower);
        shooter.flywheel.update(
                shooter.flywheel.getRegressionVelocity(
                        shooter.getFlywheel().getDistance(predictedPose.getX(),
                                predictedPose.getY())));

        if (gamepad1.aWasPressed()) {
            shooterPower += 10;
        }
        else if (gamepad1.bWasPressed()) {
            shooterPower -= 10;
        }
        if (gamepad1.dpadUpWasPressed()) {
            shooterPower +=1;
        }
        else if (gamepad1.dpadDownWasPressed()) {
            shooterPower -= 1;
        }

        if (gamepad1.xWasPressed()) {
            transfer.fireSortedArtifacts();
        }

        if (gamepad1.dpadDownWasPressed()) {
            intake.toggle();
        }

        if (gamepad1.leftStickButtonWasPressed()) {
            shooter.flywheel.setVoltageComp(shooter.flywheel.getVoltageComp() + 0.05);
        }
        else if (gamepad1.rightStickButtonWasPressed()) {
            shooter.flywheel.setVoltageComp(shooter.flywheel.getVoltageComp() - 0.05);
        }
        intake.run();
        telemetry.addData("Shooter Velocity: ", shooter.flywheel.shooter.getVelocity(AngleUnit.DEGREES));
        telemetry.addData("Shooter Velocity: ", shooter.flywheel.shooter2.getVelocity(AngleUnit.DEGREES));
        telemetry.addData("Shooter Target: ", shooterPower);
        telemetry.addData("Distance: ", shooter.flywheel.getDistance(dt.getPose().getX(), dt.getPose().getY()));
        telemetry.addData("tilt", tiltAngle);
        telemetry.update();
    }

    @Override
    public void stop() {
        MatchSettings.vision.stop();
        transfer.kicker.stop();
    }
}
