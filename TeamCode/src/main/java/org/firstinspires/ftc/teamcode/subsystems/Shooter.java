package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class Shooter {
    private PIDFCoefficients shooterCoefficients = null;
    public DcMotorEx shooter = null;
    public Servo transfer = null;
    public Shooter (HardwareMap hwMap, PIDFCoefficients shooterCoefficients) {
        shooter = hwMap.get(DcMotorEx.class, "shooter");
        shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        transfer = hwMap.get(Servo.class, "push");

        this.shooterCoefficients = shooterCoefficients;
        shooter.setVelocityPIDFCoefficients(shooterCoefficients.p, shooterCoefficients.i, shooterCoefficients.d, shooterCoefficients.f);

    }

    public void shoot() {
        shooter.setPower(0.75);
    }

    public void eject() {
        shooter.setPower(-1.0);
    }

    public void stop() {
        shooter.setPower(0);
    }
    public void idle() {
        shooter.setPower(0.2);
    }

    public void midFieldShoot() {
        shooter.setVelocity(140, AngleUnit.DEGREES);
    }

    public void farShoot() {
        shooter.setVelocity(170, AngleUnit.DEGREES);
    }

    public void transfer() {
        transfer.setPosition(0.845);
    }
    public void reload() {
        transfer.setPosition(1);
    }
}
