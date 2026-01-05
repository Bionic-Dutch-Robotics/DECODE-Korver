package org.firstinspires.ftc.teamcode.subsystems.shooter;

import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.control.PIDFController;
import com.pedropathing.math.MathFunctions;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Turret {
    public DcMotorEx turret;
    private PIDFController turretPid;
    public double turretRad, targetRad, fieldCentricTurretRad, turretPower;

    public Turret(HardwareMap hwMap) {
        turret = hwMap.get(DcMotorEx.class, "turret");
        turret.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        turretPid = new PIDFController(new PIDFCoefficients(0.013, 0, 0.0001, 0.0));

        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void loop(double x, double y, double heading) {
        // Current turret angle in radians
        turretRad = MathFunctions.scale(
                MathFunctions.normalizeAngle(turret.getCurrentPosition() / 140.003629846),
                0, Math.PI*2,
                -Math.PI,Math.PI
        );
        //turretRad = MathFunctions.clamp(turretRad, -Math.PI/2, Math.PI/2);

        // Target angle
        targetRad = Math.atan2(144 - y, 0 - x) - MathFunctions.normalizeAngle(heading) + Math.PI/2;
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
        if ((turretRad <= -Math.PI/2 && turretPid.run() < 0) ||
                (turretRad >= Math.PI/2 && turretPid.run() > 0)) {
            turretPower = 0; // stop motor at hard limit
        } else {
            turretPower = MathFunctions.clamp(turretPid.run(), -0.4, 0.4);
        }

        turret.setPower(turretPower);
    }

}
