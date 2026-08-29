package org.firstinspires.ftc.teamcode.teleops.bindings;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.subsystems.shooter.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.transfer.Transfer;

import java.util.function.BooleanSupplier;

public class SubController
{
    public Command[] controls;
    private Shooter shooter;
    public SubController(Transfer transfer, Shooter shooter, Gamepad gamepad) {
        this.shooter = shooter;
        controls = new Command[]{
                event(() -> gamepad.aWasPressed() && gamepad.bWasPressed(), transfer::fireSortedArtifacts),
                event(gamepad::bWasPressed, transfer::runSlow),
                event(gamepad::leftBumperWasPressed,
                        () -> shooter.flywheel.setVoltageComp(
                                shooter.flywheel.getVoltageComp() + 0.015)),
                event(gamepad::rightBumperWasPressed,
                        () -> shooter.flywheel.setVoltageComp(
                                shooter.flywheel.getVoltageComp() - 0.015)),
                event(gamepad::leftTriggerWasPressed,
                        () -> shooter.turret.setLiveOffset(0.1)),
                event(gamepad::rightTriggerWasPressed,
                        () -> shooter.turret.setLiveOffset(-0.1))
        };
        Scheduler.schedule(controls);
    }

    public void runShooter(Follower follower) {
        shooter.runLoop(
                follower.getPose(),
                follower.getVelocity(),
                follower.getAngularVelocity()
        );
    }

    private static Command event(BooleanSupplier condition, Runnable action) {
        return Command.build()
                .setExecute(() -> {
                    if (condition.getAsBoolean()) {
                        action.run();
                    }
                })
                .setDone(() -> false);
    }
}
