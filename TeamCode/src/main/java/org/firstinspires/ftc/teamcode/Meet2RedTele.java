package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.SubsystemsManager;
import org.firstinspires.ftc.teamcode.subsystems.Transfer;
import org.firstinspires.ftc.teamcode.util.AllianceColor;

@TeleOp(name="Meet 2 Red")
public class Meet2RedTele extends OpMode {
    public SubsystemsManager subsystems;
    public Shooter shooter;
    public Transfer transfer;

    @Override
    public void init() {
        subsystems = new SubsystemsManager(new AllianceColor(AllianceColor.Selection.RED), hardwareMap, gamepad1);
        shooter = new Shooter(hardwareMap, Constants.shooterCoefficients);
        transfer = new Transfer(hardwareMap);
    }

    @Override
    public void start() {
        subsystems.start();
    }

    @Override
    public void loop() {
        telemetry.update();
        telemetry.addData("Shooter:", SubsystemsManager.shooterState.name());
        telemetry.addData("hEADING: ", Constants.follower.getHeading());
        telemetry.addData("Bot X: ", Constants.follower.getPose().getX());
        telemetry.addData("Bot Y: ", Constants.follower.getPose().getY());
        subsystems.drivetrain(gamepad1);
        subsystems.intake(gamepad2);
        subsystems.shooter(gamepad2);
        subsystems.transfer(gamepad1);
        subsystems.emergencyResets(gamepad1);
    }
}
