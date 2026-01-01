package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;

@TeleOp(name="Servo Test")
public class v3ServoTest extends OpMode {
    public static Servo servo1, servo2, servo3;
    public static Servo tiltServo;
    public static NormalizedColorSensor sensor;
    public static float gain = 0;
    public static double tilt;
    public static float[] hsvValues;
    public static Shooter shooter;
    public static double shooterSpeed;
    @Override
    public void init() {
        servo1 = hardwareMap.get(Servo.class, "kicker1");
        servo2 = hardwareMap.get(Servo.class, "kicker2");
        servo3 = hardwareMap.get(Servo.class, "kicker3");
        tiltServo = hardwareMap.get(Servo.class, "tilt");
        sensor = hardwareMap.get(NormalizedColorSensor.class, "color1");
        shooter = new Shooter(hardwareMap, Constants.shooterCoefficients);
        shooterSpeed = 250;
        tilt = 0.01;
    }

    @Override
    public void loop() {
        tiltServo.setPosition(tilt);
        if (gamepad1.dpadLeftWasPressed()) {
            tilt += 0.01;
        }
        else if (gamepad1.dpadRightWasPressed()){
            tilt -= 0.01;
        }
        shooter.update(shooterSpeed);
        if (gamepad1.dpadUpWasPressed()) {
            shooterSpeed += 10;
        }
        if (gamepad1.dpadDownWasPressed()) {
            shooterSpeed -= 10;
        }
        if (gamepad1.a) {
            servo1.setPosition(0.62);
        }
        else {
            servo1.setPosition(0.2);
        }

        if (gamepad1.b) {
            servo2.setPosition(0.5);
        }
        else {
            servo2.setPosition(0.95);
        }

        if (gamepad1.x) {
            servo3.setPosition(0.2);
        }
        else {
            servo3.setPosition(0.62);
        }
        telemetry.addData("Servo1 Pos: ", servo1.getPosition());
        telemetry.addData("Servo2 Pos: ", servo2.getPosition());
        telemetry.addData("Servo3 Pos: ", servo3.getPosition());
        runColorSensor();
        telemetry.addData("Shooter Vel: ", shooter.shooter.getVelocity(AngleUnit.DEGREES));
        telemetry.addData("Shooter Target: ", shooter.getTarget());
        telemetry.update();

    }


    public void runColorSensor() {
        // Explain basic gain information via telemetry
        telemetry.addLine("Hold the Dpad Up button on gamepad 1 to increase gain, or Dpad Down to decrease it.\n");
        telemetry.addLine("Higher gain values mean that the sensor will report larger numbers for Red, Green, and Blue, and Value\n");

        // Update the gain value if either of the A or B gamepad buttons is being held
        if (gamepad1.dpad_up) {
            // Only increase the gain by a small amount, since this loop will occur multiple times per second.
            gain += 0.005;
        } else if (gamepad1.dpad_down && gain > 1) { // A gain of less than 1 will make the values smaller, which is not helpful.
            gain -= 0.005;
        }

        // Show the gain value via telemetry
        telemetry.addData("Gain", gain);

        // Tell the sensor our desired gain value (normally you would do this during initialization,
        // not during the loop)
        sensor.setGain(10.0F);

        // Get the normalized colors from the sensor
        NormalizedRGBA colors = sensor.getNormalizedColors();

        /* Use telemetry to display feedback on the driver station. We show the red, green, and blue
         * normalized values from the sensor (in the range of 0 to 1), as well as the equivalent
         * HSV (hue, saturation and value) values. See http://web.archive.org/web/20190311170843/https://infohost.nmt.edu/tcc/help/pubs/colortheory/web/hsv.html
         * for an explanation of HSV color. */

        // Update the hsvValues array by passing it to Color.colorToHSV()
        //Color.colorToHSV(colors.toColor(), hsvValues);

        telemetry.addLine()
                .addData("Red", "%.3f", colors.red)
                .addData("Green", "%.3f", colors.green)
                .addData("Blue", "%.3f", colors.blue);
        /*telemetry.addLine()
                .addData("Hue", "%.3f", hsvValues[0])
                .addData("Saturation", "%.3f", hsvValues[1])
                .addData("Value", "%.3f", hsvValues[2]);*/
        telemetry.addData("Alpha", "%.3f", colors.alpha);

        /* If this color sensor also has a distance sensor, display the measured distance.
         * Note that the reported distance is only useful at very close range, and is impacted by
         * ambient light and surface reflectivity. */
        if (sensor instanceof DistanceSensor) {
            telemetry.addData("Distance (cm)", "%.3f", ((DistanceSensor) sensor).getDistance(DistanceUnit.CM));
        }
    }

}
