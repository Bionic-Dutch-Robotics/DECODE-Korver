package org.firstinspires.ftc.teamcode.teleops;

import static org.firstinspires.ftc.teamcode.util.Hardware.dt;
import static org.firstinspires.ftc.teamcode.util.Hardware.intake;
import static org.firstinspires.ftc.teamcode.util.Hardware.shooter;
import static org.firstinspires.ftc.teamcode.util.Hardware.transfer;

import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.AllianceColor;
import org.firstinspires.ftc.teamcode.util.Hardware;
import org.firstinspires.ftc.teamcode.util.MatchSettings;

import java.util.List;

@TeleOp(name="Blue")
public class BlueFar extends OpMode {
    private List<LynxModule> allHubs = null;

    @Override
    public void init() {
        allHubs = hardwareMap.getAll(LynxModule.class);
        MatchSettings.initSelection(hardwareMap, new AllianceColor(AllianceColor.Selection.BLUE), gamepad1);
        MatchSettings.start();
        dt.startTeleOpDrive();

        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }
    }

    @Override
    public void loop() {
        for (LynxModule hub : allHubs) {
            hub.getBulkData();
        }
        dt.update();
        intake.run();
        shooter.turret.loop(dt.getPose()
        );
        shooter.tilt.setTilt(shooter.tilt.auto(shooter.flywheel.getDistance(
                dt.getPose().getX(),
                dt.getPose().getY(),
                new AllianceColor(AllianceColor.Selection.BLUE)
        )));
        shooter.flywheel.adaptive(
                dt.follower.getPose().getX(),
                dt.follower.getPose().getY()
        );

        dt.teleOpDrive(
                -gamepad1.left_stick_y,
                -gamepad1.left_stick_x,
                -gamepad1.right_stick_x
        );

        if (gamepad1.aWasPressed()) {
            transfer.fireSortedArtifacts();
        }
        else if (gamepad1.bWasPressed()) {
            transfer.cancelFire();
        }

        if (gamepad1.startWasPressed()) {
            dt.follower.setPose(new Pose(144-17.75/2, 17.75/2, Math.PI));
            dt.follower.startTeleopDrive();
        }

        telemetry.addData("bot X", dt.getPose().getX());
        telemetry.addData("bot Y: ", dt.getPose().getY());
        telemetry.addData("bot Heading: ", dt.getPose().getHeading());
        telemetry.addData("Target Velocity: ", shooter.flywheel.getRegressionVelocity(
                shooter.flywheel.getDistance(
                        dt.getPose().getX(),
                        dt.getPose().getY(),
                        new AllianceColor(AllianceColor.Selection.BLUE)
                )
        ));
        telemetry.addData("Turret Target", shooter.turret.getTargetRadians(dt.getPose()));
        telemetry.addData("Turret Pos", shooter.turret.convertTicksToRadians(shooter.turret.turret.getCurrentPosition()));
        telemetry.addData("Encoder", shooter.turret.turret.getCurrentPosition());
        telemetry.update();
    }
}
