package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.BOTT;

@TeleOp(name = "FinalDrive")
public class FinalDrive extends OpMode {
    private BOTT bot;

    @Override
    public void init() {
        bot = new BOTT(hardwareMap);
    }

    @Override
    public void loop() {
        bot.drivetrain(gamepad1);
        bot.transfer(gamepad1);
        bot.intake(gamepad2);
        bot.shooter(gamepad2);

        telemetry.addData("Shoot: ", bot.shooter.shooterState.name());
        telemetry.addData("BotX:", bot.fw.getPose().getX());
        telemetry.addData("BotY", bot.fw.getPose().getY());
        telemetry.addData("BotTheta", bot.fw.getPose().getHeading());
        telemetry.update();
    }
}
