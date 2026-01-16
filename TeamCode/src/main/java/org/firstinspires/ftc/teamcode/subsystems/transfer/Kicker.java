package org.firstinspires.ftc.teamcode.subsystems.transfer;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.util.Settings;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@SuppressWarnings("all")
public class Kicker {
    private Servo[] kickers;
    private ElapsedTime servoTimer;
    private Integer[] order;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Future<?> future = null;

    public Kicker(HardwareMap hwMap) {
            kickers = new Servo[3];

            for (int i = 0; i < kickers.length; i++) {
                kickers[i] = hwMap.get(Servo.class, Settings.HardwareNames.Transfer.KICKERS[i]);
            }

            servoTimer = new ElapsedTime(ElapsedTime.Resolution.SECONDS);
            order = new Integer[3];
    }

    public void start() {
        this.kickAllServosDown();
    }
    public void setFireSequence(Integer[] order) {
        this.order = order;
    }

    public void runFireSequence ()  {
        this.future = this.executor.submit(this::createFireSequence);
    }
    private void createFireSequence() {
        for (int i : this.order) {
            servoTimer.reset();
            while (servoTimer.time() < Settings.Positions.Transfer.RUN_TO_POS_TIME) {
                kickServoUp(i);
            }

            servoTimer.reset();
            while (servoTimer.time() < Settings.Positions.Transfer.RUN_TO_POS_TIME) {
                kickServoDown(i);
            }
        }
    }
    public void cancelSequence() {
        if (future != null && !future.isDone()) {
            future.cancel(true);
            this.kickAllServosDown();
        }

    }

    public void kickServoUp(int servoIndex) {
        kickers[servoIndex].setPosition(Settings.Positions.Transfer.upPos[servoIndex]);
    }

    public void kickServoDown(int servoIndex) {
        kickers[servoIndex].setPosition(Settings.Positions.Transfer.downPos[servoIndex]);
    }

    public void kickAllServosDown() {
        for (int i = 0; i < kickers.length; i++) {
            this.kickServoDown(i);
        }
    }

    public Double[] getServoPositions() {
        return new Double[] {kickers[0].getPosition(), kickers[1].getPosition(), kickers[2].getPosition()};
    }

    public void stop() {
        this.cancelSequence();
        executor.shutdown();
    }
}