package org.firstinspires.ftc.teamcode.tests;

import static org.firstinspires.ftc.teamcode.util.Hardware.dt;
import static org.firstinspires.ftc.teamcode.util.Hardware.intake;
import static org.firstinspires.ftc.teamcode.util.Hardware.shooter;
import static org.firstinspires.ftc.teamcode.util.Hardware.transfer;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.teamcode.util.AllianceColor;
import org.firstinspires.ftc.teamcode.util.Controller;
import org.firstinspires.ftc.teamcode.util.MatchSettings;

@TeleOp(name="Controller Test")
public class ControllerTest extends OpMode {
    private Controller controller1;
    private final AllianceColor alliance = new AllianceColor(AllianceColor.Selection.BLUE);


    @Override
    public void init() {
        MatchSettings.initSelection(hardwareMap, alliance, new Pose(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x));
        controller1 = new Controller();

        controller1.bind(
                () -> gamepad1.aWasPressed(),
                () -> intake.toggle()
        );

        controller1.bind(
                () -> gamepad1.xWasPressed(),
                () -> transfer.fireSortedArtifacts()
        );

        controller1.bind(
                () -> true,
                () -> shooter.flywheel.getRegressionVelocity(shooter.flywheel.getDistance(dt.follower.getPose().getX(), dt.follower.getPose().getY(), alliance), alliance),
                (vel) -> shooter.flywheel.update(vel)
        );
    }

    @Override
    public void start() {
        MatchSettings.start();
    }
    @Override
    public void loop() {
        controller1.update();
    }
}
