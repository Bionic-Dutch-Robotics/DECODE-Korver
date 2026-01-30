package org.firstinspires.ftc.teamcode.tests;

import static android.os.SystemClock.sleep;
import static org.firstinspires.ftc.teamcode.util.MatchSettings.motif;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.transfer.Transfer;
import org.firstinspires.ftc.teamcode.util.AllianceColor;
import org.firstinspires.ftc.teamcode.util.MatchSettings;

@Disabled
@TeleOp(name="Match Settings / Vision Test")
public class MatchSettingsTest extends OpMode {
    private Transfer transfer;

    @Override
    public void init() {
        MatchSettings.initSelection(hardwareMap, new AllianceColor(AllianceColor.Selection.BLUE), gamepad1);
        transfer = new Transfer(hardwareMap);
    }

    @Override
    public void init_loop() {
        MatchSettings.refreshMotif(telemetry);
        sleep(5);
    }

    @Override
    public void start() {
        transfer.setMotif(motif);
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

        if (gamepad1.aWasPressed()) {
            transfer.fireSortedArtifacts();
        }
    }
}
