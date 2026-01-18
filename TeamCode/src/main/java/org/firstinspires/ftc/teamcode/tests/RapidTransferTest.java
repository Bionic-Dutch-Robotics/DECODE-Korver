package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.ArrayList;

@Disabled
@Autonomous(name="Rapid Fire")
public class RapidTransferTest extends LinearOpMode {
    public static ArrayList<Servo> transfers;

    @Override
    public void runOpMode () {
        transfers.add(0,hardwareMap.get(Servo.class, "kicker1"));
        transfers.add(1, hardwareMap.get(Servo.class, "kicker2"));
        transfers.add(2, hardwareMap.get(Servo.class, "kicker3"));

        waitForStart();
        resetRuntime();
        //sleep(350);
        while (getRuntime() < 0.5) {
            transfers.get(0).setPosition(0.2);
        }
        resetRuntime();
        while (getRuntime() < 0.5) {transfers.get(0).setPosition(0.64);}
        resetRuntime();
        while (getRuntime() < 0.5) {transfers.get(1).setPosition(0.5);}
        resetRuntime();

        while (getRuntime() < 0.5) {transfers.get(1).setPosition(0.96);}
        resetRuntime();

        while (getRuntime() < 0.5) {transfers.get(2).setPosition(0.62);}
        resetRuntime();
        while (getRuntime() < 0.5) {transfers.get(2).setPosition(0.2);}
        resetRuntime();
    }
}
