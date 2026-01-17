package org.firstinspires.ftc.teamcode.subsystems.shooter;

import static org.firstinspires.ftc.teamcode.util.Settings.Positions.Shooter.SHOOTER_COEFFICIENTS;

import com.pedropathing.control.PIDFController;
import com.pedropathing.math.MathFunctions;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.util.AllianceColor;
import org.firstinspires.ftc.teamcode.util.Settings;

public class Flywheel {
    private PIDFController shooterPidf = null;
    public DcMotorEx shooter = null;
    public final double redPowerCoefficient = 1.1;
    public final double bluePowerCoefficient = 1.0;

    public Flywheel(HardwareMap hwMap) {
        shooter = hwMap.get(DcMotorEx.class, Settings.HardwareNames.Shooter.SHOOTER);
        shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooter.setDirection(DcMotorSimple.Direction.REVERSE);
        shooterPidf = new PIDFController(SHOOTER_COEFFICIENTS);
    }

    public void eject() {
        shooter.setPower(-0.1);
    }
    public void adaptive(double x, double y, AllianceColor alliance) {
        this.update(
                this.getRegressionVelocity(
                        this.getDistance(x, y, alliance),
                        alliance
                )
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
        return 1.84932*distance + 162.83105;
    }
}
