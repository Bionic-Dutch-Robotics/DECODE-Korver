package org.firstinspires.ftc.teamcode.tests;

import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Subsystem;
import org.firstinspires.ftc.teamcode.subsystems.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.util.AllianceColor;
import org.firstinspires.ftc.teamcode.util.AugmentedOpMode;
import org.firstinspires.ftc.teamcode.util.Command;
import org.firstinspires.ftc.teamcode.util.Controller;

public class AugmentedOpModeTest extends AugmentedOpMode {
    private Drivetrain dt;
    private Intake intake;

    @Override
    public void init() {
        dt.setInitCommands(
                new Command[] {
                        new Command(
                                () -> dt.setReferences(
                                        new Pose(
                                                -gamepad1.left_stick_y,
                                                -gamepad1.left_stick_x,
                                                -gamepad1.right_stick_x
                                        ),
                                        new Pose(1.15, 1.15, 1.15)
                                )
                        )
                }
        );
        this.registerSubsystems(
                new Subsystem[]{
                        dt,
                        intake
                }
        );

        this.initialize(
                new AllianceColor(AllianceColor.Selection.BLUE)
        );
    }

    @Override
    public void loop() {
        this.update();
    }

    @Override
    public void stop() {
        this.kill();
    }
}
