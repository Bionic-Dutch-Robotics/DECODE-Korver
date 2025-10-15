package org.firstinspires.ftc.teamcode;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.List;


@SuppressWarnings("all")
@TeleOp (name="TeleKorver")
public class TeleKorver extends OpMode {
    private Bot bot;
    private Pose drivePower;

    @Override
    public void init () {
        try {
            bot = new Bot(gamepad1, hardwareMap);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        drivePower = new Pose();
    }

    @Override
    public void start() {
        bot.dt.setBrakeMode(true);
    }

    @Override
    public void loop () {
        drivePower = new Pose(
                gamepad1.left_stick_x,
                -gamepad1.left_stick_y,
                gamepad1.right_stick_x
        );
        bot.drivetrain(false, gamepad1);
    }
}
