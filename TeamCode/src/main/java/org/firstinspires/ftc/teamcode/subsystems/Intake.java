package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {
    public DcMotorEx spinner;

    public Intake (HardwareMap hardwareMap) {
        spinner = hardwareMap.get(DcMotorEx.class, "intake");
        spinner.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        spinner.setDirection(DcMotorEx.Direction.FORWARD);
        spinner.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void intake() {
        spinner.setPower(0.75);
    }

    public void reject() {
        spinner.setPower(-0.75);
    }
}