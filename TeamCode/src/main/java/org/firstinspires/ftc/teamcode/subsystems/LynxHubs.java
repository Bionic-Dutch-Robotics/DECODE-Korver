package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

import org.firstinspires.ftc.robotcontroller.external.samples.SensorGoBildaPinpoint;
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

        // Servos cannot be read
    }

    public double getMotorPosition(Hub hub, int port) {
        return hub == Hub.CONTROL_HUB ? ctrlHubData.getMotorCurrentPosition(port)
                : exHubData.getMotorCurrentPosition(port);
    }

    public double getAnalogInputVoltage(Hub hub, int port) {
        return hub == Hub.CONTROL_HUB ? ctrlHubData.getAnalogInputVoltage(port) :
                exHubData.getAnalogInputVoltage(port);
    }

    public byte[] getSensorData(I2cDevice sensor, int ireg, int creg) {
        return sensor.device.read(ireg, creg);
    }

    @Override
    public void stop() {allHubs = null;}

    public enum Hub {
        CONTROL_HUB,
        EXPANSION_HUB
    }

    public static class I2cDevice {
        I2cDeviceSynch device;
        String name;
    }
}
