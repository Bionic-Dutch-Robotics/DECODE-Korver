package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.transfer.Transfer;
import org.firstinspires.ftc.teamcode.util.Artifact;

@TeleOp(name="TransTest")
public class TransTest extends OpMode {
    private Transfer ts;

    @Override
    public void init() {
        ts = new Transfer(hardwareMap);
        ts.setMotif(new Artifact[] {Artifact.PURPLE, Artifact.GREEN, Artifact.PURPLE});
        ts.start();
    }

    @Override
    public void loop() {
        if (gamepad1.aWasPressed()) {
            ts.fireSortedArtifacts();
        }
    }

    @Override
    public void stop() {
        ts.kicker.stop();
    }
}
