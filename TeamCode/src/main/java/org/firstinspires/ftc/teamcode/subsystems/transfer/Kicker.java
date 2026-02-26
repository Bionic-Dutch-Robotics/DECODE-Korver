package org.firstinspires.ftc.teamcode.subsystems.transfer;

import static org.firstinspires.ftc.teamcode.util.Settings.Positions.Transfer.RUN_TO_POS_TIME;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.util.Settings;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@SuppressWarnings("all")
//TODO: Add limiting so only one fire sequence can run at a time
public class Kicker {
    private volatile Servo[] kickers;
    private boolean run = false;
    private ElapsedTime servoTimer;
    private volatile Integer[] order;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Future<?> future = null;
    private boolean isBusy = false;

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

    public void runFireSequence (Integer[] order)  {
        this.order = order;
        this.cancelSequence();
        this.future = this.executor.submit(this::createFireSequence);
    }
    public void createFireSequence() {
        for (int i=0; i < order.length; i++) {
            servoTimer.reset();
            kickServoUp(order[i]);
            try {
                Thread.sleep((long) (RUN_TO_POS_TIME * 1000));
            } catch (InterruptedException ignored) {
            }

            servoTimer.reset();
            kickServoDown(order[i]);
            try {
                Thread.sleep((long) (RUN_TO_POS_TIME * 1000));
            } catch (InterruptedException ignored) {
            }
        }
    }

    public void runSlowSequence (Integer[] order)  {
        this.order = order;
        this.cancelSequence();
        this.future = this.executor.submit(this::slowFireSequence);
    }
    public void slowFireSequence() {
        for (int i=0; i < order.length; i++) {
            servoTimer.reset();
            kickServoUp(order[i]);
            try {
                Thread.sleep((long) (RUN_TO_POS_TIME * 1.5));
            } catch (InterruptedException ignored) {
            }

            servoTimer.reset();
            kickServoDown(order[i]);
            try {
                Thread.sleep((long) (RUN_TO_POS_TIME * 1.5));
            } catch (InterruptedException ignored) {
            }
        }
    }
    public void toggleRunSequence() {

    }

    public void cancelSequence() {
        if (future != null && !future.isDone()) {
            future.cancel(true);
            this.kickAllServosDown();
            this.isBusy = false;
        }
    }

    public void kickServoUp(int servoIndex) {
            kickers[servoIndex].setPosition(Settings.Positions.Transfer.upPos[servoIndex]);
    }

    public void kickServoDown(int servoIndex) {
            kickers[servoIndex].setPosition(Settings.Positions.Transfer.downPos[servoIndex]);
    }

    public void kickAllServosDown() {
        this.cancelSequence();
            for (int i = 0; i < kickers.length; i++) {
                this.kickServoDown(i);
            }
    }

    public Double[] getServoPositions() {
        return new Double[] {kickers[0].getPosition(), kickers[1].getPosition(), kickers[2].getPosition()};
    }

    public boolean isBusy() {
        return false;
    }

    public void stop() {
        this.cancelSequence();
        executor.shutdown();
        executor = null;
        future = null;
        order = null;
        servoTimer.reset();
    }
}