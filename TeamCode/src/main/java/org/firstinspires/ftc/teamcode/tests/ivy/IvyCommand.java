package org.firstinspires.ftc.teamcode.tests.ivy;

import static org.firstinspires.ftc.teamcode.util.Hardware.dt;
import static org.firstinspires.ftc.teamcode.util.Hardware.shooter;
import static org.firstinspires.ftc.teamcode.util.Hardware.transfer;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.util.AllianceColor;
import org.firstinspires.ftc.teamcode.util.MatchSettings;

public class IvyCommand extends OpMode {
    private Command update;

    @Override
    public void init() {
        MatchSettings.initSelection(hardwareMap, new AllianceColor(AllianceColor.Selection.BLUE), gamepad1);

        update = Command.build()
                .setStart(MatchSettings::start)
                .setExecute(() -> {
                    dt.update();
                    shooter.runLoop(dt.getPose(),
                            dt.follower.getVelocity(),
                            dt.follower.getAngularVelocity());
                })
                .setEnd((endCondition) -> transfer.kicker.stop())
                .requiring(dt, shooter);

        Scheduler.schedule(update);
    }

    @Override
    public void loop() {
        Scheduler.execute();
    }
}
