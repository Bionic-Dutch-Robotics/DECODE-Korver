package org.firstinspires.ftc.teamcode.tests;

import com.pedropathing.control.FilteredPIDFController;
import com.pedropathing.control.PIDFController;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.MathFunctions;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Transfer;

@TeleOp(name="SHOOTER Manual")
public class ShooterManualTest extends OpMode {
    //public Follower follower;
    public Shooter shooter;
    public PIDFController shooterPidf;
    //public Transfer transfer;
    //public Intake intake;
    //public PIDFController headingPid;
    public boolean goToHeading;
    public double shooterPower;
    @Override
    public void init() {
        shooterPidf = new PIDFController(Constants.shooterCoefficients);
        shooter = new Shooter(hardwareMap, Constants.shooterCoefficients);
        /*follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(Constants.redStartPose);*/
        //intake = new Intake(hardwareMap);
        //transfer = new Transfer(hardwareMap);
        shooterPower = 250;
        //headingPid = new PIDFController(follower.getConstants().getCoefficientsHeadingPIDF());
        goToHeading = false;
    }

    @Override
    public void start() {
        /*follower.startTeleopDrive(false);*/
    }
    @Override
    public void loop() {
        /*follower.update();
        if (!goToHeading) {
            follower.setTeleOpDrive(
                    -gamepad1.left_stick_y,
                    -gamepad1.left_stick_x,
                    -gamepad1.right_stick_x,
                    true
            );
        }
        else {
            headingPid.updatePosition(follower.getHeading());
            headingPid.setTargetPosition(shootertest.getRedTargetHeading(141-follower.getPose().getX(), 141-follower.getPose().getY()));
            follower.setTeleOpDrive(
                    -gamepad1.left_stick_y,
                    -gamepad1.left_stick_x,
                    headingPid.run(),
                    false
            );
        }*/
        /*if (gamepad1.leftBumperWasPressed()) {
            goToHeading = !goToHeading;

            if (!goToHeading) {
                follower.breakFollowing();
                follower.startTeleopDrive(false);
            }
        }*/

        //shooterPidf.updatePosition(shooter.getVelocity(AngleUnit.DEGREES));
        //shooterPidf.setTargetPosition(shooterPower);
        shooter.update(shooterPower);

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

        //intake.intake();

        if (gamepad1.x) {
            //transfer.reload();
        }
        else {
            //transfer.feed();
        }

        /*telemetry.addData("Distance: ", Math.sqrt(
                Math.pow(144-follower.getPose().getX(), 2) + Math.pow(144-follower.getPose().getY(), 2)
        ));*/
        telemetry.addData("Shooter Velocity: ", shooter.shooter.getVelocity(AngleUnit.DEGREES));
        telemetry.addData("Shooter Target: ", shooterPower);
        telemetry.update();
    }
}
