package org.firstinspires.ftc.teamcode.util.control.bindings;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.subsystems.shooter.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.transfer.Transfer;
import org.firstinspires.ftc.teamcode.util.control.Command;
import org.firstinspires.ftc.teamcode.util.control.Controller;

public class SubController extends Controller
{
    public static Command[] controls;
    public SubController(Transfer transfer, Shooter shooter, Gamepad gamepad) {
        controls = new Command[]{
                new Command(
                        gamepad::aWasPressed,
                        transfer::fireSortedArtifacts
                ),
                new Command(
                        gamepad::bWasPressed,
                        transfer::cancelFire
                ),
        };
        this.setController(controls);
    }
}
