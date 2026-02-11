package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.subsystems.transfer.Transfer;

import java.util.ArrayList;

public class SubsystemsManager {
    private ArrayList<Subsystem> subsystems;

    public <T> SubsystemsManager(
            ArrayList<Subsystem> subsystems
    ) {
        this.subsystems = subsystems;
    }

    public void start() {
        for (Subsystem system : subsystems) {
            system.init();
        }
    }

    public void loop() {
        for (Subsystem system : subsystems) {
            system.loop();
        }
    }

    public void stop() {
        for (Subsystem system : subsystems) {
            system.stop();
        }
    }
}
