package org.firstinspires.ftc.teamcode.tests;

import static org.firstinspires.ftc.teamcode.util.Hardware.intake;
import static org.firstinspires.ftc.teamcode.util.Hardware.transfer;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.AllianceColor;
import org.firstinspires.ftc.teamcode.util.Controller;
import org.firstinspires.ftc.teamcode.util.MatchSettings;

@TeleOp(name="Controller Test")
public class ControllerTest extends OpMode {
    private Controller controller1;


    @Override
    public void init() {
        MatchSettings.initSelection(hardwareMap, new AllianceColor(AllianceColor.Selection.BLUE));
        controller1 = new Controller(gamepad1);
    }

    @Override
    public void start() {
        MatchSettings.start();

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
    public void loop() {
        controller1.update();
    }
}
