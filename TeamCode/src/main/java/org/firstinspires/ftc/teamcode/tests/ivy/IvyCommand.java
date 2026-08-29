package org.firstinspires.ftc.teamcode.tests.ivy;

import static org.firstinspires.ftc.teamcode.util.Hardware.dt;
import static org.firstinspires.ftc.teamcode.util.Hardware.shooter;
import static org.firstinspires.ftc.teamcode.util.Hardware.transfer;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.behaviors.BlockedBehavior;
import com.pedropathing.ivy.behaviors.ConflictBehavior;
import com.pedropathing.ivy.behaviors.EndCondition;
import com.pedropathing.ivy.behaviors.InterruptedBehavior;

import org.firstinspires.ftc.teamcode.util.MatchSettings;

import java.util.Set;

public class IvyCommand implements Command {
    @Override
    public void start() {
        MatchSettings.start();
    }

    @Override
    public void execute() {
        dt.update();
        shooter.runLoop(
                dt.getPose(),
                dt.follower.getVelocity(),
                dt.follower.getAngularVelocity());
    }

    @Override
    public boolean done() {
        return false;
    }

    @Override
    public void end(EndCondition endCondition) {
        transfer.kicker.stop();
    }

    @Override
    public Set<Object> requirements() {
        return Set.of(dt, shooter);
    }

    @Override
    public int priority() {
        return 0;
    }

    @Override
    public InterruptedBehavior interruptedBehavior() {
        return InterruptedBehavior.END;
    }

    @Override
    public BlockedBehavior blockedBehavior() {
        return BlockedBehavior.CANCEL;
    }

    @Override
    public ConflictBehavior conflictBehavior() {
        return ConflictBehavior.OVERRIDE;
    }
}
