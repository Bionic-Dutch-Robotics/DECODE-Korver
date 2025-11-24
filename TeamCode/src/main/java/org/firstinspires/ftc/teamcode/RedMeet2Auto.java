package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Transfer;

import java.util.HashMap;

@Autonomous(name="TestAUTOred")
public class RedMeet2Auto extends LinearOpMode {
    private Shooter shooter;
    private Transfer transfer;
    private Follower fw;
    private Intake intake;
    private HashMap<String, Path> paths;

    @Override
    public void runOpMode() {

        shooter = new Shooter(hardwareMap, Constants.shooterCoefficients);
        transfer = new Transfer(hardwareMap);
        fw = Constants.createFollower(hardwareMap);
        fw.setStartingPose(Constants.redStartPose);
        intake = new Intake(hardwareMap);

        paths = new HashMap<>();
        paths.put("toFarShoot", new Path(new BezierLine(Constants.redStartPose, Constants.farRedShoot)));

        waitForStart();

        //      GO TO
        fw.update();
        fw.followPath(paths.get("toFarShoot"));

        while(fw.isBusy()) {
            fw.update();
            fw.followPath(paths.get("toFarShoot"));
            if (time > 5) {
                break;
            }
        }

        shooter.farShoot();

        //  CYCLE 1
        try {
            wait(5000);
        }   catch (Exception ignored){}

        intake.intake();
        transfer.reload();

        try {
            wait(1500);
        }   catch (Exception ignored){}

        transfer.feed();

        //  CYCLE 2
        try {
            wait(5000);
        }   catch (Exception ignored){}

        intake.intake();
        transfer.reload();

        try {
            wait(1000);
        }   catch (Exception ignored){}

        transfer.feed();

        //  CYCLE 3
        try {
            wait(5000);
        }   catch (Exception ignored){}

        intake.intake();
        transfer.reload();

        try {
            wait(1000);
        }   catch (Exception ignored){}

        transfer.feed();
    }
}
