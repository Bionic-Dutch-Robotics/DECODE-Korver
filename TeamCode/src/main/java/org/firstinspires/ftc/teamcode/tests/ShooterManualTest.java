package org.firstinspires.ftc.teamcode.tests;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Transfer;

@TeleOp(name="SHOOTER Manual")
public class ShooterManualTest extends OpMode {
    public Follower follower;
    public Shooter shooter;
    public Transfer transfer;
    public Intake intake;
    public double shooterPower;
    @Override
    public void init() {
        shooter = new Shooter(hardwareMap, Constants.shooterCoefficients);
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(Constants.redStartPose);
        intake = new Intake(hardwareMap);
        transfer = new Transfer(hardwareMap);
        shooterPower = 250;
    }

    @Override
    public void start() {
        follower.startTeleopDrive(false);
    }
    @Override
    public void loop() {
        follower.update();
        follower.setTeleOpDrive(
                -gamepad1.left_stick_y,
                -gamepad1.left_stick_x,
                -gamepad1.right_stick_x,
                true
        );

        shooter.update(shooterPower);

        if (gamepad1.aWasPressed()) {
            shooterPower += 10;
        }
        else if (gamepad1.bWasPressed()) {
            shooterPower -= 10;
        }

        intake.intake();

        if (gamepad1.x) {
            transfer.reload();
        }
        else {
            transfer.feed();
        }

        telemetry.addData("Distance: ", Math.sqrt(
                Math.pow(144-follower.getPose().getX(), 2) + Math.pow(144-follower.getPose().getY(), 2)
        ));
        telemetry.addData("Shooter Power: ", shooter.getTarget());
        telemetry.update();
    }
}
