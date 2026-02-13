package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.util.AllianceColor;
import org.firstinspires.ftc.teamcode.util.control.Command;
import org.firstinspires.ftc.teamcode.util.control.Controller;

public abstract class Subsystem {
    private Command[] initCommands;
    private Command[] teleCommands;
    public Controller controller = new Controller();

    abstract public void init(HardwareMap hardwareMap, AllianceColor alliance);
    public void setInitCommands(Command[] commands) {
        this.initCommands = commands;
    }

    public void setTeleCommands(Command[] commands) {
        this.teleCommands = commands;
    }

    public void init() {
        for (Command command : initCommands) {
            controller.bind(command);
        }
    }

    public void start() {
        for (Command command : teleCommands) {
            controller.bind(command);
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

    public Controller getController() {
        return controller;
    }
}
