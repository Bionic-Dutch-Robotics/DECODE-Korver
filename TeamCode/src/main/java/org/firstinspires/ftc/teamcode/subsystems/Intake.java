package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {
    public DcMotorEx spinner;

    /**
     * Initializes the Intake of the robot
     * @param hardwareMap   An OpMode HardwareMap object
     */
    public Intake (HardwareMap hardwareMap) {
        spinner = hardwareMap.get(DcMotorEx.class, "intake");
        spinner.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        spinner.setDirection(DcMotorEx.Direction.FORWARD);
        spinner.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    /**
     * Run the intake
     */
    public void intake() {
        spinner.setPower(0.75);
    }

    /**
     * Eject an artifact in the robot
     */
    public void eject() {
        spinner.setPower(-0.75);
    }
}