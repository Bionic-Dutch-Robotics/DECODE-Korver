package org.firstinspires.ftc.teamcode.tests;

import static org.firstinspires.ftc.teamcode.util.Hardware.intake;
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


    @Override
    public void init() {
        MatchSettings.initSelection(hardwareMap, new AllianceColor(AllianceColor.Selection.BLUE), new Pose(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x));
        controller1 = new Controller();

        controller1.bindAction(
                () -> gamepad1.aWasPressed(),
                null,
                T -> intake.toggle()
        );

        controller1.bindAction(
                () -> gamepad1.xWasPressed(),
                null,
                T -> transfer.fireSortedArtifacts()
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
