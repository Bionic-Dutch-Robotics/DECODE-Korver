package org.firstinspires.ftc.teamcode.teleops;

import static org.firstinspires.ftc.teamcode.util.MatchSettings.dt;
import static org.firstinspires.ftc.teamcode.util.MatchSettings.motif;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.shooter.Flywheel;
import org.firstinspires.ftc.teamcode.subsystems.shooter.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.transfer.Transfer;
import org.firstinspires.ftc.teamcode.util.AllianceColor;
import org.firstinspires.ftc.teamcode.util.MatchSettings;
import org.firstinspires.ftc.teamcode.util.Settings;

@TeleOp(name="Blue Far")
public class BlueFar extends OpMode {
    private Transfer transfer;
    private Shooter shooter;
    private Intake intake;
    private final AllianceColor alliance = new AllianceColor(AllianceColor.Selection.BLUE);

    @Override
    public void init() {
        /*
        Normally all MatchSettings configuration would be done in AUTO.
        This is a TEST only.
         */
        MatchSettings.initSelection(hardwareMap, alliance);
        transfer = new Transfer(hardwareMap);
        intake = new Intake(hardwareMap);
        shooter = new Shooter(hardwareMap);
        shooter.setAlliance(alliance);
    }

    @Override
    public void init_loop() {
        MatchSettings.refreshMotif(telemetry);
    }

    @Override
    public void start() {
        transfer.setMotif(motif);
        dt.startTeleOpDrive();
    }



    @Override
    public void loop() {
        dt.update();
        shooter.runLoop(dt.getPose().getX(), dt.getPose().getY(), dt.getPose().getHeading());
        dt.follower.setTeleOpDrive(
                -gamepad1.left_stick_y,
                -gamepad1.left_stick_x,
                -gamepad1.right_stick_x,
                true
        );

        if (gamepad1.aWasPressed()) {
            if (intake.spinner.getPower() == Settings.Positions.Intake.INTAKE_SPEED) {
                intake.stop();
            }
            else {
                intake.run();
            }
        }

        if (gamepad1.bWasPressed()) {
            transfer.fireSortedArtifacts();
        }
        if (gamepad1.dpadUpWasPressed()) {
            transfer.cancelFire();
        }
    }
}
