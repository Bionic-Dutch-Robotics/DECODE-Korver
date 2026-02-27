package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="Park")
public class ParkTest extends OpMode {
    private Servo servo;
    @Override
    public void init() {
        servo = hardwareMap.get(Servo.class, "park");
        servo.setPosition(0.65);

        servo.scaleRange(0, 1);
    }

    @Override
    public void loop() {
        if (gamepad1.aWasPressed()) {
            servo.setPosition(
                    servo.getPosition() + 0.05
            );
        }
        else if (gamepad1.bWasPressed()) {
            servo.setPosition(
                    servo.getPosition() - 0.05
            );
        }
        telemetry.update();
        telemetry.addData("Servo Pos", servo.getPosition());
    }
}
