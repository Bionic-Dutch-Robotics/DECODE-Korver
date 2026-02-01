package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.control.PIDFController;
import com.pedropathing.math.MathFunctions;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.pedropathing.control.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.util.AllianceColor;

public class Shooter {
    private PIDFController shooterPidf = null;
    public DcMotorEx shooter = null;
    public final double redPowerCoefficient = 1.0;
    public final double bluePowerCoefficient = 0.94;

    public Shooter (HardwareMap hwMap, PIDFCoefficients shooterCoefficients) {
        shooter = hwMap.get(DcMotorEx.class, "shooter");
        shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooter.setDirection(DcMotorEx.Direction.FORWARD);
        shooter.setVelocityPIDFCoefficients(shooterCoefficients.P, shooterCoefficients.I, shooterCoefficients.D, shooterCoefficients.F);
        shooterPidf = new PIDFController(shooterCoefficients);
    }

    public void eject() {
        shooter.setPower(-0.1);
    }
    public void adaptive(double x, double y, AllianceColor alliance, double multiplier) {
        this.update(
                this.getRegressionVelocity(
                        this.getDistance(x, y, alliance),
                        alliance
                )
                * multiplier
        );
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
    public double getDistance(double x, double y, AllianceColor alliance) {
        if (alliance.isRed()) {
            return Math.sqrt(Math.pow(144-x, 2) + Math.pow(144-y, 2));
        }
        else {
            return Math.sqrt(Math.pow(-x, 2) + Math.pow(144-y, 2));
        }
    }
    public double getRegressionVelocity (double distance, AllianceColor alliance) {
        return
                (0.558815 * distance + 89.297) *
                (alliance.isRed() ? redPowerCoefficient : bluePowerCoefficient);
    }
}
