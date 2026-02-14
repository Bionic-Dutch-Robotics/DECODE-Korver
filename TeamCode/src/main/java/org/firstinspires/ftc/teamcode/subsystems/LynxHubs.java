package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.util.AllianceColor;

public class LynxHubs extends Subsystem {
    private LynxModule[] allHubs;

    @Override
    public void init(HardwareMap hardwareMap, AllianceColor alliance) {
        allHubs = hardwareMap.getAll(LynxModule.class).toArray(allHubs);

        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }
    }

    @Override
    public void loop() {
        for (LynxModule hub : allHubs) {
            hub.getBulkData();
        }
    }

    @Override
    public void stop() {allHubs = null;}
}
