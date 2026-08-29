package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.util.AllianceColor;

public abstract class Subsystem {
    private Command[] initCommands;
    private Command[] teleCommands;

    abstract public void init(HardwareMap hardwareMap, AllianceColor alliance);
    public void setInitCommands(Command[] commands) {
        this.initCommands = commands;
    }

    public void setTeleCommands(Command[] commands) {
        this.teleCommands = commands;
    }

    public void init() {
        if (initCommands != null) {
            Scheduler.schedule(initCommands);
        }
    }

    public void start() {
        if (teleCommands != null) {
            Scheduler.schedule(teleCommands);
        }
    }

    public Command[] getInitCommands() {
        return initCommands;
    }
    public Command[] getTeleCommands() {
        return teleCommands;
    }

    abstract public void loop();
    abstract public void stop();
}
