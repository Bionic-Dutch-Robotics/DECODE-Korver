package org.firstinspires.ftc.teamcode.subsystems.shooter;

import com.pedropathing.math.MathFunctions;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.util.Settings;

public class Tilt {
    private Servo tilt;
    public Tilt(HardwareMap hwMap) {
        tilt = hwMap.get(Servo.class, Settings.HardwareNames.Shooter.TILT_SERVO);
    }

    public void setTilt(double tiltAngle) {
        tiltAngle = MathFunctions.clamp(tiltAngle, 0.05, 0.5);
        this.tilt.setPosition(tiltAngle);
    }

    public double auto(double distance) {
        return -0.0000121382*Math.pow(distance, 2) + 0.00269935*distance + 0.00144725;
    }
}
