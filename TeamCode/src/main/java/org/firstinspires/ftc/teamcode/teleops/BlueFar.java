package org.firstinspires.ftc.teamcode.teleops;

import static org.firstinspires.ftc.teamcode.util.Hardware.dt;
import static org.firstinspires.ftc.teamcode.util.Hardware.intake;
import static org.firstinspires.ftc.teamcode.util.Hardware.shooter;
import static org.firstinspires.ftc.teamcode.util.Hardware.transfer;

import com.pedropathing.geometry.Pose;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.AllianceColor;
import org.firstinspires.ftc.teamcode.util.MatchSettings;

import java.util.List;
import java.util.function.BooleanSupplier;

@TeleOp(name="Blue")
public class BlueFar extends OpMode {
    private List<LynxModule> allHubs = null;
    private double voltageComp = 1.0;
    private double turretCorrection = 0.0;

    @Override
    public void init() {
        Scheduler.reset();
        allHubs = hardwareMap.getAll(LynxModule.class);
        MatchSettings.initSelection(hardwareMap, new AllianceColor(AllianceColor.Selection.BLUE), gamepad1);
        MatchSettings.start();
        dt.startTeleOpDrive();

        for (LynxModule hub : allHubs) {
            //hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }

        Scheduler.schedule(
                event(gamepad1::aWasPressed, () -> transfer.fireSortedArtifacts()),
                event(gamepad1::startWasPressed,
                        () -> {
                            dt.follower.setPose(new Pose(144-17.75/2, 17.75/2, Math.PI));
                            dt.follower.startTeleopDrive();
                        }),
                event(gamepad1::dpadUpWasPressed, intake::toggle),
                event(gamepad1::leftBumperWasPressed, () -> voltageComp += 0.02),
                event(gamepad1::rightBumperWasPressed, () -> voltageComp -= 0.02),
                event(gamepad1::leftTriggerWasPressed, () -> shooter.turret.setLiveOffset(0.05)),
                event(gamepad1::rightTriggerWasPressed, () -> shooter.turret.setLiveOffset(-0.05))
        );
    }

    @Override
    public void loop() {
        for (LynxModule hub : allHubs) {
            //hub.getBulkData();
        }

        Scheduler.execute();
        /*if (gamepad1.rightBumperWasPressed()) {
            voltageComp -= 0.02;
        }
        else if (gamepad1.leftBumperWasPressed()) {
            voltageComp += 0.02;
        }

        if (gamepad1.dpadUpWasPressed()) {
            intake.toggle();
        }

        if (gamepad1.aWasPressed()) {
            transfer.fireSortedArtifacts();
        }*/
        dt.update();
        //turretCorrection = MatchSettings.findError();
        shooter.turret.loop(dt.getPose()
        );
        shooter.tilt.setTilt(shooter.tilt.auto(shooter.flywheel.getDistance(
                dt.getPose().getX(),
                dt.getPose().getY()
        )));

        shooter.flywheel.setVoltageComp(voltageComp);
        shooter.flywheel.adaptive(
                dt.follower.getPose().getX(),
                dt.follower.getPose().getY()
        );

        dt.teleOpDrive(
                -gamepad1.left_stick_y,
                -gamepad1.left_stick_x,
                -gamepad1.right_stick_x
        );

        telemetry.addData("Turret Offset", (Double) turretCorrection);
        telemetry.addData("bot X", dt.getPose().getX());
        telemetry.addData("bot Y: ", dt.getPose().getY());
        telemetry.addData("bot Heading: ", dt.getPose().getHeading());
        telemetry.addData("Target Velocity: ", shooter.flywheel.getRegressionVelocity(
                shooter.flywheel.getDistance(
                        dt.getPose().getX(),
                        dt.getPose().getY()
                )
        ));
        telemetry.addData("Turret Target", shooter.turret.getTargetRadians(dt.getPose()));
        telemetry.addData("Turret Pos", shooter.turret.convertTicksToRadians(shooter.turret.turret.getCurrentPosition()));
        telemetry.addData("Encoder", shooter.turret.turret.getCurrentPosition());
        telemetry.update();
    }

    private static Command event(BooleanSupplier condition, Runnable action) {
        return Command.build()
                .setExecute(() -> {
                    if (condition.getAsBoolean()) {
                        action.run();
                    }
                })
                .setDone(() -> false);
    }
}
