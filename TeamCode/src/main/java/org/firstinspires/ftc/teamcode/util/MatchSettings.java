package org.firstinspires.ftc.teamcode.util;

import static org.firstinspires.ftc.teamcode.util.Hardware.transfer;

import org.firstinspires.ftc.teamcode.util.Hardware;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.Vision;

import java.util.Arrays;

/**
 * Alliance and match-specific settings. Defaults are red alliance.
 */
public class MatchSettings {
    public static final AllianceColor BLUE = new AllianceColor(AllianceColor.Selection.BLUE);
    public static final AllianceColor RED = new AllianceColor(AllianceColor.Selection.RED);
    public static Artifact[] motif;
    private static Vision vision;
    public static AllianceColor allianceColor;

    public static void initSelection(HardwareMap hwMap, AllianceColor alliance) {
        allianceColor = alliance;
        vision = new Vision(hwMap);
        Hardware.initialize(hwMap, alliance);
    }

    public static void start() {
        if (motif == null) {
            motif = new Artifact[] {Artifact.PURPLE, Artifact.GREEN, Artifact.PURPLE};
        }
        transfer.setMotif(motif);
    }

    /**
     * Run this in the init loop - selects the auto configuration
     */
    public static void refreshMotif(Telemetry telemetry) {
        Artifact[] detectedMotif = vision.findMotif(telemetry);

        if ((motif != detectedMotif) && detectedMotif != null) {
            motif = vision.findMotif(telemetry);
        }

        manageTelemetry(telemetry);
    }
    private static void manageTelemetry(Telemetry telemetry) {
        telemetry.addData("Motif: ", Arrays.toString(motif));
        telemetry.update();
    }
}