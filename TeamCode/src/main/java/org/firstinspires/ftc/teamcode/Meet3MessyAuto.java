package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Transfer;
import org.firstinspires.ftc.teamcode.subsystems.drivetrain.autonomous.Actions;
import org.firstinspires.ftc.teamcode.util.AllianceColor;

import java.util.concurrent.TimeUnit;

@Autonomous(name="Meet 3 Red TeST")
public class Meet3MessyAuto extends OpMode {
    public static final Actions paths = new Actions(AllianceColor.Selection.RED);
    private static Follower follower;
    private static Shooter shooter;
    private static Transfer transfer;
    private static Intake intake;
    public static TestAuto.AutoState autoState;
    public static double savedTime;
    public static ElapsedTime time;
    public static int intakeIndex;
    @Override
    public void init() {
        autoState = TestAuto.AutoState.SHOOT;
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(
                Constants.redStartPose
        );
        shooter = new Shooter(hardwareMap, Constants.shooterCoefficients);
        transfer = new Transfer(hardwareMap);
        intake = new Intake(hardwareMap);
        savedTime = 0.0;
        intakeIndex = 0;
        time = new ElapsedTime();
    }

    @Override
    public void loop() {
        follower.update();
        if (intakeIndex == 0) {
            savedTime = time.time(TimeUnit.SECONDS);
            follower.followPath(paths.shoot1);

            if (!follower.isBusy()) {
                intakeIndex++;
            }
        }
        if (intakeIndex == 1) {
            savedTime = time.time(TimeUnit.SECONDS);
            follower.followPath(paths.redIntakeRow1);

            if (follower.isBusy()) {
                intakeIndex++;
            }
        }
        if (intakeIndex == 2) {
            follower.followPath(paths.redIntakeRow2);
        }
    }
}
