package org.firstinspires.ftc.teamcode.autos;

import static org.firstinspires.ftc.teamcode.util.MatchSettings.dt;
import static org.firstinspires.ftc.teamcode.util.MatchSettings.motif;
import static org.firstinspires.ftc.teamcode.util.MatchSettings.transfer;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.util.Artifact;
import org.firstinspires.ftc.teamcode.util.MatchSettings;

@Autonomous(name="Blue", preselectTeleOp="Blue Far")
public class Blue extends OpMode {

    @Override
    public void init() {
        MatchSettings.initSelection(hardwareMap, MatchSettings.BLUE);
    }

    @Override
    public void init_loop() {
        MatchSettings.refreshMotif(telemetry);
    }

    @Override
    public void start() {
        if (motif == null) {
            motif = new Artifact[] {Artifact.PURPLE, Artifact.GREEN, Artifact.PURPLE};
        }
        transfer.setMotif(motif);
    }

    @Override
    public void loop() {

    }
}
