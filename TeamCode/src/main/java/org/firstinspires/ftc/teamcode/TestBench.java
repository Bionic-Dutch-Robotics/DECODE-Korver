package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name="Test Bench")
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

        telemetry.addData("Shooter Speed: ", bot.shooter.shooter.getVelocity(AngleUnit.DEGREES));
        telemetry.update();
    }
}
