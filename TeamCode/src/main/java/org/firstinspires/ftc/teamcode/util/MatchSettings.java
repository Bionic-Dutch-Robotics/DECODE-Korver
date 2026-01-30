package org.firstinspires.ftc.teamcode.util;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.HashMap;

/**
 * Alliance and match-specific settings. Defaults are red alliance.
 */
public class MatchSettings {
    public static Pose autoStart, teleStart;


    private static final String AUTO_START_KEY = "autoStartPos";
    private static final String STORED_POSE_KEY = "storedPose";
    private static final String ALLIANCE_COLOR_KEY = "allianceColor";

    public HashMap<String, Pose> savedPoses;
    public AllianceColor allianceColor;

    public MatchSettings(HashMap<String, Pose> savedPoses) {
        this.savedPoses = savedPoses;
    }

    /**
     * Run this in the init loop - selects the auto configuration
     * @param gp    OpMode or LinearOpMode Gamepad
     */
    public void selectStartingPosition(Gamepad gp, Telemetry telemetry) {
        if (gp.bWasPressed()) {
            allianceColor = AllianceColor.RED;
        }
        else if (gp.xWasPressed()) {
            allianceColor = AllianceColor.BLUE;
        }

        if (gp.dpadUpWasPressed()) {
            if (allianceIsRed()){
                savedPoses.put(AUTO_START_KEY, Settings.Positions.Drivetrain.Red.FAR_AUTO_START);
            }
            else {
                savedPoses.put(AUTO_START_KEY, Settings.Positions.Drivetrain.Blue.FAR_AUTO_START);
            }
        }
        else if (gp.dpadDownWasPressed()) {
            savedPoses.put(AUTO_START_KEY,
                    allianceIsRed()? Settings.Positions.Drivetrain.Red.CLOSE_AUTO_START : Settings.Positions.Drivetrain.Blue.CLOSE_AUTO_START
                    );
        }
        manageTelemetry(telemetry);
    }
    private void manageTelemetry(Telemetry telemetry) {
        telemetry.addLine("RED      --  gamepad2: B");
        telemetry.addLine("BLUE     --  gamepad2: X");
        telemetry.addLine("CLOSE    --  gamepad2: D-Pad UP");
        telemetry.addLine("FAR      --  gamepad2: D-Pad DOWN");
        telemetry.update();
    }

    public enum AllianceColor {
        RED, BLUE
    }
    public enum AutoStartingPosition {
        CLOSE, FAR
    }

    public boolean allianceIsRed() {
        return allianceColor == AllianceColor.RED;
    }
}