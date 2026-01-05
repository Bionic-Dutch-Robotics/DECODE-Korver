package org.firstinspires.ftc.teamcode.subsystems.shooter;

import com.pedropathing.math.MathFunctions;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Tilt {
    private Servo tilt;
    public Tilt(HardwareMap hwMap) {
        tilt = hwMap.get(Servo.class, "tilt");
    }

    public void setTilt(double tiltAngle) {
        tiltAngle = MathFunctions.clamp(tiltAngle, 0.05, 0.5);
        this.tilt.setPosition(tiltAngle);
    }
}
