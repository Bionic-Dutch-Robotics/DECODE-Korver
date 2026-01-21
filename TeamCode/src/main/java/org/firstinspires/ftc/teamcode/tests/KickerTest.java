package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.transfer.Transfer;
import org.firstinspires.ftc.teamcode.util.Artifact;

@TeleOp(name="Kicker Test")
public class KickerTest extends OpMode {
    private Transfer transfer;
    @Override
    public void init() {
        transfer = new Transfer(hardwareMap);
        transfer.setMotif(new Artifact[]{Artifact.PURPLE, Artifact.GREEN, Artifact.PURPLE});
        transfer.kickAllServosDown();
    }

    @Override
    public void loop() {
        telemetry.update();
        if (gamepad1.aWasPressed()) {
            transfer.fireSortedArtifacts();
        }

        for (double position : transfer.kicker.getServoPositions()) {
            telemetry.addData("Servo Pos", position);
        }
    }
}
