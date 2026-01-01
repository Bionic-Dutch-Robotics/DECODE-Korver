package org.firstinspires.ftc.teamcode.subsystems;


import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.control.PIDFController;
import com.pedropathing.math.MathFunctions;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

public class Turret {
    public DcMotorEx turret;
    public PIDFController turretPid;
    public double offset;
    public double targetRad, targetTicks;

    public Turret(HardwareMap hwMap, double headingOffset) {
        turret = hwMap.get(DcMotorEx.class, "turret");
        turretPid = new PIDFController(new PIDFCoefficients(0.011, 0, 0.0005, 0));
        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        this.offset = headingOffset;
    }

    public void autoAim(double x, double y, double heading) {
        turretPid.updatePosition(turret.getCurrentPosition());
        targetRad = Math.atan2(144-y, 0-x) - heading + offset;
        targetTicks = targetRad * 140.003629846;
        turretPid.setTargetPosition(targetTicks);
        turret.setPower(MathFunctions.clamp(turretPid.run(), -0.4, 0.4));
    }
}
