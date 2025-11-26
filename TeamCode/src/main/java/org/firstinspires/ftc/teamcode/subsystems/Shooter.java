package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.control.PIDFController;
import com.pedropathing.math.MathFunctions;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.pedropathing.control.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

public class Shooter {
    private PIDFController shooterPidf = null;
    public DcMotorEx shooter = null;
    public Shooter (HardwareMap hwMap, PIDFCoefficients shooterCoefficients) {
        shooter = hwMap.get(DcMotorEx.class, "shooter");
        shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooter.setVelocityPIDFCoefficients(shooterCoefficients.P, shooterCoefficients.I, shooterCoefficients.D, shooterCoefficients.F);
        shooterPidf = new PIDFController(shooterCoefficients);
    }

    public void eject() {
        shooter.setPower(-0.1);
    }

    public void stop() {
        shooter.setPower(0);
    }
    public void idle() {
        shooter.setPower(0.2);
    }

    public void midFieldShoot() {
        update(Constants.closeShootPower);
    }

    public void farShoot() {
        update(Constants.farShootPower);
    }
    public void update(double targetVelocity) {
        shooterPidf.updatePosition(shooter.getVelocity(AngleUnit.DEGREES));
        shooterPidf.setTargetPosition(targetVelocity);
        shooter.setPower(MathFunctions.clamp(shooterPidf.run(), -1, 1));
    }
    public double getTarget() {
        return shooterPidf.getTargetPosition();
    }
}
