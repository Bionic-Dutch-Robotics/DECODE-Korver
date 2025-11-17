package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.util.Settings.Positions;
import org.firstinspires.ftc.teamcode.util.Settings.HardwareNames;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.control.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class Shooter {
    public PIDFController shooterPidf = null;
    public DcMotorEx shooter = null;
    public boolean stop;

    public enum ShooterState {
        MIDFIELD, FAR, EJECT, IDLE
    }

    public ShooterState shooterState;
    public Shooter (HardwareMap hwMap, PIDFCoefficients shooterCoefficients) {
        shooterPidf = new PIDFController(shooterCoefficients);
        shooter = hwMap.get(DcMotorEx.class, HardwareNames.Shooter.SHOOTER);
        shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        stop = true;
    }

    public void eject() {
        this.start();
        shooter.setPower(-0.05);
        shooterState = ShooterState.EJECT;
    }
    public void idle() {
        this.start();
        shooter.setPower(0.25);
        shooterState = ShooterState.IDLE;
    }

    public void shootFar() {
        this.setTargetVelocity(Positions.Shooter.FAR_VELOCITY);
        shooterState = ShooterState.FAR;
    }
    public void shootClose() {
        this.setTargetVelocity(Positions.Shooter.MIDFIELD_VELOCITY);
        shooterState = ShooterState.MIDFIELD;
    }

    public void setTargetVelocity(double velocity) {
        this.start();
        shooterPidf.setTargetPosition(velocity);
    }


    public void update() {
        shooterPidf.updatePosition(shooter.getVelocity(AngleUnit.DEGREES));

        if (!stop) {
            shooter.setPower(shooterPidf.run());
        }
        else {
            shooter.setPower(0);
        }
    }

    public void start() {
        if (stop) {
            stop = false;
        }
    }

    public void stop() {
        if (!stop) {
            stop = true;
        }
    }
}
