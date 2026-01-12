package org.firstinspires.ftc.teamcode.teleops;

import static org.firstinspires.ftc.teamcode.util.MatchSettings.dt;
import static org.firstinspires.ftc.teamcode.util.MatchSettings.motif;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.shooter.Flywheel;
import org.firstinspires.ftc.teamcode.subsystems.shooter.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.transfer.Transfer;
import org.firstinspires.ftc.teamcode.util.AllianceColor;
import org.firstinspires.ftc.teamcode.util.Artifact;
import org.firstinspires.ftc.teamcode.util.MatchSettings;
import org.firstinspires.ftc.teamcode.util.Settings;

@TeleOp(name="Blue Far")
public class BlueFar extends OpMode {
    private Transfer transfer;
    private Shooter shooter;
    private Intake intake;
    private final AllianceColor alliance = new AllianceColor(AllianceColor.Selection.BLUE);
    private double shooterSpeed, tiltPos;
    private Pose gamepadReference;
    private boolean runIntake = false;

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
        shooterSpeed = 250.0;
        tiltPos = 0.0;

        gamepadReference = new Pose(
                -gamepad1.left_stick_y,
                -gamepad1.left_stick_x,
                -gamepad1.right_stick_x
        );
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
        dt.startTeleOpDrive();
        tiltPos = 0.0;
    }



    @Override
    public void loop() {
        dt.update();
        telemetry.update();
        telemetry.addData("Shooter vel", shooterSpeed);
        telemetry.addData("Distance to Target", shooter.flywheel.getDistance(dt.follower.getPose().getX(), dt.follower.getPose().getY(), alliance));
        shooter.runLoop(dt.getPose().getX(), dt.getPose().getY(), dt.getPose().getHeading());
        shooter.flywheel.update(shooterSpeed);
        shooter.tilt.setTilt(tiltPos);

        double forward =
                (-gamepad1.left_stick_y - gamepadReference.getY()) * 1.03;
        double strafe =
                (-gamepad1.left_stick_x - gamepadReference.getX()) * 1.03;
        double turn =
                (-gamepad1.right_stick_x - gamepadReference.getHeading()) * 1.03;

        dt.follower.setTeleOpDrive(
                forward,
                strafe,
                turn,
                true
        );

        if (gamepad1.aWasPressed()) {
            runIntake = !runIntake;

            if (runIntake) {
                intake.run();
            }
            else {
                intake.stop();
            }
        }

        if (gamepad1.bWasPressed()) {
            transfer.fireSortedArtifacts();
        }
        if (gamepad1.dpadUpWasPressed()) {
            transfer.cancelFire();
        }

        if (gamepad1.leftBumperWasPressed()) {
            shooterSpeed += 10.0;
        }
        if (gamepad1.dpadDownWasPressed()) {
            shooterSpeed -= 10.0;
        }

        if (gamepad1.dpadLeftWasPressed()) {
            shooterSpeed += 1.0;
        }
        if (gamepad1.dpadRightWasPressed()) {
            shooterSpeed -= 1.0;
        }

        if (gamepad1.xWasPressed()) {
            tiltPos += 0.03;
        }
        else if (gamepad1.yWasPressed()) {
            tiltPos -= 0.03;
        }
    }

    @Override
    public void stop() {
        transfer.cancelFire();  //  Close the thread when the OpMode is done
        shooter.flywheel.stop();

    }
}
