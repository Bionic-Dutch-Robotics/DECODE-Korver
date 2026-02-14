package org.firstinspires.ftc.teamcode.tests;

import com.pedropathing.control.PIDFController;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.math.Vector;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.shooter.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.transfer.Transfer;
import org.firstinspires.ftc.teamcode.util.AllianceColor;
import org.firstinspires.ftc.teamcode.util.Artifact;
import org.firstinspires.ftc.teamcode.util.Settings;

@TeleOp(name="SHOOTER Manual")
public class ShooterManualTest extends OpMode {
    //public Follower follower;
    public Shooter shooter;
    public PIDFController shooterPidf;
    private Follower follower;
    //public Transfer transfer;
    //public Intake run;
    //public PIDFController headingPid;
    public boolean goToHeading;
    public double shooterPower;
    private Intake intake;
    private Transfer transfer;
    private double tiltAngle = 0.15;
    @Override
    public void init() {
        intake = new Intake(hardwareMap);
        transfer = new Transfer(hardwareMap);
        transfer.setMotif(new Artifact[] {Artifact.PURPLE, Artifact.GREEN, Artifact.PURPLE});
        transfer.start();
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(Constants.blueStartPose);
        shooterPidf = new PIDFController(Settings.Positions.Shooter.SHOOTER_COEFFICIENTS);
        shooter = new Shooter(hardwareMap);
        shooter.setAlliance(new AllianceColor(AllianceColor.Selection.BLUE));
        shooter.turret.setAlliance(new AllianceColor(AllianceColor.Selection.BLUE));
        //follower = Constants.createFollower(hardwareMap);
        //run = new Intake(hardwareMap);
        //transfer = new Transfer(hardwareMap);
        shooterPower = 250;
        //headingPid = new PIDFController(follower.getConstants().getCoefficientsHeadingPIDF());
        goToHeading = false;
    }

    @Override
    public void start() {
        follower.startTeleopDrive(false);
    }
    @Override
    public void loop() {
        shooter.tilt.setTilt(tiltAngle);
        if (gamepad1.leftBumperWasPressed()) {
            tiltAngle += 0.01;
        }
        else if (gamepad1.rightBumperWasPressed()) {
            tiltAngle -= 0.01;
        }
        follower.update();
        if (!goToHeading) {
            follower.setTeleOpDrive(
                    -gamepad1.left_stick_y,
                    -gamepad1.left_stick_x,
                    -gamepad1.right_stick_x,
                    true
            );
        }
        /*if (gamepad1.leftBumperWasPressed()) {
            goToHeading = !goToHeading;

            if (!goToHeading) {
                follower.breakFollowing();
                follower.startTeleopDrive(false);
            }
        }*/

        //shooterPidf.updatePosition(shooter.getVelocity(AngleUnit.DEGREES));
        //shooterPidf.setTargetPosition(shooterPower);
        //shooter.tilt.setTilt(
        //        shooter.tilt.auto(shooter.flywheel.getDistance(follower.getPose().getX(), follower.getPose().getY(), new AllianceColor(AllianceColor.Selection.BLUE))
        //));

        shooter.turret.loop(follower.getPose().getX(), follower.getPose().getY(), follower.getPose().getHeading());
        shooter.flywheel.update(shooterPower);

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

        //run.run();
        if (gamepad1.xWasPressed()) {
            transfer.fireSortedArtifacts();
        }
        intake.run();
        telemetry.addData("Shooter Velocity: ", shooter.flywheel.shooter.getVelocity(AngleUnit.DEGREES));
        telemetry.addData("Shooter Target: ", shooterPower);
        telemetry.addData("Distance: ", shooter.flywheel.getDistance(follower.getPose().getX(), follower.getPose().getY(), new AllianceColor(AllianceColor.Selection.BLUE)));
        telemetry.update();
    }

    @Override
    public void stop() {
        transfer.kicker.stop();
    }
}
