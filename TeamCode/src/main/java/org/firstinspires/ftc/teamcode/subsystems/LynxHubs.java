package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.util.AllianceColor;

import java.util.List;

public class LynxHubs extends Subsystem {
    private List<LynxModule> allHubs = null;
    private LynxModule.BulkData ctrlHubData = null;
    private LynxModule.BulkData exHubData = null;

    @Override
    public void init(HardwareMap hardwareMap, AllianceColor alliance) {
        allHubs = hardwareMap.getAll(LynxModule.class);

        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }
    }

    @Override
    public void loop() {
        for (LynxModule hub : allHubs) {
            hub.clearBulkCache();
        }

        ctrlHubData = allHubs.get(0).getBulkData();
        exHubData = allHubs.get(1).getBulkData();

        //TODO: Figure out how to get/set motor/servo/sensor values
    }

    @Override
    public void stop() {allHubs = null;}
}
