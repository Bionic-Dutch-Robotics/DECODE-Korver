package org.firstinspires.ftc.teamcode.util;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.Vision;
import org.firstinspires.ftc.teamcode.subsystems.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.shooter.Turret;
import org.firstinspires.ftc.teamcode.subsystems.transfer.Transfer;

import java.util.Arrays;
import java.util.HashMap;

/**     TODO: Move all hardware to dedicated file
 * Alliance and match-specific settings. Defaults are red alliance.
 */
public class MatchSettings {
    public static final AllianceColor BLUE = new AllianceColor(AllianceColor.Selection.BLUE);
    public static final AllianceColor RED = new AllianceColor(AllianceColor.Selection.RED);
    public static Drivetrain dt;
    public static Transfer transfer;
    public static Turret turret;
    public static Artifact[] motif;
    private static Vision vision;

    private static final String AUTO_START_KEY = "autoStartPos";

    public static HashMap<String, Pose> savedPoses;
    public static AllianceColor allianceColor;

    public static void initSelection(HardwareMap hwMap, AllianceColor alliance) {
        savedPoses = new HashMap<>();
        allianceColor = alliance;
        vision = new Vision(hwMap);
        dt = new Drivetrain(hwMap, allianceColor);
        turret = new Turret(hwMap);
        turret.setAlliance(allianceColor);
        transfer = new Transfer(hwMap);
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
        /*telemetry.addLine("RED      --  gamepad2: B");
        telemetry.addLine("BLUE     --  gamepad2: X");
        telemetry.addLine("CLOSE    --  gamepad2: D-Pad UP");
        telemetry.addLine("FAR      --  gamepad2: D-Pad DOWN");
        telemetry.addLine("Select   --  gamepad2: start");*/
        telemetry.addData("Motif: ", Arrays.toString(motif));
        telemetry.update();
    }
}