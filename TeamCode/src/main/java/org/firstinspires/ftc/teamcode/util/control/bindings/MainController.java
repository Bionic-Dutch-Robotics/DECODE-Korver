package org.firstinspires.ftc.teamcode.util.control.bindings;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.shooter.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.transfer.Transfer;
import org.firstinspires.ftc.teamcode.util.control.Command;
import org.firstinspires.ftc.teamcode.util.control.Controller;

public class MainController extends Controller {
    public static Command[] controls;
    private static boolean runToPos = false;
    private final Pose resetCornerPose = new Pose(144-17.75, 144-17.75);

    public MainController(Drivetrain dt, Intake intake, Gamepad gamepad) {
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
                        () -> dt.follower.setPose(new Pose(144-17.75, 144-17.75, Math.PI))
                ),
                new Command(
                        gamepad::aWasPressed,
                        () -> {
                            if (!runToPos) {
                                dt.lineToCloseShoot(dt.follower.getHeading());
                                runToPos = true;
                            }
                            else if (runToPos) {
                                runToPos = false;
                                dt.follower.breakFollowing();
                                dt.follower.startTeleopDrive();
                            }
                        }
                ),
                new Command(
                        gamepad::aWasPressed,
                        () ->{}
                )
        };
        this.setController(controls);
    }
}
