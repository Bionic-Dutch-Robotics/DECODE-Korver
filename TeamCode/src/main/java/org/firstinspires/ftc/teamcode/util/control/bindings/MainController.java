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
    private Gamepad gamepad1;

    public MainController(Drivetrain dt, Intake intake, Shooter shooter, Transfer transfer, Gamepad gamepad) {
        this.gamepad1 = gamepad;
        controls = new Command[] {
                new Command(
                        gamepad::yWasPressed,
                        () -> dt.lineToPose(new Pose(), dt.follower.getHeading())
                )
        };
    }
}
