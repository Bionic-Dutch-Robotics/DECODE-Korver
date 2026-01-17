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
    private AllianceColor alliance;

    public Turret(HardwareMap hwMap) {
        turret = hwMap.get(DcMotorEx.class, Settings.HardwareNames.Shooter.TURRET);
        turret.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        turretPid = new PIDFController(new PIDFCoefficients(0.013, 0, 0.0001, 0.065));

        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        target = new Pose();
    }

    public void setAlliance(AllianceColor alliance) {
        target = alliance.isRed() ? new Pose(141, 120) : new Pose(3, 120);
    }

    public void loop(double x, double y, double heading) {
        // Current turret angle in radians
        turretRad = MathFunctions.scale(
                MathFunctions.normalizeAngle(turret.getCurrentPosition() / 140.003629846 - fieldCentricTurretStartingPosition),
                0, Math.PI*2,
                -Math.PI,Math.PI
        );
        //turretRad = MathFunctions.clamp(turretRad, -Math.PI/2, Math.PI/2);

        // Target angle
        targetRad = Math.atan2(target.getY() - y, target.getX() - x) - MathFunctions.normalizeAngle(heading + fieldCentricTurretStartingPosition/2 + Math.toRadians(6));
        targetRad = MathFunctions.scale(
                MathFunctions.normalizeAngle(targetRad),
                0, Math.PI*2,
                -Math.PI, Math.PI
        );
        targetRad = MathFunctions.clamp(targetRad, -Math.PI/2, Math.PI/2);

        // Set PID in radians
        turretPid.setTargetPosition(targetRad * 140.003629846);
        turretPid.updatePosition(turretRad * 140.003629846);

        // Only apply power if we are not at limit OR moving away from limit
        if ((turretRad <= -Math.toRadians(89) /* -Math.PI/2 */ && turretPid.run() < 0) ||
                (turretRad >= Math.toRadians(89) && turretPid.run() > 0)) {
            turretPower = 0; // stop motor at hard limit
        } else {
            turretPower = MathFunctions.clamp(turretPid.run(), -0.4, 0.4);
        }

        turret.setPower(turretPower);
    }

}
