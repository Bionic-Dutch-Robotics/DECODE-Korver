package org.firstinspires.ftc.teamcode.tests;

import static org.firstinspires.ftc.teamcode.util.Hardware.dt;
import static org.firstinspires.ftc.teamcode.util.Hardware.intake;
import static org.firstinspires.ftc.teamcode.util.Hardware.shooter;
import static org.firstinspires.ftc.teamcode.util.Hardware.transfer;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.AllianceColor;
import org.firstinspires.ftc.teamcode.util.MatchSettings;

import java.util.function.BooleanSupplier;

@TeleOp(name="Controller Test")
public class ControllerTest extends OpMode {
    private final AllianceColor alliance = new AllianceColor(AllianceColor.Selection.BLUE);


    @Override
    public void init() {
        Scheduler.reset();
        MatchSettings.initSelection(hardwareMap, alliance, gamepad1);

        Scheduler.schedule(
                event(gamepad1::aWasPressed, () -> intake.toggle()),
                event(gamepad1::xWasPressed, () -> transfer.fireSortedArtifacts()),
                Command.build()
                        .setExecute(() -> shooter.flywheel.update(
                                shooter.flywheel.getRegressionVelocity(
                                        shooter.flywheel.getDistance(dt.follower.getPose().getX(), dt.follower.getPose().getY()))))
                        .setDone(() -> false)
        );
    }

    @Override
    public void start() {
        MatchSettings.start();
    }
    @Override
    public void loop() {
        Scheduler.execute();
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
