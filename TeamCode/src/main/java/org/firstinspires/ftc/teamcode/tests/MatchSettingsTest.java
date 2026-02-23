package org.firstinspires.ftc.teamcode.tests;

import static android.os.SystemClock.sleep;
import static org.firstinspires.ftc.teamcode.util.Hardware.transfer;
import static org.firstinspires.ftc.teamcode.util.MatchSettings.motif;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.transfer.Transfer;
import org.firstinspires.ftc.teamcode.util.AllianceColor;
import org.firstinspires.ftc.teamcode.util.MatchSettings;

@TeleOp(name="Match Settings / Vision Test")
public class MatchSettingsTest extends OpMode {

    @Override
    public void init() {
        MatchSettings.initSelection(hardwareMap, new AllianceColor(AllianceColor.Selection.BLUE), gamepad1);
    }

    @Override
    public void init_loop() {
        MatchSettings.refreshMotif(telemetry);
        sleep(5);
    }

    @Override
    public void start() {
        MatchSettings.start();
    }

    @Override
    public void loop() {
        telemetry.update();
        if (motif != null) {
            telemetry.addData("Motif", motif[0]);
            telemetry.addData("Motif", motif[1]);
            telemetry.addData("Motif", motif[2]);
        }

        telemetry.addData("Alliance", MatchSettings.allianceColor.getSelection().name());
        telemetry.addLine(String.format("%o", MatchSettings.findError().longValue()));

        if (gamepad1.aWasPressed()) {
            transfer.fireSortedArtifacts();
        }
    }
}
