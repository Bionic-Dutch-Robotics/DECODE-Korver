package org.firstinspires.ftc.teamcode.teleops.bindings;

import com.pedropathing.geometry.Pose;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.util.AllianceColor;
import org.firstinspires.ftc.teamcode.util.Settings;

import java.util.function.BooleanSupplier;

public class MainController {
    public static Command[] controls;
    private static boolean runToPos = false;
    private final Pose resetCornerPoseBlue = new Pose(144-17.75, 144-17.75, Math.PI);
    private final Pose resetCornerPoseRed = new Pose(17.75, 17.75, Math.PI);

    private Drivetrain dt;

    private Gamepad gamepad;

    public MainController(Drivetrain dt, Intake intake, Gamepad gamepad, AllianceColor alliance) {
        this.dt = dt;
        this.gamepad = gamepad;
        controls = new Command[] {
                event(gamepad::leftBumperWasPressed, intake::toggle),
                event(gamepad::rightBumperWasPressed, intake::eject),
                event(gamepad::yWasPressed,
                        () -> dt.follower.setPose(alliance.isRed() ? resetCornerPoseRed : resetCornerPoseBlue)),
                event(gamepad::aWasPressed,
                        () -> {
                            if (!runToPos) {
                                dt.lineToCloseShoot(dt.follower.getHeading());
                                runToPos = true;
                            }
                            else {
                                runToPos = false;
                                dt.follower.breakFollowing();
                                dt.follower.startTeleopDrive();
                            }
                        }),
                event(gamepad::xWasPressed,
                        () -> dt.lineToPose(
                                alliance.isRed() ? Settings.Positions.Drivetrain.Red.PARK : Settings.Positions.Drivetrain.Blue.PARK,
                                dt.getPose().getHeading())),
                event(gamepad::bWasPressed,
                        () -> {
                            if (!runToPos) {
                                dt.lineToFarShoot(dt.follower.getHeading());
                                runToPos = true;
                            } else {
                                runToPos = false;
                                dt.follower.breakFollowing();
                                dt.follower.startTeleopDrive();
                            }
                        })
        };
        Scheduler.schedule(controls);
    }

    public void runDrive() {
        if (!runToPos) {
            dt.teleOpDrive(
                    -gamepad.left_stick_y,
                    -gamepad.left_stick_x,
                    -gamepad.right_stick_x
            );
        }
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
