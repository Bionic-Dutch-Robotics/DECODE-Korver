package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.control.FilteredPIDFController;
import com.pedropathing.control.PIDFController;
import com.pedropathing.follower.Follower;

import com.pedropathing.geometry.Pose;
import com.pedropathing.math.Vector;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import static org.firstinspires.ftc.teamcode.pedroPathing.Constants.followerConstants;
import static org.firstinspires.ftc.teamcode.pedroPathing.Constants.startPose;

import androidx.annotation.NonNull;
//test
@SuppressWarnings("all")
public class Drivetrain {
    private final FilteredPIDFController xPid = new
            FilteredPIDFController(followerConstants.coefficientsDrivePIDF);
    private final FilteredPIDFController yPid = new
            FilteredPIDFController(followerConstants.coefficientsDrivePIDF);
    private PIDFController headingPid;
    public Follower follower;
    //private Gamepad gamepad1;

    public Pose position;
    public Pose drivePower;
    public Vector velocity;
    public final Pose RED_GOAL = new Pose(144, 144, 0);
    public final Pose BLUE_GOAL = new Pose(0, 144, 0);

    /**
     * Initializes a Drivetrain object
     * @param gamepad1  takes `gamepad1` or `gamepad2` - The
    controller responsible for driving
     * @param hardwareMap  An OpMode HardwareMap
     * @param startingPose  The robot's starting Pose, in inches
     */
    public Drivetrain(Gamepad gamepad, HardwareMap hardwareMap, Pose startingPose)
    {
        //this.gamepad1 = gamepad;
        this.follower = Constants.createFollower(hardwareMap);
        this.follower.setStartingPose(startingPose);
        this.follower.update();

        headingPid = new
                PIDFController(followerConstants.coefficientsHeadingPIDF);

        position = new Pose(startingPose.getX(), startPose.getY(),
                startingPose.getHeading());
        velocity = new Vector();

        for (LynxModule module : hardwareMap.getAll(LynxModule.class)) {
            module.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }
    }

    /**
     * Drive the Follower
     * @param forwardPower      Forward `power` vector
     * @param strafePower       Strafe `power` vector
     * @param turnPower         Turn `power` vector
     * @param isRobotCentric    Should the robot drive robot centric or field centric?
     */
    public void drive(double forwardPower, double strafePower, double turnPower,
                    boolean isRobotCentric) {
        follower.setTeleOpDrive (
                forwardPower,
                strafePower,
                turnPower,
                isRobotCentric
        );
    }

    /**
     * Orbits the drivetrain around the selected goal
     * @param posMultiplier Position control coefficient. 1 is full
    speed, 0 is bricked.
     * @param goal          Target to orbit around
     */
    private void orbit(double posMultiplier, Pose goal, Gamepad gamepad) {
        headingPid.updatePosition(position.getHeading());
        headingPid.setTargetPosition(calculateRobotCentricTargetHeading(goal));

        this.drive(
                -gamepad.left_stick_y * posMultiplier,
                gamepad.left_stick_x * posMultiplier,
                headingPid.run(),
                false               // Robot-centric
        );

        //TODO: Finish testing normal drive, migrate to calculateDrive() for position and heading lock
        /*  TEST: Position Lock Drive
        drivePower = calculateDrive(
                        gamepad1.left_stick_x * posMultiplier,
                        gamepad1.left_stick_y * posMultiplier,
                        gamepad1.right_stick_x * headingMultiplier
                    );

        follower.setTeleOpDrive(drivePower.getX(), drivePower.getY(),
drivePower.getHeading());
        */
    }

    /**
     * Sets the drivetrain brake mode
     * @param brake If true, drivetrain will brake when no power is
    applied. If false, drivetrain will coast.
     */
    public void setBrakeMode (boolean brake) {
        follower.startTeleopDrive(brake);
    }

    /**
     * Drives a robot Field-Centric
     * @param driveCoefficient  Speed coefficient. 1 is full speed, 0
    is bricked.
     * @param isAutoOrienting   Toggle Orbit
     */
    public void runTeleOpDrive(@NonNull double forwardPower, @NonNull double strafePower, @NonNull double turnPower,
                               double driveCoefficient, boolean isAutoOrienting, Pose orbitTarget,
                               @NonNull Gamepad gamepad) {
        // TODO: Remove vector inputs, rely on gamepad. Testing needed.
        if (!isAutoOrienting)
        {
            follower.setTeleOpDrive(
                    (forwardPower * driveCoefficient),
                    (strafePower * driveCoefficient),
                    (turnPower * driveCoefficient),
                    false
            );

        /*  TEST Position Lock Drive
        drivePower = calculateDrive(
                        gamepad1.left_stick_x * driveCoefficient,
                        -gamepad1.left_stick_y * driveCoefficient,
                        gamepad1.right_stick_x * driveCoefficient
                    );

        follower.setTeleOpDrive(
            calculateDrive(drivePower.getX(), drivePower.getY(),
drivePower.getHeading());
        );
        */
        } else {
            orbit(driveCoefficient, orbitTarget, gamepad);
        }
    }

    /**
     * Calculate powers for defensive driving.
     * @param xPower    Desired X power
     * @param yPower    Desired Y power
     * @param headingPower  Desired Heading power
     * @param posThreshold  If X or Y power is less than this number,
    respective dimension will be locked in place
     * @param headingThreshold  If heading power is less than this
    number, it will lock in place
     * @return  heading-locked and position-locked vectors.
     */
    public Pose calculateDrive(double xPower, double yPower, double
            headingPower, double posThreshold, double headingThreshold) {
        xPid.updatePosition(position.getX());
        yPid.updatePosition(position.getY());
        headingPid.updatePosition(position.getHeading());

        Pose outputPower = new Pose();

        if (xPower < posThreshold) {
            xPid.setTargetPosition(position.getX());
            outputPower = new Pose(xPid.run(), outputPower.getY(),
                    outputPower.getHeading());
        } else {
            outputPower = new Pose(xPower, outputPower.getY(),
                    outputPower.getHeading());
        }

        if (yPower < posThreshold) {
            yPid.setTargetPosition(position.getY());
            outputPower = new Pose(outputPower.getX(), yPid.run(),
                    outputPower.getHeading());
        } else {
            outputPower = new Pose(outputPower.getX(), yPower,
                    outputPower.getHeading());
        }

        if (headingPower < headingThreshold) {
            headingPid.setTargetPosition(position.getHeading());
            outputPower = new Pose(outputPower.getX(),
                    outputPower.getY(), headingPid.run());
        } else {
            outputPower = new Pose(outputPower.getX(),
                    outputPower.getY(), headingPower);
        }

        return outputPower;
    }

    /**
     * Calculates desired heading for Orbit
     * @return  Desired field-centric heading to Orbit using
     */
    public double calculateRobotCentricTargetHeading(Pose target) {
        double adjacent = Math.abs(target.getX() - position.getX());
        double opposite = Math.abs(target.getY() - position.getY());
        return opposite / adjacent;
    }


    /**
     * Updates all necessary components of the Drivetrain. Call once per loop.
     */
    public void update() {
        follower.update();
        position = follower.getPose();
        velocity = follower.getVelocity();

        xPid.updatePosition(position.getX());
        yPid.updatePosition(position.getY());
        headingPid.updatePosition(position.getHeading());
    }
}