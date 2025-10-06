package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp (name="TeleOp")
public class TeleKorver extends OpMode {
    private Bot bot;

    @Override
    public void init () {
        bot = new Bot(gamepad1, hardwareMap);
    }

    @Override
    public void start() {
        bot.dt.startTeleOpDrive(true);
    }

    @Override
    public void loop () {
        bot.drivetrain();
        bot.intake();
    }
}