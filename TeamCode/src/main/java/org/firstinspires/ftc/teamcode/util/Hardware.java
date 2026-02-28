package org.firstinspires.ftc.teamcode.util;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.LynxHubs;
import org.firstinspires.ftc.teamcode.subsystems.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.shooter.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.transfer.Transfer;

public class Hardware {
    public static Drivetrain dt = null;
    public static Transfer transfer = null;
    public static Shooter shooter = null;
    public static Intake intake = null;
    public static boolean hasBeenInitialized = false;
    public static void initialize(HardwareMap hwMap, AllianceColor alliance, Pose gamepadReference) {
        dt = new Drivetrain(
                hwMap, alliance,
                gamepadReference,
                new Pose(1.15, 1.15, 1.15),
                hasBeenInitialized
        );

        transfer = new Transfer(hwMap);

        shooter = new Shooter(hwMap);
        shooter.setAlliance(alliance);
        intake = new Intake(hwMap);

        if (!hasBeenInitialized) hasBeenInitialized = true;
    }

    public static void loop() {
        dt.update();
        //hubs.loop();
    }

    public static void stop() {
        dt.stop();
        transfer.kicker.stop();
        shooter.flywheel.stop();
        shooter.turret.stop();
        //hubs.stop();
        intake.stop();

    }
}
