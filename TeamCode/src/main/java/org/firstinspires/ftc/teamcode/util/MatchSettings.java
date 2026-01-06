package org.firstinspires.ftc.teamcode.util;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.Vision;
import org.firstinspires.ftc.teamcode.subsystems.drivetrain.Drivetrain;

import java.util.HashMap;

/**
 * Alliance and match-specific settings. Defaults are red alliance.
 */
public class MatchSettings {
    public static Drivetrain dt;
    public static Artifact[] motif;
    private static Vision vision;

    private static final String AUTO_START_KEY = "autoStartPos";

    public static HashMap<String, Pose> savedPoses;
    public static AllianceColor allianceColor;

    public static void initSelection(HardwareMap hwMap) {
        savedPoses = new HashMap<>();
        allianceColor = new AllianceColor(AllianceColor.Selection.BLUE);
        vision = new Vision(hwMap);
    }

    /**
     * Run this in the init loop - selects the auto configuration
     * @param gp    OpMode or LinearOpMode Gamepad
     */
    public static void selectStartingPosition(Gamepad gp, Telemetry telemetry, HardwareMap hwMap) {
        if ((motif != vision.findMotif(telemetry) || motif == null) && vision.findMotif(telemetry) != null) {
            motif = vision.findMotif(telemetry);
        }

        if (gp.bWasPressed()) {
            allianceColor = new AllianceColor(AllianceColor.Selection.RED);
        }
        else if (gp.xWasPressed()) {
            allianceColor = new AllianceColor(AllianceColor.Selection.BLUE);
        }

        if (gp.dpadUpWasPressed()) {
            if (allianceColor.isRed()){
                savedPoses.put(AUTO_START_KEY, Settings.Positions.Drivetrain.Red.FAR_AUTO_START);
            }
            else {
                savedPoses.put(AUTO_START_KEY, Settings.Positions.Drivetrain.Blue.FAR_AUTO_START);
            }
        }
        else if (gp.dpadDownWasPressed()) {
            savedPoses.put(
                    AUTO_START_KEY,
                    allianceColor.isRed() ? Settings.Positions.Drivetrain.Red.CLOSE_AUTO_START : Settings.Positions.Drivetrain.Blue.CLOSE_AUTO_START
            );
        }
        else if (gp.startWasPressed()) {
            dt = new Drivetrain(hwMap, allianceColor);
        }
        manageTelemetry(telemetry);
    }
    private static void manageTelemetry(Telemetry telemetry) {
        telemetry.addLine("RED      --  gamepad2: B");
        telemetry.addLine("BLUE     --  gamepad2: X");
        telemetry.addLine("CLOSE    --  gamepad2: D-Pad UP");
        telemetry.addLine("FAR      --  gamepad2: D-Pad DOWN");
        telemetry.addLine("Select   --  gamepad2: start");
        telemetry.addData("Motif: ", motif);
        telemetry.update();
    }
}