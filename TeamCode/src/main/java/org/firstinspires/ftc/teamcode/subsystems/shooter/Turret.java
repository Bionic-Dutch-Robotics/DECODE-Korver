package org.firstinspires.ftc.teamcode.subsystems.shooter;

import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.control.PIDFController;
import com.pedropathing.geometry.Pose;
import com.pedropathing.math.MathFunctions;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.util.AllianceColor;
import org.firstinspires.ftc.teamcode.util.Settings;

public class Turret {
    public DcMotorEx turret;
    private PIDFController turretPid;
    public double turretRad, targetRad, fieldCentricTurretRad, turretPower;
    private Pose target;
    private final double fieldCentricTurretStartingPosition = Math.PI;


    public Turret(HardwareMap hwMap) {
        turret = hwMap.get(DcMotorEx.class, Settings.HardwareNames.Shooter.TURRET);
        turret.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        turretPid = new PIDFController(new PIDFCoefficients(0.03, 0, 0.00045,0.0));

        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        target = new Pose();
    }

    public void setAlliance(AllianceColor alliance) {
        target = alliance.isRed() ? new Pose(135, 135) : new Pose(5, 135);
    }

    public void loop(Pose botPose) {
        // Current turret angle in radians
        turretRad = this.getTurretRadians();
        //turretRad = MathFunctions.clamp(turretRad, -Math.PI/2, Math.PI/2);

        // Target angle
        targetRad = this.getTargetRadians(botPose);

        // Set PID in radians
        turretPid.setTargetPosition(this.convertRadiansToTicks(targetRad));
        turretPid.updatePosition(this.convertRadiansToTicks(turretRad));

        // Only apply power if we are not at limit OR moving away from limit
        if ((turretRad <= Math.toRadians(-100) /* -Math.PI/2 */ && turretPid.run() < 0) ||
                (turretRad >= Math.toRadians(100) && turretPid.run() > 0)) {
            turretPower = 0; // stop motor at hard limit
        } else {
            turretPower = MathFunctions.clamp(turretPid.run(), -0.7, 0.7);
        }

        turret.setPower(turretPower);
    }

    public double getTargetRadians(Pose botPose) {
        double targetRad = Math.atan2(
                target.getY() - botPose.getY(),
                target.getX() - botPose.getX()
        ) - MathFunctions.normalizeAngle(
                botPose.getHeading() + fieldCentricTurretStartingPosition/2
        ) - Math.toRadians(0);

        targetRad = MathFunctions.scale(
                MathFunctions.normalizeAngle(targetRad),
                0, Math.PI*2,
                -Math.PI, Math.PI
        );
        targetRad = MathFunctions.clamp(targetRad, Math.toRadians(-90), Math.toRadians(90));

        return targetRad;
    }

    public double getTurretRadians() {
        turretRad = MathFunctions.scale(
                MathFunctions.normalizeAngle(turret.getCurrentPosition() / 140.003629846 - fieldCentricTurretStartingPosition),
                0, Math.PI*2,
                -Math.PI,Math.PI
        );

        return turretRad;
    }

    public double convertRadiansToTicks(double rads) {
        return 140.003629846*rads;
    }

    public double convertTicksToRadians(double ticks) {
        return ticks/140.003629846;
    }
}
