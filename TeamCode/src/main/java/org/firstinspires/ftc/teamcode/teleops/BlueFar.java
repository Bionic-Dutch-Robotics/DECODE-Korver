package org.firstinspires.ftc.teamcode.teleops;

import static org.firstinspires.ftc.teamcode.util.Hardware.dt;
import static org.firstinspires.ftc.teamcode.util.Hardware.intake;
import static org.firstinspires.ftc.teamcode.util.Hardware.shooter;
import static org.firstinspires.ftc.teamcode.util.Hardware.transfer;
import static org.firstinspires.ftc.teamcode.util.MatchSettings.motif;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.AllianceColor;
import org.firstinspires.ftc.teamcode.util.Artifact;
import org.firstinspires.ftc.teamcode.util.MatchSettings;

@TeleOp(name="Blue Far")
public class BlueFar extends OpMode {
    private final AllianceColor alliance = new AllianceColor(AllianceColor.Selection.BLUE);
    private double shooterSpeed, tiltPos;
    private boolean runIntake = false;

    @Override
    public void init() {
        /*
        Normally all MatchSettings configuration would be done in AUTO.
        This is a TEST only.
         */

        MatchSettings.initSelection(
                hardwareMap,
                alliance,
                gamepad1
        );

        shooterSpeed = 250.0;
        tiltPos = 0.0;
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
        telemetry.addData("X", gamepad1.left_stick_x);
        shooter.tilt.auto(shooter.flywheel.getDistance(dt.follower.getPose().getX(), dt.follower.getPose().getY(), alliance));
        shooterSpeed = shooter.flywheel.getRegressionVelocity(shooter.flywheel.getDistance(dt.follower.getPose().getX(), dt.follower.getPose().getY(), alliance), alliance);
        telemetry.update();
        telemetry.addData("Shooter vel", shooterSpeed);
        telemetry.addData("Distance to Target", shooter.flywheel.getDistance(dt.follower.getPose().getX(), dt.follower.getPose().getY(), alliance));
        telemetry.addData("Tilt", tiltPos);
        shooter.runLoop(dt.getPose().getX(), dt.getPose().getY(), dt.getPose().getHeading());
        shooter.flywheel.update(shooterSpeed);
        shooter.tilt.setTilt(tiltPos);

        double forward = -gamepad1.left_stick_y;
        double strafe = -gamepad1.left_stick_x;
        double turn = -gamepad1.right_stick_x;

        dt.teleOpDrive(
                forward,
                strafe,
                turn
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
            //transfer.s
            shooterSpeed = shooter.flywheel.getRegressionVelocity(shooter.flywheel.getDistance(dt.follower.getPose().getX(), dt.follower.getPose().getY(), alliance), alliance);
            transfer.fireSortedArtifacts();
            tiltPos = shooter.tilt.auto(shooter.flywheel.getDistance(dt.follower.getPose().getX(), dt.follower.getPose().getY(), alliance));
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

        if (gamepad1.rightBumperWasPressed()) {
            tiltPos = shooter.tilt.auto(shooter.flywheel.getDistance(dt.follower.getPose().getX(), dt.follower.getPose().getY(), alliance));
        }
    }   //430 power , 142 dist, 0.14
        //340 power, 100 dist, 0.15 tilt
        //240 power, 40 dist, 0.09

    @Override
    public void stop() {
        transfer.cancelFire();  //  Close the thread when the OpMode is done
        shooter.flywheel.stop();
        intake.stop();
    }
}
