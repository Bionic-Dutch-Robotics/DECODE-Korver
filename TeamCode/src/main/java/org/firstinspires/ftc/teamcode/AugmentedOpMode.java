package org.firstinspires.ftc.teamcode;

import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.subsystems.Subsystem;
import org.firstinspires.ftc.teamcode.util.AllianceColor;

public abstract class AugmentedOpMode extends OpMode {
    private Subsystem[] subsystems;
    private AllianceColor alliance;
    public void registerSubsystems(Subsystem[] systems) {
        subsystems = systems;
    }

    @Override
    public final void init() {
        Scheduler.reset();
        alliance = this.initialize();
        subsystems = this.getSubsystems();

        for (Subsystem subsystem : subsystems) {
            subsystem.init(hardwareMap, alliance);
        }
    }

    @Override
    public final void init_loop() {
        Scheduler.execute();
        this.initLoop();
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
        }
        Scheduler.execute();
        this.onLoop();
    }

    @Override
    public final void stop() {
        this.onStop();
        Scheduler.reset();
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
        Scheduler.execute();
    }

    public void kill() {
        for (Subsystem system : subsystems) {
            system.stop();
        }
    }
}
