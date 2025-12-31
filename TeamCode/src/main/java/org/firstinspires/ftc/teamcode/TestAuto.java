package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
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

@Autonomous(name="TEST")
public class TestAuto extends OpMode {
    public static final Actions paths = new Actions(AllianceColor.Selection.RED);
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
                Constants.redStartPose
        );
        shooter = new Shooter(hardwareMap, Constants.shooterCoefficients);
        transfer = new Transfer(hardwareMap);
        intake = new Intake(hardwareMap);
        savedTime = 0.0;
        intakeIndex = 0;
        time = new ElapsedTime();

        //Constants.teleOpStartPose = follower.getPose();
    }

    @Override
    public void start() {
        transfer.feed();
        //intake.custom(0.2);
        //time.reset();

        //follower.followPath(paths.shoot1);
    }

    @Override
    public void loop() {
        if(gamepad1.a) {
            follower.followPath(paths.shoot1);
        }
        follower.update();
        if (gamepad1.b) {
            follower.followPath(paths.redIntakeRow1);
        }
        if (gamepad1.x) {
            follower.followPath(paths.redIntakeRow2);
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
            if (time.time(TimeUnit.SECONDS) - savedTime >= 0.25) {
                transfer.feed();
                intake.custom(0.2);
            }
        }
    }

    /*
     * This draws a robot at a specified Pose with a specified
     * look. The heading is represented as a line.
     *
     * @param pose  the Pose to draw the robot at
     * @param style the parameters used to draw the robot with
     */
    /*public static void drawRobot(Pose pose, Style style) {
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
    }*/

    public static enum AutoState {
        SHOOT,
        INTAKE1,
        INTAKE2,
        PREPARE_TO_EMPTY_CLASSIFIER
    }
}
