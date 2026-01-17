package org.firstinspires.ftc.teamcode.tests;

import static java.lang.Thread.sleep;

import com.pedropathing.geometry.Pose;
import com.pedropathing.math.MathFunctions;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.subsystems.shooter.Turret;
import org.firstinspires.ftc.teamcode.subsystems.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.shooter.Flywheel;
import org.firstinspires.ftc.teamcode.util.AllianceColor;
import org.firstinspires.ftc.teamcode.util.Artifact;
import org.firstinspires.ftc.teamcode.util.ArtifactOrder;
import static org.firstinspires.ftc.teamcode.util.Settings.Positions.Transfer.RUN_TO_POS_TIME;

import java.util.ArrayList;

@TeleOp(name="Transfer")
public class TransferTest extends OpMode {
    private Servo[] kickers;
    private NormalizedColorSensor[] colorSensors;
    private static ArrayList<Artifact> MOTIF = new ArrayList<>();
    private ElapsedTime time;
    private TransferState transferState;
    private Flywheel shooter;
    private double shooterSpeed;
    private float gain;
    Integer[] targets;
    private ArtifactOrder shootOrder;
    private Turret turret;
    private Pose currentPose;
    private boolean shoot;
    private Intake intake;
    private Drivetrain dt;

    @Override
    public void init() {
        initColorSensors();
        initServos();
        intake = new Intake(hardwareMap);

        dt = new Drivetrain(hardwareMap, new AllianceColor(AllianceColor.Selection.BLUE));

        time = new ElapsedTime(ElapsedTime.Resolution.SECONDS);
        shooter = new Flywheel(hardwareMap);
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

        turret = new Turret(hardwareMap);

        shoot = false;
    }

    @Override
    public void start() {
        dt.startTeleOpDrive();
        time.reset();
    }

    @Override
    public void loop() {
        this.updateAll();
        if (gamepad1.left_bumper) {
            intake.run();
        }
        else if (gamepad1.right_bumper) {
            intake.eject();
        }
        else {
            intake.stop();
        }

        if (gamepad1.dpadUpWasPressed()) {
            shooterSpeed += 10;
        }
        else if (gamepad1.dpadDownWasPressed()) {
            shooterSpeed -= 10;
        }

        telemetry.addData("Servo 1 Pos: ", kickers[0].getPosition());
        telemetry.addData("Servo 2 Pos: ", kickers[1].getPosition());
        telemetry.addData("Servo 3 Pos: ", kickers[2].getPosition());

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

        telemetry.addData("Turret Target", turret.targetRad);
        telemetry.addData("Turret Current", turret.turretRad);
        telemetry.addData("Heading", MathFunctions.scale(MathFunctions.normalizeAngle(dt.getPose().getHeading()), 0, 2*Math.PI, -Math.PI, Math.PI));
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
        if (shoot) {
            targets = shootOrder.getOrder();
            if (time.time() < RUN_TO_POS_TIME) {
                this.kickServoUp(targets[0]);
            }
            else if (time.time() < RUN_TO_POS_TIME * 2 && time.time() >= RUN_TO_POS_TIME) {
                this.kickServoDown(targets[0]);
            }
            else if (time.time() < RUN_TO_POS_TIME * 3 && time.time() >= RUN_TO_POS_TIME * 2) {
                this.kickServoUp(targets[1]);
            }
            else if (time.time() < RUN_TO_POS_TIME * 4 && time.time() >= RUN_TO_POS_TIME * 3) {
                this.kickServoDown(targets[1]);
            }
            else if (time.time() < RUN_TO_POS_TIME * 5 && time.time() >= RUN_TO_POS_TIME * 4) {
                this.kickServoUp(targets[2]);
            }
            else if (time.time() < RUN_TO_POS_TIME * 6 && time.time() >= RUN_TO_POS_TIME * 5) {
                this.kickServoDown(targets[2]);
            }
        }

        if (gamepad1.xWasPressed()) {
            if (!shoot) {
                updateArtifacts();
                shootOrder.search();
                time.reset();
                shoot = true;
            }
            else {
                shoot = false;
                this.kickServoDown(0);
                this.kickServoDown(1);
                this.kickServoDown(2);
            }
        }
    }
    private void initColorSensors() {
        colorSensors = new NormalizedColorSensor[3];
        colorSensors[0] =
                hardwareMap.get(NormalizedColorSensor.class, "color1");
        colorSensors[1] =
                hardwareMap.get(NormalizedColorSensor.class, "color2")
        ;
        colorSensors[2] =
                hardwareMap.get(NormalizedColorSensor.class, "color3")
        ;
    }

    private void initServos() {
        kickers = new Servo[3];
        kickers[0] =
                hardwareMap.get(Servo.class, "kicker1");
        kickers[1] =
                hardwareMap.get(Servo.class, "kicker2");
        kickers[2] =
                hardwareMap.get(Servo.class, "kicker3");
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
        dt.update();
        double forward = (Math.abs(gamepad1.left_stick_y) - 0.05) > 0 ? -gamepad1.left_stick_y : 0;
        double strafe = (Math.abs(gamepad1.left_stick_x) - 0.05) > 0 ? -gamepad1.left_stick_x : 0;
        double turn = (Math.abs(gamepad1.right_stick_x) - 0.05) > 0 ? -gamepad1.right_stick_x : 0;

        dt.teleOpDrive(
                forward,
                strafe,
                turn
        );
    }

    private void kickServoUp(int servoIndex) {
        if (servoIndex == 0) {
            kickers[0].setPosition(0.2);
        }
        else if (servoIndex == 1) {
            kickers[1].setPosition(0.6);
        }
        else if (servoIndex == 2) {
                kickers[2].setPosition(0.6);
        }
    }
    private void kickServoDown(int servoIndex) {
        if (servoIndex == 0) {
            kickers[0].setPosition(0.62);
        }
        else if (servoIndex == 1) {
            kickers[1].setPosition(1);
        }
        else if (servoIndex == 2) {
            kickers[2].setPosition(0.2);
        }
    }

    private void updateAll() {
        updateDrivetrain();

        currentPose = dt.getPose();
        turret.loop(currentPose.getX(), currentPose.getY(), dt.getPose().getHeading());

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
