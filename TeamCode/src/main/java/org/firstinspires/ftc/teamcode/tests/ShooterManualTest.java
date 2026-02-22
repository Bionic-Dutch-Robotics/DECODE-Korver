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
    private double tiltAngle = 0.1;
    @Override
    public void init() {
        intake = new Intake(hardwareMap);
        transfer = new Transfer(hardwareMap);
        transfer.setMotif(new Artifact[] {Artifact.PURPLE, Artifact.GREEN, Artifact.PURPLE});
        transfer.start();
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(Settings.Positions.Drivetrain.Blue.FAR_AUTO_START);
        shooterPidf = new PIDFController(Settings.Positions.Shooter.SHOOTER_COEFFICIENTS);
        shooter = new Shooter(hardwareMap);
        shooter.setAlliance(new AllianceColor(AllianceColor.Selection.BLUE));
        shooter.turret.setAlliance(new AllianceColor(AllianceColor.Selection.BLUE));
        shooterPower = 50;
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
            tiltAngle += 0.05;
        }
        else if (gamepad1.rightBumperWasPressed()) {
            tiltAngle -= 0.05;
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

        Vector velocity = follower.getVelocity();
        double headingVel = follower.getAngularVelocity();
        Pose predictedPose = follower.getPose().copy();
        if (velocity.getMagnitude() > 0.5) {
            predictedPose = follower.getPose().plus(
                    new Pose(
                            velocity.getXComponent(),
                            velocity.getYComponent(),
                            headingVel
                    ).times(Settings.Positions.Transfer.RUN_TO_POS_TIME /*0.05*/)
            );
        }

        shooter.turret.loop(predictedPose);
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

        if (gamepad1.xWasPressed()) {
            transfer.fireSortedArtifacts();
        }
        intake.run();
        telemetry.addData("Shooter Velocity: ", shooter.flywheel.shooter.getVelocity(AngleUnit.DEGREES));
        telemetry.addData("Shooter Velocity: ", shooter.flywheel.shooter2.getVelocity(AngleUnit.DEGREES));
        telemetry.addData("Shooter Target: ", shooterPower);
        telemetry.addData("Distance: ", shooter.flywheel.getDistance(follower.getPose().getX(), follower.getPose().getY(), new AllianceColor(AllianceColor.Selection.BLUE)));
        telemetry.addData("tilt", tiltAngle);
        telemetry.update();
    }

    @Override
    public void stop() {
        transfer.kicker.stop();
    }
}
