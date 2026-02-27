package org.firstinspires.ftc.teamcode.util.control.bindings;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.util.AllianceColor;
import org.firstinspires.ftc.teamcode.util.Settings;
import org.firstinspires.ftc.teamcode.util.control.Command;
import org.firstinspires.ftc.teamcode.util.control.Controller;

public class MainController extends Controller {
    public static Command[] controls;
    private static boolean runToPos = false;
    private final Pose resetCornerPoseBlue = new Pose(144-17.75, 144-17.75, Math.PI);
    private final Pose resetCornerPoseRed = new Pose(17.75, 17.75, Math.PI);

    public MainController(Drivetrain dt, Intake intake, Gamepad gamepad, AllianceColor alliance) {
        controls = new Command[] {
                new Command(
                        gamepad::leftBumperWasPressed,
                        intake::toggle
                ),
                new Command(
                        gamepad::rightBumperWasPressed,
                        intake::eject
                ),
                new Command(
                        gamepad::yWasPressed,
                        () -> dt.follower.setPose(alliance.isRed() ? resetCornerPoseRed : resetCornerPoseBlue)
                ),
                new Command(
                        gamepad::aWasPressed,
                        () -> {
                            if (!runToPos) {
                                dt.lineToCloseShoot(dt.follower.getHeading());
                                runToPos = true;
                            }
                            else {
                                runToPos = false;
                                dt.follower.breakFollowing();
                                dt.follower.startTeleopDrive();
                            }
                        }
                ),
                new Command(
                        gamepad::xWasPressed,
                        () ->{dt.lineToPose(
                                alliance.isRed() ? Settings.Positions.Drivetrain.Red.PARK : Settings.Positions.Drivetrain.Blue.PARK,
                                dt.getPose().getHeading());
                        }
                ),
                new Command(
                        gamepad::bWasPressed,
                        () -> {
                            if (!runToPos) {
                                dt.lineToFarShoot(dt.follower.getHeading());
                                runToPos = true;
                            } else {
                                runToPos = false;
                                dt.follower.breakFollowing();
                                dt.follower.startTeleopDrive();
                            }
                        }
                )
        };
        this.setController(controls);
    }
}
