package org.firstinspires.ftc.teamcode.util.control.bindings;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.subsystems.shooter.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.transfer.Transfer;
import org.firstinspires.ftc.teamcode.util.control.Command;
import org.firstinspires.ftc.teamcode.util.control.Controller;

public class SubController extends Controller
{
    public Command[] controls;
    private Shooter shooter;
    public SubController(Transfer transfer, Shooter shooter, Gamepad gamepad) {
        this.shooter = shooter;
        controls = new Command[]{
                new Command(
                        gamepad::aWasPressed,
                        transfer::fireSortedArtifacts
                ),
                new Command(
                        gamepad::bWasPressed,
                        transfer::runSlow
                ),
                new Command(
                        gamepad::leftBumperWasPressed,
                        () -> shooter.flywheel.setVoltageComp(
                                shooter.flywheel.getVoltageComp() + 0.015
                        )
                ),
                new Command(
                        gamepad::rightBumperWasPressed,
                        () -> shooter.flywheel.setVoltageComp(
                                shooter.flywheel.getVoltageComp() - 0.015
                        )
                ),
                new Command(
                        gamepad::leftTriggerWasPressed,
                        () -> shooter.turret.setLiveOffset(0.05)
                ),
                new Command(
                        gamepad::rightTriggerWasPressed,
                        () -> shooter.turret.setLiveOffset(-0.05)
                )
        };
        this.setController(controls);
    }

    public void runShooter(Follower follower) {
        shooter.runLoop(
                follower.getPose(),
                follower.getVelocity(),
                follower.getAngularVelocity()
        );
    }
}
