package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.util.Settings.HardwareNames;
import org.firstinspires.ftc.teamcode.util.Settings.Positions;


public class Intake {
    public DcMotorEx spinner;
    private boolean isRunning = false;


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
        isRunning = true;
    }

    /**
     * Eject an artifact in the robot
     */
    public void eject() {
        spinner.setPower(Positions.Intake.EJECT_SPEED);
        isRunning = true;
    }

    public void stop() {
        spinner.setPower(0);
        isRunning = false;
    }
    public void toggle() {
        isRunning = !isRunning;
        if (isRunning)  this.run();
        else            this.stop();
    }
    public void custom(double speed) {
        spinner.setPower(speed);
        isRunning = true;
    }
}