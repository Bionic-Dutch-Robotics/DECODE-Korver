package org.firstinspires.ftc.teamcode.tests.ivy;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.behaviors.BlockedBehavior;
import com.pedropathing.ivy.behaviors.ConflictBehavior;
import com.pedropathing.ivy.behaviors.EndCondition;
import com.pedropathing.ivy.behaviors.InterruptedBehavior;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.pedropathing.ivy.CommandBuilder;

import org.firstinspires.ftc.teamcode.util.Hardware;
import org.firstinspires.ftc.teamcode.util.Settings;

import java.util.Collections;
import java.util.Set;

public class IvyCommand implements Command {
    private ElapsedTime timer;
    private int state;
    private Integer[] order;

    public IvyCommand() {
        timer = new ElapsedTime(ElapsedTime.Resolution.SECONDS);
    }

    @Override
    public Set<Object> requirements() {
        return Collections.emptySet();
    }

    @Override
    public int priority() {
        return 0;
    }

    @Override
    public InterruptedBehavior interruptedBehavior() {
        return null;
    }

    @Override
    public ConflictBehavior conflictBehavior() {
        return null;
    }

    @Override
    public BlockedBehavior blockedBehavior() {
        return null;
    }

    @Override
    public void start() {
        timer.reset();
        order = Hardware.transfer.sorter.getOrder();
    }

    @Override
    public boolean done() {
        return state >= 6;
    }

    @Override
    public void execute() {
        if (state == 0) {
            Hardware.transfer.kickServoUp(order[0]);
            manageState();
        }
        else if (state == 1) {
            Hardware.transfer.kickServoDown(order[0]);
            manageState();
        }
        else if (state == 2) {
            Hardware.transfer.kickServoUp(order[1]);
            manageState();
        }
        else if (state == 3) {
            Hardware.transfer.kickServoDown(order[1]);
            manageState();
        }
        else if (state == 4) {
            Hardware.transfer.kickServoUp(order[2]);
            manageState();
        }
        else if (state == 5) {
            Hardware.transfer.kickServoDown(order[2]);
            manageState();
        }
    }

    public void manageState() {
        if (timer.time() >= Settings.Positions.Transfer.RUN_TO_POS_TIME && state < 6) {
            timer.reset();
            state++;
        }
    }

    @Override
    public void end(EndCondition endCondition) {

    }
}
