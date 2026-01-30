package org.firstinspires.ftc.teamcode.tests;

import static java.lang.Thread.sleep;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Turret;
import org.firstinspires.ftc.teamcode.util.Artifact;
import org.firstinspires.ftc.teamcode.util.ArtifactOrder;

import java.util.ArrayList;

@TeleOp(name="TRANSFER")
public class transfertest extends OpMode {
    private ArrayList<Servo> kickers;
    private ArrayList<NormalizedColorSensor> colorSensors;
    private static ArrayList<Artifact> MOTIF = new ArrayList<>();
    private final double servoRunToPosTime = 3;
    private ElapsedTime time;
    private TransferState transferState;
    private Shooter shooter;
    private Follower follower;
    private double shooterSpeed;
    private float gain;
    Integer[] targets;
    private ArtifactOrder shootOrder;
    private Turret turret;
    private Pose currentPose;

    @Override
    public void init() {
        initColorSensors();
        initServos();

        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(Constants.blueStartPose);

        time = new ElapsedTime(ElapsedTime.Resolution.SECONDS);
        shooter = new Shooter(hardwareMap, Constants.shooterCoefficients);
        shooterSpeed = 250;

        transferState = TransferState.IDLE;
        gain = 10.0F;
        MOTIF = new ArrayList<>(3);
        MOTIF.add(0,
                Artifact.PURPLE);
        MOTIF.add(1,
                Artifact.GREEN);
        MOTIF.add(2,
                Artifact.PURPLE);

        targets = new Integer[3];
        shootOrder = new ArtifactOrder(new Artifact[] {Artifact.PURPLE, Artifact.GREEN, Artifact.PURPLE});

        turret = new Turret(hardwareMap, Math.PI/2);
    }

    @Override
    public void start() {
        follower.startTeleopDrive(false);
        time.reset();
    }
    @Override
    public void loop() {
        this.updateAll();

        if (gamepad1.dpadUpWasPressed()) {
            shooterSpeed += 10;
        }
        else if (gamepad1.dpadDownWasPressed()) {
            shooterSpeed -= 10;
        }

        telemetry.addData("Servo 1 Pos: ", kickers.get(0).getPosition());
        telemetry.addData("Servo 2 Pos: ", kickers.get(1).getPosition());
        telemetry.addData("Servo 3 Pos: ", kickers.get(2).getPosition());

        // Explain basic gain information via telemetry
        runSensors();
        telemetry.addData("Artifact selection 1: ", this.detectArtifacts().get(0));
        telemetry.addData("Artifact selection 2: ", this.detectArtifacts().get(1));
        telemetry.addData("Artifact selection 3: ", this.detectArtifacts().get(2));

        if (targets.length > 0) {
            telemetry.addData("Motif 1 Index: ",
                    targets[0]
            );
            telemetry.addData("Motif 2 Index: ",
                    targets[1]
            );
            telemetry.addData("Motif 3 Index: ",
                    targets[2]
            );
        }
    }

    private ArrayList<Artifact> detectArtifacts() {
        ArrayList<Artifact> results = new ArrayList<>(3);
        int index = 0;
        for (NormalizedColorSensor sensor : colorSensors) {
            if (((DistanceSensor) sensor).getDistance(DistanceUnit.CM) < 1) {
                if (sensor.getNormalizedColors().green < sensor.getNormalizedColors().blue) {
                    results.add(index,
                            Artifact.PURPLE
                    );
                } else {
                    results.add(index,
                            Artifact.GREEN
                    );
                }
            }
            else {
                results.add(
                        index,
                        Artifact.UNKNOWN
                );
            }
        }
        return results;
    }
    private void runSensors() {
        // Tell the sensor our desired gain value (normally you would do this during initialization,
        // not during the loop)
        for (NormalizedColorSensor sensor : colorSensors) {
            sensor.setGain(10.0F);
        }

        // Get the normalized colors from the sensor
        ArrayList<NormalizedRGBA> colors = new ArrayList<> (3);

        int index = 0;
        for (NormalizedColorSensor sensor : colorSensors) {
            colors.add(index, sensor.getNormalizedColors());
            index += 1;
        }

        /* Use telemetry to display feedback on the driver station. We show the red, green, and blue
         * normalized values from the sensor (in the range of 0 to 1), as well as the equivalent
         * HSV (hue, saturation and value) values. See http://web.archive.org/web/20190311170843/https://infohost.nmt.edu/tcc/help/pubs/colortheory/web/hsv.html
         * for an explanation of HSV color. */

        // Update the hsvValues array by passing it to Color.colorToHSV()
        //Color.colorToHSV(colors.toColor(), hsvValues);

        /*telemetry.addLine()
                .addData("Red", "%.3f", colors.red)
                .addData("Green", "%.3f", colors.green)
                .addData("Blue", "%.3f", colors.blue);
        telemetry.addLine()
                .addData("Hue", "%.3f", hsvValues[0])
                .addData("Saturation", "%.3f", hsvValues[1])
                .addData("Value", "%.3f", hsvValues[2]);
        telemetry.addData("Alpha", "%.3f", colors.alpha);*/

        /* If this color sensor also has a distance sensor, display the measured distance.
         * Note that the reported distance is only useful at very close range, and is impacted by
         * ambient light and surface reflectivity. */
        /*if (colorSensors.get(0) instanceof DistanceSensor) {
            telemetry.addData("Distance (cm)", "%.3f", ((DistanceSensor) colorSensors.get(0)).getDistance(DistanceUnit.CM));
        }*/
    }
    private void runServoRapidFire() {
        if (gamepad1.yWasPressed()) {
            time.reset();
        }
        if (gamepad1.y) {
            if (time.time() < servoRunToPosTime) {
                kickers.get(0).setPosition(0.2);
            }
            else if (time.time() < servoRunToPosTime * 2 && time.time() >= servoRunToPosTime) {
                kickers.get(0).setPosition(0.64);
            }
            else if (time.time() < servoRunToPosTime * 3 && time.time() >= servoRunToPosTime * 2) {
                kickers.get(1).setPosition(0.5);
            }
            else if (time.time() < servoRunToPosTime * 4 && time.time() >= servoRunToPosTime * 3) {
                kickers.get(1).setPosition(0.96);
            }
            else if (time.time() < servoRunToPosTime * 5 && time.time() >= servoRunToPosTime * 4) {
                kickers.get(2).setPosition(0.62);
            }
            else if (time.time() < servoRunToPosTime * 6 && time.time() >= servoRunToPosTime * 5) {
                kickers.get(2).setPosition(0.2);
            }
        }
        else if (gamepad1.x) {
            targets = shootOrder.get();
            if (time.time() < servoRunToPosTime) {
                this.kickServoUp(targets[0]);
            }
            else if (time.time() < servoRunToPosTime * 2 && time.time() >= servoRunToPosTime) {
                this.kickServoDown(targets[0]);
            }
            else if (time.time() < servoRunToPosTime * 3 && time.time() >= servoRunToPosTime * 2) {
                this.kickServoUp(targets[1]);
            }
            else if (time.time() < servoRunToPosTime * 4 && time.time() >= servoRunToPosTime * 3) {
                this.kickServoDown(targets[1]);
            }
            else if (time.time() < servoRunToPosTime * 5 && time.time() >= servoRunToPosTime * 4) {
                this.kickServoUp(targets[2]);
            }
            else if (time.time() < servoRunToPosTime * 6 && time.time() >= servoRunToPosTime * 5) {
                this.kickServoDown(targets[2]);
            }
        }

        if (gamepad1.xWasPressed()) {
            updateArtifacts();
            shootOrder.search();
            time.reset();
        }
        else if (gamepad1.xWasReleased()) {
            this.kickServoDown(0);
            this.kickServoDown(1);
            this.kickServoDown(2);
        }
    }

    private void initColorSensors() {
        colorSensors = new ArrayList<>(3);
        colorSensors.add(
                0,
                hardwareMap.get(NormalizedColorSensor.class, "color1")
        );
        colorSensors.add(
                1,
                hardwareMap.get(NormalizedColorSensor.class, "color2")
        );
        colorSensors.add(
                2,
                hardwareMap.get(NormalizedColorSensor.class, "color3")
        );
    }

    private void initServos() {
        kickers = new ArrayList<>(3);
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

    private void updateArtifacts() {
        int i = 0;
        for (NormalizedColorSensor sensor : colorSensors) {
            NormalizedRGBA color = sensor.getNormalizedColors();
            if (((DistanceSensor) sensor).getDistance(DistanceUnit.INCH) < 2.5) {
                if (color.blue > color.green) {
                    shootOrder.addPair(i, Artifact.PURPLE);
                } else if (color.blue < color.green) {
                    shootOrder.addPair(i, Artifact.GREEN);
                }
                i++;
            }
            else {
                shootOrder.addPair(i, Artifact.UNKNOWN);
            }
        }
    }

    private void updateDrivetrain() {
        follower.update();

        follower.setTeleOpDrive(
                -gamepad1.left_stick_y,
                -gamepad1.left_stick_x,
                -gamepad1.right_stick_x,
                true
        );
    }

    private void kickServoUp(int servoIndex) {
        if (servoIndex == 0) {
            kickers.get(servoIndex).setPosition(0.62);
        }
        else if (servoIndex == 1) {
            kickers.get(servoIndex).setPosition(0.5);
        }
        else if (servoIndex == 2) {
            kickers.get(servoIndex).setPosition(0.2);
        }
    }
    private void kickServoDown(int servoIndex) {
        if (servoIndex == 0) {
            kickers.get(servoIndex).setPosition(0.2);
        }
        else if (servoIndex == 1) {
            kickers.get(servoIndex).setPosition(0.95);
        }
        else if (servoIndex == 2) {
            kickers.get(servoIndex).setPosition(0.64);
        }
    }

    private void updateAll() {
        updateDrivetrain();

        currentPose = follower.getPose();
        turret.autoAim(currentPose.getX(), currentPose.getY(), follower.getHeading());

        telemetry.update();
        runServoRapidFire();

        shooter.update(shooterSpeed);
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
