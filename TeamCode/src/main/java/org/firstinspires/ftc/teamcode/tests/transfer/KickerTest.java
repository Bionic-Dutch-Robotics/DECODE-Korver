package org.firstinspires.ftc.teamcode.tests.transfer;

import static org.firstinspires.ftc.teamcode.util.Hardware.shooter;
import static org.firstinspires.ftc.teamcode.util.Hardware.transfer;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.AllianceColor;
import org.firstinspires.ftc.teamcode.util.Artifact;
import org.firstinspires.ftc.teamcode.util.MatchSettings;

@TeleOp(name="Kicker Test")
public class KickerTest extends OpMode {
    @Override
    public void init() {
        MatchSettings.initSelection(hardwareMap, new AllianceColor(AllianceColor.Selection.BLUE), gamepad1);
        transfer.setMotif(new Artifact[]{Artifact.PURPLE, Artifact.GREEN, Artifact.PURPLE});
        transfer.kickAllServosDown();
    }

    @Override
    public void loop() {
        shooter.flywheel.update(150);
        telemetry.update();
        if (gamepad1.aWasPressed()) {
            transfer.fireSortedArtifacts();
        }

        for (double position : transfer.kicker.getServoPositions()) {
            telemetry.addData("Servo Pos", position);
        }
    }
}
