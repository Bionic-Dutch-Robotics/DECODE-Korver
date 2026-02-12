package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.subsystems.Subsystem;

public abstract class AugmentedOpMode extends OpMode {
    private Subsystem[] subsystems;
    private Controller mainController = new Controller();
    private Controller subController = new Controller();
    public void registerSubsystems(Subsystem[] systems) {
        subsystems = systems;
    }

    public void initialize(AllianceColor alliance) {
        this.init();
        for (Subsystem subsystem : subsystems) {
            subsystem.init(hardwareMap, alliance);
            subsystem.getController().update();
            subsystem.getController().removeAllBindings();
        }
    }

    public void update() {
        for (Subsystem system : subsystems) {
            system.loop();
        }
        mainController.update();
        subController.update();
    }

    public void kill() {
        for (Subsystem system : subsystems) {
            system.stop();
        }
    }
}
