package org.firstinspires.ftc.teamcode.tests;

import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.subsystems.Transfer;

import java.util.ArrayList;

@TeleOp(name="TRANSFER")
public class transfertest extends OpMode {
    private ArrayList<Servo> kickers;
    private ArrayList<NormalizedColorSensor> colorSensors;
    private final double servoRunToPosTime = 0.5;
    private ElapsedTime time;
    private TransferState transferState;
    private float gain;

    @Override
    public void init() {
        initColorSensors();
        initServos();
        time = new ElapsedTime(ElapsedTime.Resolution.SECONDS);

        transferState = TransferState.IDLE;
        gain = 0.0F;
    }

    @Override
    public void start() {
        time.reset();
    }
    @Override
    public void loop() {
        telemetry.update();
        runServoRapidFire();
        if (gamepad1.yWasPressed()) {
            transferState = TransferState.SERVO_ONE_UP;
            time.reset();
        }

        telemetry.addData("Servo 1 Pos: ", kickers.get(0).getPosition());
        telemetry.addData("Servo 2 Pos: ", kickers.get(1).getPosition());
        telemetry.addData("Servo 3 Pos: ", kickers.get(2).getPosition());

        // Explain basic gain information via telemetry
        telemetry.addLine("Hold the Dpad Up button on gamepad 1 to increase gain, or Dpad Down to decrease it.\n");
        telemetry.addLine("Higher gain values mean that the sensor will report larger numbers for Red, Green, and Blue, and Value\n");

        // Update the gain value if either of the A or B gamepad buttons is being held
        if (gamepad1.dpad_up) {
            // Only increase the gain by a small amount, since this loop will occur multiple times per second.
            gain += 0.005F;
        } else if (gamepad1.dpad_down && gain > 1) { // A gain of less than 1 will make the values smaller, which is not helpful.
            gain -= 0.005F;
        }

        // Show the gain value via telemetry
        telemetry.addData("Gain", gain);
    }

    private void runSensors() {
        // Tell the sensor our desired gain value (normally you would do this during initialization,
        // not during the loop)
        colorSensors.get(1).setGain(gain);

        // Get the normalized colors from the sensor
        NormalizedRGBA colors = colorSensors.get(1).getNormalizedColors();

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
        if (colorSensors.get(1) instanceof DistanceSensor) {
            telemetry.addData("Distance (cm)", "%.3f", ((DistanceSensor) colorSensors.get(1)).getDistance(DistanceUnit.CM));
        }
    }
    private void runServoRapidFire() {
        switch(transferState) {
            case SERVO_ONE_UP:
                kickers.get(0).setPosition(0.2);

                if (time.time() > servoRunToPosTime) {
                    transferState = TransferState.SERVO_ONE_DOWN;
                    time.reset();
                }

            case SERVO_ONE_DOWN:
                kickers.get(0).setPosition(0.64);

                if (time.time() > servoRunToPosTime) {
                    transferState = TransferState.SERVO_TWO_UP;
                    time.reset();
                }

            case SERVO_TWO_UP:
                kickers.get(1).setPosition(0.5);

                if (time.time() > servoRunToPosTime) {
                    transferState = TransferState.SERVO_TWO_DOWN;
                    time.reset();
                }

            case SERVO_TWO_DOWN:
                kickers.get(1).setPosition(0.96);

                if (time.time() > servoRunToPosTime) {
                    transferState = TransferState.SERVO_THREE_UP;
                    time.reset();
                }

            case SERVO_THREE_UP:
                kickers.get(2).setPosition(0.62);

                if (time.time() > servoRunToPosTime) {
                    transferState = TransferState.SERVO_THREE_DOWN;
                    time.reset();
                }

            case SERVO_THREE_DOWN:
                kickers.get(0).setPosition(0.64);

                if (time.time() > servoRunToPosTime) {
                    transferState = TransferState.IDLE;
                }
        }
    }

    private void initColorSensors() {
        colorSensors = new ArrayList<>();
        /*colorSensors.add(
                0,
                hardwareMap.get(NormalizedColorSensor.class, "colorSensor1")
        );*/
        colorSensors.add(
                1,
                hardwareMap.get(NormalizedColorSensor.class, "Color Sensor 2")  //Change this
        );
        /*colorSensors.add(
                2,
                hardwareMap.get(NormalizedColorSensor.class, "colorSensor3")
        );*/
    }

    private void initServos() {
        kickers = new ArrayList<>();
        kickers.add(
                0,
                hardwareMap.get(Servo.class, "kicker1")
        );
        kickers.add(
                1,
                hardwareMap.get(Servo.class, "kicker2")
        );
        kickers.add(
                2,
                hardwareMap.get(Servo.class, "kicker3")
        );
    }

    private enum TransferState {
        IDLE,
        SERVO_ONE_UP,
        SERVO_ONE_DOWN,
        SERVO_TWO_UP,
        SERVO_TWO_DOWN,
        SERVO_THREE_UP,
        SERVO_THREE_DOWN
    }
}
