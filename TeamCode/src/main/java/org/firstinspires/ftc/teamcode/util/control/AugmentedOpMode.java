package org.firstinspires.ftc.teamcode.util.control;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.subsystems.Subsystem;
import org.firstinspires.ftc.teamcode.util.AllianceColor;

public abstract class AugmentedOpMode extends OpMode {
    private Subsystem[] subsystems;
    private Controller mainController = new Controller();
    private Controller subController = new Controller();
    public void registerSubsystems(Subsystem[] systems) {
        subsystems = systems;
    }
    private AllianceColor alliance;

    @Override
    public final void init() {
        alliance = this.initialize();
        subsystems = this.getSubsystems();

        for (Subsystem subsystem : subsystems) {
            subsystem.init(hardwareMap, alliance);
            subsystem.controller.update();
            subsystem.controller.removeAllBindings();
        }
    }

    @Override
    public final void init_loop() {
        mainController.update();
        subController.update();
        this.initLoop();

        //for (Subsystem subsystem : subsystems) {}
    }

    @Override
    public final void start() {
        for (Subsystem subsystem : subsystems) {
            subsystem.start();
        }
        this.onStart();
    }

    @Override
    public final void loop() {
        for (Subsystem subsystem : subsystems) {
            subsystem.loop();
            subsystem.controller.update();
        }
        this.onLoop();
    }

    @Override
    public final void stop() {
        this.onStop();
        for (Subsystem subsystem : subsystems) {
            subsystem.stop();
            subsystem.controller.stop();
        }
    }
    public abstract AllianceColor initialize();
    public abstract void initLoop();
    public abstract Subsystem[] getSubsystems();
    public abstract void onStart();
    public abstract void onLoop();
    public abstract void onStop();

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
