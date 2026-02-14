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

    public MainController(Drivetrain dt, Intake intake, Gamepad gamepad) {
        controls = new Command[] {
                new Command(
                        gamepad::yWasPressed,
                        () -> dt.lineToPose(new Pose(72,72, Math.PI), dt.follower.getHeading())
                ),
                new Command(
                        gamepad::xWasPressed,
                        () -> dt.lineToPose(new Pose(55, 15, Math.PI), dt.follower.getHeading())
                ),
                new Command(
                        gamepad::aWasPressed,
                        intake::toggle
                ),
                new Command(
                        gamepad::bWasPressed,
                        intake::eject
                ),
        };
        this.setController(controls);
    }
}
