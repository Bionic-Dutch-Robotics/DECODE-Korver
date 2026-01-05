package org.firstinspires.ftc.teamcode.subsystems.transfer;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.util.Artifact;
import org.firstinspires.ftc.teamcode.util.ArtifactOrder;
import org.firstinspires.ftc.teamcode.util.Settings;

public class Sorter {
    private NormalizedColorSensor[] colorSensors;
    private ArtifactOrder order;

    public Sorter(HardwareMap hwMap) {
        colorSensors = new NormalizedColorSensor[3];
        for (int i = 0; i < colorSensors.length; i++) {
            colorSensors[i] = hwMap.get(NormalizedColorSensor.class, Settings.HardwareNames.Sorter.COLOR_SENSORS[i]);
        }

        order = new ArtifactOrder(new Artifact[] {Artifact.GREEN, Artifact.PURPLE, Artifact.GREEN});
    }

    public Artifact[] getStoredArtifacts() {
        Artifact[] storedArtifacts = new Artifact[3];
        for (int i = 0; i < colorSensors.length; i++) {
            NormalizedRGBA color = colorSensors[i].getNormalizedColors();
            if (((DistanceSensor) colorSensors[i]).getDistance(DistanceUnit.INCH) < 3) {
                if (color.blue >= color.green) {
                    storedArtifacts[i] = Artifact.PURPLE;
                } else {
                    storedArtifacts[i] = Artifact.GREEN;
                }
            }
            else {
                storedArtifacts[i] = Artifact.UNKNOWN;
            }

            order.addPair(i, storedArtifacts[i]);
        }
        return storedArtifacts;
    }

    public Integer[] getOrder() {
        order.search();
        return order.getOrder();
    }

    public void setMotif(Artifact[] motif) {
        order.setMotif(motif);
    }
    public Artifact[] getMotif() {
        return order.getMotif();
    }
}
