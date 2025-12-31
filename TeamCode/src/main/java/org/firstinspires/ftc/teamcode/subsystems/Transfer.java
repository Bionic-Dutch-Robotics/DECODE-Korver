package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Transfer {
    public Servo transfer = null;

    public Transfer(HardwareMap hwMap) {

        transfer = hwMap.get(Servo.class, "push");  //  :)
    }

    public void reload() {
        transfer.setPosition(1);
    }

    public void feed() {
        transfer.setPosition(0.85);
    }
}
