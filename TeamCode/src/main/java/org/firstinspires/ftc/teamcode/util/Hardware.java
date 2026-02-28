package org.firstinspires.ftc.teamcode.util;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.LynxHubs;
import org.firstinspires.ftc.teamcode.subsystems.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.shooter.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.transfer.Transfer;

public class Hardware {
    public static Drivetrain dt;
    public static Transfer transfer;
    public static Shooter shooter;
    public static Intake intake;
    public static boolean hasBeenInitialized = false;
    public static LynxHubs hubs;
    public static void initialize(HardwareMap hwMap, AllianceColor alliance, Pose gamepadReference) {
        if (!hasBeenInitialized) {
            dt = new Drivetrain(hwMap, alliance, gamepadReference, new Pose(1.15, 1.15, 1.15));

            transfer = new Transfer(hwMap);

            shooter = new Shooter(hwMap);
            shooter.setAlliance(alliance);
            intake = new Intake(hwMap);
            hasBeenInitialized = true;
        }
        //hubs = new LynxHubs();
        //hubs.init(hwMap, alliance);
    }

    public static void loop() {
        dt.update();
        //hubs.loop();
    }

    public static void stop() {
        transfer.kicker.stop();
        //hubs.stop();
        intake.stop();

    }
}
