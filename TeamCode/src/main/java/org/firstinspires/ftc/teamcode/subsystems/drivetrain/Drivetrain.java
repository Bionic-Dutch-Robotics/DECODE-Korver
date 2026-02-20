package org.firstinspires.ftc.teamcode.subsystems.drivetrain;

import com.pedropathing.control.FilteredPIDFController;
import com.pedropathing.control.PIDFController;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Subsystem;
import org.firstinspires.ftc.teamcode.util.AllianceColor;
import org.firstinspires.ftc.teamcode.util.Settings;

public class Drivetrain extends Subsystem {
    public FilteredPIDFController xPid, yPid;
    public PIDFController headingPid;
    public Follower follower = null;
    private Pose gamepadReference = null;
    private Pose multipliers = new Pose();
    private AllianceColor alliance = null;

    /**
     * Creates a new drivetrain
     * @param hwMap     A HardwareMap object from an OpMode or LinearOpMode
     */
    public Drivetrain(HardwareMap hwMap, AllianceColor alliance, Pose gamepadReference, Pose multipliers) {
        if (follower == null) {
            follower = Constants.createFollower(hwMap);
            follower.setStartingPose(
                    alliance.isRed() ? Settings.Positions.Drivetrain.Red.FAR_AUTO_START : Settings.Positions.Drivetrain.Blue.FAR_AUTO_START
            );
        }
        this.gamepadReference = gamepadReference.copy();
        this.multipliers = multipliers.copy();
        this.alliance = alliance;
        //follower.teleOpLock(false, false, true);
    }

    @Override
    public void init(HardwareMap hardwareMap, AllianceColor alliance) {
        super.init();
        if (follower == null) {
            follower = Constants.createFollower(hardwareMap);
            follower.setStartingPose(alliance.isRed() ? Settings.Positions.Drivetrain.Red.FAR_AUTO_START : Settings.Positions.Drivetrain.Blue.FAR_AUTO_START);
        }
    }
    public void setReferences(Pose gamepadReference, Pose multipliers) {
        this.gamepadReference = gamepadReference;
        this.multipliers = multipliers;
    }

    @Override
    public void loop() {
        follower.update();
    }

    @Override
    public void stop() {
        follower.breakFollowing();
        follower.startTeleopDrive(true);
        follower.setTeleOpDrive(
                0,0,0
        );
        follower.update();
    }

    public void startTeleOpDrive() {
        follower.startTeleopDrive(true);
    }

    /**
     * Moves the drivetrain in specified direction.
     */
    public void update() {
        follower.update();
    }
    public void teleOpDrive(double forwardPower, double strafePower, double turnPower) {
        follower.setTeleOpDrive(
                multipliers.getY() * (forwardPower - gamepadReference.getY()),
                multipliers.getX() * (strafePower - gamepadReference.getX()),
                multipliers.getHeading() * (turnPower - gamepadReference.getHeading()),
                true
        );
    }

    public void lineToPose(Pose target, double currentHeading) {
        Path path = new Path(new BezierLine(follower::getPose, target));
        path.setLinearHeadingInterpolation(currentHeading, target.getHeading());
        follower.breakFollowing();
        follower.followPath(
                path
        );
    }

    public void lineToFarShoot(double currentHeading) {
        this.lineToPose(alliance.isRed() ? Settings.Positions.Drivetrain.Red.FAR_SHOOT : Settings.Positions.Drivetrain.Blue.FAR_SHOOT, currentHeading);
    }

    public void lineToCloseShoot(double currentHeading) {
        this.lineToPose(alliance.isRed() ? Settings.Positions.Drivetrain.Red.CLOSE_SHOOT : Settings.Positions.Drivetrain.Blue.CLOSE_SHOOT, currentHeading);
    }
    /**
     * Gets real-time position of the robot, in PedroPathing coordinates.
     */
    public Pose getPose() {
        return follower.getPose();
    }
}
