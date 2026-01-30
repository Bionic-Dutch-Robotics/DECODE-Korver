package org.firstinspires.ftc.teamcode.tests;

import com.pedropathing.control.PIDFController;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.shooter.Flywheel;
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
    private Transfer transfer;
    @Override
    public void init() {
        transfer = new Transfer(hardwareMap);
        transfer.setMotif(new Artifact[] {Artifact.PURPLE, Artifact.GREEN, Artifact.PURPLE});
        transfer.start();
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(Constants.blueStartPose);
        shooterPidf = new PIDFController(Constants.shooterCoefficients);
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
        shooter.tilt.auto(shooter.flywheel.getDistance(follower.getPose().getX(), follower.getPose().getY(), new AllianceColor(AllianceColor.Selection.BLUE)));
        shooter.turret.loop(follower.getPose().getX(), follower.getPose().getY(), follower.getHeading());
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
        else {
            //transfer.feed();
        }

        /*telemetry.addData("Distance: ", Math.sqrt(
                Math.pow(144-follower.getPose().getX(), 2) + Math.pow(144-follower.getPose().getY(), 2)
        ));*/
        telemetry.addData("Shooter Velocity: ", shooter.flywheel.shooter.getVelocity(AngleUnit.DEGREES));
        telemetry.addData("Shooter Target: ", shooterPower);
        telemetry.update();
    }

    @Override
    public void stop() {
        transfer.kicker.stop();
    }
}
