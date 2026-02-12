package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.util.AllianceColor;
import org.firstinspires.ftc.teamcode.util.Command;
import org.firstinspires.ftc.teamcode.util.Controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class Subsystem {
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Future<?> future = null;
    private Command[] initCommands;
    public Controller controller = new Controller();

    abstract public void init(HardwareMap hardwareMap, AllianceColor alliance);
    public void setInitCommands(Command[] commands) {
        this.initCommands = commands;
        for (Command command : commands) {
            controller.bind(command);
        }
    }

    public Command[] getInitCommands() {
        return initCommands;
    }
    abstract public void loop();
    abstract public void stop();

    public Controller getController() {
        return controller;
    }
}
