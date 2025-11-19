package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.control.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.UnnormalizedAngleUnit;

public class Shooter {
    private PIDFCoefficients shooterCoefficients = null;
    private PIDFController shooterPid = null;
    public DcMotorEx shooter = null;
    public Servo transfer = null;
    public Shooter (HardwareMap hwMap, PIDFCoefficients shooterCoefficients) {
        shooter = hwMap.get(DcMotorEx.class, "shooter");
        shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        transfer = hwMap.get(Servo.class, "push");

        this.shooterCoefficients = shooterCoefficients;
        shooterPid = new PIDFController(shooterCoefficients);

    }

    public void shoot() {
        shooter.setPower(0.75);
    }

    public void eject() {
        shooter.setPower(-0.05);
    }

    public void stop() {
        shooter.setPower(0);
    }
    public void idle() {
        shooter.setPower(0.43);
    }

    public void midFieldShoot() {
        shooter.setVelocity(140, AngleUnit.DEGREES);
    }

    public void farShoot() {
        shooter.setVelocity(180, AngleUnit.DEGREES);
    }

    public void transfer() {
        transfer.setPosition(0.845);
    }
    public void reload() {
        transfer.setPosition(1);
    }
}
