package org.firstinspires.ftc.teamcode.util;

import static org.firstinspires.ftc.teamcode.util.Hardware.dt;
import static org.firstinspires.ftc.teamcode.util.Hardware.transfer;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.pedropathing.geometry.Pose;

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
    public static Vision vision;
    public static AllianceColor allianceColor;

    public static class AutoToTeleOpCarryOver {
        public static Pose drivetrainEndPos = null;
        public static double turretEndRadians = 0.0;
    }


    public static void initSelection(HardwareMap hwMap, AllianceColor alliance, Gamepad gamepad1) {
        allianceColor = alliance;
        Pose gamepadReference = new Pose(
                -gamepad1.left_stick_x,
                -gamepad1.left_stick_y,
                -gamepad1.right_stick_x
        );
        vision = new Vision(hwMap);
        Hardware.initialize(hwMap, alliance, gamepadReference);
    }

    public static void start() {
        //vision.stop();
        if (motif == null) {
            motif = new Artifact[] {Artifact.PURPLE, Artifact.GREEN, Artifact.PURPLE};
        }
        transfer.setMotif(motif);
        transfer.start();

        dt.startTeleOpDrive();
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
        //telemetry.update();
    }

    public static Double findError() {
        return vision.findTurretErrorFromBlueGoal();
    }
}