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
        tiltAngle = MathFunctions.clamp(tiltAngle, 0.1, 1.0);
        this.tilt.setPosition(tiltAngle);
    }

    public double auto(double distance) {
        return distance < 115 ? -0.0000066366*Math.pow(distance, 3) + 0.00183287*Math.pow(distance, 2) - 0.15909*distance + 4.5531 :
                0.1;
    }
}
