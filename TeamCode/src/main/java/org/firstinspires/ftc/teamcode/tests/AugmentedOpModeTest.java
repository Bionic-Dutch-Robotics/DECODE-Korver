package org.firstinspires.ftc.teamcode.tests;

import com.pedropathing.geometry.Pose;
import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.AugmentedOpMode;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.LynxHubs;
import org.firstinspires.ftc.teamcode.subsystems.Subsystem;
import org.firstinspires.ftc.teamcode.subsystems.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.util.AllianceColor;

@TeleOp(name="Augmented OpMode Test", group="tests")
public class AugmentedOpModeTest extends AugmentedOpMode {
    private Drivetrain dt;
    private Intake intake;
    private LynxHubs hubs;

    @Override
    public AllianceColor initialize() {
        dt.setInitCommands(
                    new Command[]{}
                );
        return new AllianceColor(AllianceColor.Selection.BLUE);
    }

    @Override
    public void initLoop() {

    }

    @Override
    public void onStart() {
        dt.startTeleOpDrive();
    }

    @Override
    public void onLoop() {
        dt.teleOpDrive(
                -gamepad1.left_stick_y,
                -gamepad1.left_stick_x,
                -gamepad1.right_stick_x
        );
        intake.run();
    }

    @Override
    public void onStop() {

    }

    @Override
    public Subsystem[] getSubsystems() {
        return new Subsystem[] {dt, intake, hubs};
    }
}
