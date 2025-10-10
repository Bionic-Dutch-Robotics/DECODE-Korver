package org.firstinspires.ftc.teamcode;

import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.List;


@SuppressWarnings("all")
@TeleOp (name="TeleOp")
public class TeleKorver extends OpMode {
    private Bot bot;
    private Pose drivePower = new Pose();

    @Override
    public void init () {
        bot = new Bot(gamepad1, hardwareMap);

        List<LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }
    }

    @Override
    public void start() {
        bot.dt.startTeleOpDrive(true);
    }

    @Override
    public void loop () {
        drivePower = new Pose(
                gamepad1.left_stick_x,
                -gamepad1.left_stick_y,
                gamepad1.right_stick_x
        );
        bot.drivetrain(drivePower, false);
        bot.intake();
        bot.log();
    }
}