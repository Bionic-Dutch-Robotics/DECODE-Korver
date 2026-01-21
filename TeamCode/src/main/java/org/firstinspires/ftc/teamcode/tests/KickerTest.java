package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.transfer.Transfer;
import org.firstinspires.ftc.teamcode.util.Artifact;

@TeleOp(name="Kickertewst")
public class KickerTest extends OpMode {
    private Transfer transfer;
    @Override
    public void init() {
        transfer = new Transfer(hardwareMap);
        transfer.kickAllServosDown();
        transfer.setMotif(
                new Artifact[] {Artifact.PURPLE, Artifact.GREEN, Artifact.PURPLE}
        );
    }

    @Override
    public void loop() {
        if (gamepad1.aWasPressed()) {
            transfer.fireSortedArtifacts();
        }
    }
}
