package org.firstinspires.ftc.teamcode.tests;

import static org.firstinspires.ftc.teamcode.util.Hardware.dt;
import static org.firstinspires.ftc.teamcode.util.Hardware.intake;
import static org.firstinspires.ftc.teamcode.util.Hardware.shooter;
import static org.firstinspires.ftc.teamcode.util.Hardware.transfer;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.AllianceColor;
import org.firstinspires.ftc.teamcode.util.Artifact;
import org.firstinspires.ftc.teamcode.util.MatchSettings;

import java.util.List;

@TeleOp(name="Sort")
public class SortTest extends OpMode {
    private List<LynxModule> allHubs = null;

    @Override
    public void init() {
        allHubs = hardwareMap.getAll(LynxModule.class);
        MatchSettings.initSelection(hardwareMap, new AllianceColor(AllianceColor.Selection.BLUE), gamepad1);
        MatchSettings.start();
        dt.startTeleOpDrive();

        for (LynxModule module : allHubs) {
            module.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }
    }

    @Override
    public void loop() {
        for (LynxModule module : allHubs) {
            module.getBulkData();
        }

        dt.update();
        intake.run();
        shooter.turret.loop(
                dt.follower.getPose().getX(),
                dt.follower.getPose().getY(),
                dt.follower.getHeading()
        );
        shooter.flywheel.adaptive(
                dt.follower.getPose().getX(),
                dt.follower.getPose().getY(),
                new AllianceColor(AllianceColor.Selection.BLUE)
        );

        dt.teleOpDrive(
                -gamepad1.left_stick_y,
                -gamepad1.left_stick_x,
                -gamepad1.right_stick_x
        );
        if (gamepad1.aWasPressed()) {
            transfer.kicker.setFireSequence(
                    new Integer[] {
                            2, 1, 0
                    }
            );
            transfer.kicker.createFireSequence();
            //transfer.fireSortedArtifacts();

        }
    }
}
