package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.Timer;

@Autonomous(name = "AUTO", preselectTeleOp = "FinalDrive")
public class FinalAuto extends OpMode {
    private BOTT bot;
    private State state = State.READY;
    private int shots = 0;
    private final com.pedropathing.util.Timer timer = new com.pedropathing.util.Timer();

    @Override
    public void init() {
        bot = new BOTT(hardwareMap);
        timer.resetTimer();
    }

    @Override
    public void start() {
        super.start();
        bot.shooter.midFieldShoot();
        timer.resetTimer();
        state = State.LAUNCH;
    }

    @Override
    public void loop() {
        if (shots == 4) {
            if (timer.getElapsedTimeSeconds() > 1) {
                bot.fw.breakFollowing();
                bot.fw.setTeleOpDrive(0, 0, 0, true);
            }
            return;
        }
        if (shots > 2 && timer.getElapsedTimeSeconds() > 1) {
            bot.fw.setTeleOpDrive(
                1,
            0,
            0, true
            );
            shots = 4;
            timer.resetTimer();
            return;}


        switch (state) {
            case NEXT:
                if (timer.getElapsedTimeSeconds() < 2) {break;}
                bot.intake.intake();
                bot.shooter.reload();
                timer.resetTimer();
                state = State.READY;
            case READY:
                if (timer.getElapsedTimeSeconds() < 2) {break;}
                bot.intake.stop();
                bot.shooter.midFieldShoot();
                timer.resetTimer();
                state = State.LAUNCH;
                break;
            case LAUNCH:
                if (timer.getElapsedTimeSeconds() < 3) {break;}
                bot.shooter.transfer();
                shots += 1;
                timer.resetTimer();
                state = State.NEXT;
                break;
        }
        telemetry.addData("STATE", state);
        telemetry.addData("TIME", timer.getElapsedTimeSeconds());
    }
    public enum State {
        NEXT,
        READY,
        LAUNCH
    }
}
