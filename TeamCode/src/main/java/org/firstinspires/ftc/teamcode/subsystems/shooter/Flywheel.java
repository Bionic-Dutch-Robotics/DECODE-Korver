package org.firstinspires.ftc.teamcode.subsystems.shooter;

import static org.firstinspires.ftc.teamcode.util.Settings.Positions.Shooter.SHOOTER_COEFFICIENTS;

import com.pedropathing.control.PIDFController;
import com.pedropathing.math.MathFunctions;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.util.AllianceColor;
import org.firstinspires.ftc.teamcode.util.Settings;

public class Flywheel {
    private PIDFController shooterPidf = null, shooterPidf2 = null;
    public DcMotorEx shooter = null, shooter2 = null;
    public VoltageSensor voltageSensor = null;
    public final double redPowerCoefficient = 1.1;
    public final double bluePowerCoefficient = 1.0;
    private AllianceColor alliance;
    private double voltageComp = 1.0;

    public Flywheel(HardwareMap hwMap, AllianceColor alliance) {
        shooter = hwMap.get(DcMotorEx.class, Settings.HardwareNames.Shooter.SHOOTER);
        shooter2 = hwMap.get(DcMotorEx.class, Settings.HardwareNames.Shooter.SHOOTER_TWO);
        voltageSensor = hwMap.voltageSensor.iterator().next();
        shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooter2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooter2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooter.setDirection(DcMotorSimple.Direction.REVERSE);
        shooter2.setDirection(DcMotorSimple.Direction.REVERSE);
        shooterPidf = new PIDFController(SHOOTER_COEFFICIENTS);
        shooterPidf2 = new PIDFController(SHOOTER_COEFFICIENTS);
        this.alliance = alliance;
    }

    public void eject() {
        shooter.setPower(-0.1);
        shooter2.setPower(-0.1);
    }
    public void adaptive(double x, double y) {
        this.update(
                this.getRegressionVelocity(
                        this.getDistance(x, y)
                )
        );
    }

    public void setVoltageComp (double voltageComp) {
        this.voltageComp = voltageComp;
    }
    public double getVoltageComp() {
        return voltageComp;
    }

    public void stop() {

        shooter.setPower(0);
        shooter2.setPower(0);
    }
    public void idle() {

        shooter.setPower(0.8);
        shooter2.setPower(0.8);
    }


    public void update(double targetVelocity) {
        shooterPidf.updatePosition(shooter.getVelocity(AngleUnit.DEGREES));
        shooterPidf2.updatePosition(shooter2.getVelocity(AngleUnit.DEGREES));
        shooterPidf.setTargetPosition(targetVelocity);
        shooterPidf2.setTargetPosition(targetVelocity);

        double batteryVoltage = voltageSensor.getVoltage();
        shooter.setPower(MathFunctions.clamp(
                (shooterPidf.run() * batteryVoltage) / 12,
                -1, 1
        ));
        shooter2.setPower(MathFunctions.clamp(
                (shooterPidf.run() * batteryVoltage) / 12,
                -1, 1));
    }
    public double getTarget() {
        return shooterPidf.getTargetPosition();
    }
    public double getDistance(double x, double y) {
        if (alliance.isRed()) {
            return Math.sqrt(Math.pow(135-x, 2) + Math.pow(135-y, 2));
        }
        else {
            return Math.sqrt(Math.pow(5-x, 2) + Math.pow(135-y, 2));
        }
    }
    public double getRegressionVelocity (double distance) {
        return 0.809636*distance + 178.98652;
    }
}
