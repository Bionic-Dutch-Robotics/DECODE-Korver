package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name="Test Bench Red")
public class TestBench extends OpMode {
    TestBenchBot bot;

    @Override
    public void init() {
        bot = new TestBenchBot(hardwareMap);
    }

    @Override
    public void loop() {
        bot.drivetrain(gamepad1);
        bot.intake(gamepad1);
        bot.shooter(gamepad1);

        telemetry.addData("Bot X", bot.fw.getPose().getX());
        telemetry.addData("Bot Y", bot.fw.getPose().getY());
        telemetry.addData("Bot Theta", bot.fw.getPose().getHeading());
        telemetry.addData("Shooter Speed: ", bot.shooter.shooter.getVelocity(AngleUnit.DEGREES));
        telemetry.addData("Shooter Boolean: ",
                bot.shooter.shooter.getVelocity() > 140 && bot.shooter.shooter.getVelocity() < 160
        );

        telemetry.update();
    }
}
