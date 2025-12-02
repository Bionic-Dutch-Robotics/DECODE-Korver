package org.firstinspires.ftc.teamcode;

import com.bylazar.field.FieldManager;
import com.bylazar.field.Style;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.bylazar.field.PanelsField;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.math.Vector;
import com.pedropathing.paths.callbacks.PathCallback;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Transfer;
import org.firstinspires.ftc.teamcode.subsystems.drivetrain.autonomous.Actions;
import org.firstinspires.ftc.teamcode.util.AllianceColor;

import java.util.concurrent.TimeUnit;

@Autonomous(name="Meet 3 Red")
public class meet3RedAuto extends OpMode {
    public static final Actions paths = new Actions(AllianceColor.Selection.BLUE);
    private static FieldManager panelsField;
    private static Follower follower;
    private static Shooter shooter;
    private static Transfer transfer;
    private static Intake intake;
    public static AutoState autoState;
    public static double savedTime;
    public static ElapsedTime time;
    public static int intakeIndex;

    @Override
    public void init() {
        autoState = AutoState.SHOOT;
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(
                paths.getAlliance() == AllianceColor.Selection.RED ? Constants.redStartPose : Constants.blueStartPose
        );
        shooter = new Shooter(hardwareMap, Constants.shooterCoefficients);
        transfer = new Transfer(hardwareMap);
        intake = new Intake(hardwareMap);
        savedTime = 0.0;
        intakeIndex = 0;
        time = new ElapsedTime();

        Constants.teleOpStartPose = follower.getPose();
    }

    @Override
    public void start() {
        transfer.feed();
        intake.custom(0.2);
        time.reset();

        follower.followPath(paths.shoot1);
    }

    @Override
    public void loop() {
        follower.update();
        switch (autoState) {
            case SHOOT:
                telemetry.addLine("SHOOT");
                for (int i = 0; i < 3; i++) {
                    telemetry.addData("Shoot #", i);
                    this.shoot();
                }

                intakeIndex += 1;
                telemetry.addData("Intake Target: ", intakeIndex);

                if (intakeIndex == 1)   autoState = AutoState.INTAKE1;
                if (intakeIndex == 2)   autoState = AutoState.INTAKE2;
                if (intakeIndex == 3)   autoState = AutoState.PREPARE_TO_EMPTY_CLASSIFIER;

            case INTAKE1:
                telemetry.addLine("INTAKE 1");
                follower.followPath(paths.redIntakeRow1);
                if (follower.getChainIndex() == 0 &&    //Halfway through first path in this sequence
                        follower.getDistanceTraveledOnPath() / (follower.getDistanceTraveledOnPath() + follower.getDistanceRemaining()) >= 0.5) {     //percent of path done
                    telemetry.addLine("INTAKING");
                    intake.intake();
                }
                else if (follower.getChainIndex() == 1 &&
                        follower.getDistanceTraveledOnPath() / (follower.getDistanceTraveledOnPath() + follower.getDistanceRemaining()) >= 0.25) {
                    telemetry.addLine("IDLE INTAKE");
                    intake.custom(0.2);
                }

                if (!follower.isBusy()) {
                    autoState = AutoState.SHOOT;
                }

            case INTAKE2:
                telemetry.addLine("INTAKE 2");
                follower.followPath(paths.redIntakeRow2);
                if (follower.getChainIndex() == 0 &&    //Halfway through first path in this sequence
                        follower.getDistanceTraveledOnPath() / (follower.getDistanceTraveledOnPath() + follower.getDistanceRemaining()) >= 0.5) {     //percent of path done
                    intake.intake();
                    telemetry.addLine("Running Intake");
                }
                else if (follower.getChainIndex() == 1 &&
                        follower.getDistanceTraveledOnPath() / (follower.getDistanceTraveledOnPath() + follower.getDistanceRemaining()) >= 0.25) {
                    intake.custom(0.2);
                    telemetry.addLine("Idling Intake");
                }

                if (!follower.isBusy()) {
                    autoState = AutoState.SHOOT;
                }

            case PREPARE_TO_EMPTY_CLASSIFIER:
                telemetry.addLine("Going to Classifier");
                follower.followPath(paths.goToLever);
                if (!follower.isBusy()) break;
        }
    }

    @Override
    public void stop() {
        Constants.teleOpStartPose = follower.getPose();
    }

    public void shoot() {
        shooter.farShoot();
        if (!follower.isBusy() && shooter.shooter.getVelocity(AngleUnit.DEGREES) == shooter.getTarget()) {
            savedTime = time.time(TimeUnit.SECONDS);
            transfer.reload();
            intake.intake();

            waitForShoot(savedTime);
        }
    }

    public void waitForShoot(double startTime) {
        if (time.time(TimeUnit.SECONDS) - startTime > 0.25) {
            transfer.feed();
            intake.custom(0.2);
        }
        else {
            this.waitForShoot(startTime);
        }
    }

    /**
     * This draws a robot at a specified Pose with a specified
     * look. The heading is represented as a line.
     *
     * @param pose  the Pose to draw the robot at
     * @param style the parameters used to draw the robot with
     */
    public static void drawRobot(Pose pose, Style style) {
        if (pose == null || Double.isNaN(pose.getX()) || Double.isNaN(pose.getY()) || Double.isNaN(pose.getHeading())) {
            return;
        }

        panelsField.setStyle(style);
        panelsField.moveCursor(pose.getX(), pose.getY());
        panelsField.circle(16);

        Vector v = pose.getHeadingAsUnitVector();
        v.setMagnitude(v.getMagnitude() * 16);
        double x1 = pose.getX() + v.getXComponent() / 2, y1 = pose.getY() + v.getYComponent() / 2;
        double x2 = pose.getX() + v.getXComponent(), y2 = pose.getY() + v.getYComponent();

        panelsField.setStyle(style);
        panelsField.moveCursor(x1, y1);
        panelsField.line(x2, y2);
    }

    public static enum AutoState {
        SHOOT,
        INTAKE1,
        INTAKE2,
        PREPARE_TO_EMPTY_CLASSIFIER
    }
}
