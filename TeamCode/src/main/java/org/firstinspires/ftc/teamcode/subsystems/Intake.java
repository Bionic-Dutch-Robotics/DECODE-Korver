package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.util.Settings.HardwareNames;
import org.firstinspires.ftc.teamcode.util.Settings.Positions;


public class Intake {
    public DcMotorEx spinner;

    /**
     * Initializes the Intake of the robot
     * @param hardwareMap   An OpMode HardwareMap object
     */
    public Intake (HardwareMap hardwareMap) {
        spinner = hardwareMap.get(DcMotorEx.class, HardwareNames.Intake.INTAKE);
        spinner.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        spinner.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    /**
     * Run the intake
     */
    public void run() {
        spinner.setPower(Positions.Intake.INTAKE_SPEED);
    }

    /**
     * Eject an artifact in the robot
     */
    public void eject() {
        spinner.setPower(Positions.Intake.EJECT_SPEED);
    }

    public void stop() {
        spinner.setPower(0);
    }
    public void custom(double speed) {
        spinner.setPower(speed);
    }
}