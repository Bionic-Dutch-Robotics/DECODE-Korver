package org.firstinspires.ftc.teamcode.tests;

import static org.firstinspires.ftc.teamcode.util.Hardware.dt;
import static org.firstinspires.ftc.teamcode.util.Hardware.shooter;

import com.pedropathing.geometry.Pose;
import com.pedropathing.math.Vector;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.AllianceColor;
import org.firstinspires.ftc.teamcode.util.MatchSettings;
import org.firstinspires.ftc.teamcode.util.Settings;

@TeleOp(name="TURRET + DT")
public class TurretAndDrivetrain extends OpMode {

    @Override
    public void init() {
        MatchSettings.initSelection(hardwareMap, new AllianceColor(AllianceColor.Selection.BLUE), gamepad1);


        dt.startTeleOpDrive();
        shooter.turret.setAlliance(new AllianceColor(AllianceColor.Selection.BLUE));
    }

    @Override
    public void loop() {
        dt.update();
        dt.teleOpDrive(
                -gamepad1.left_stick_y,
                -gamepad1.left_stick_x,
                -gamepad1.right_stick_x
        );

        Vector velocity = dt.follower.getVelocity();
        double headingVel = dt.follower.getAngularVelocity();

        Pose predictedPose = dt.getPose().copy();
        if (velocity.getMagnitude() > 2.5) {
            predictedPose = dt.getPose().plus(
                    new Pose(
                            velocity.getXComponent(),
                            velocity.getYComponent(),
                            headingVel
                    ).times(Settings.Positions.Transfer.RUN_TO_POS_TIME /*0.05*/)
            );
        }
        shooter.turret.loop(
                dt.getPose()
        );
        //shooter.turret.loop(new Pose(72,72,Math.PI));

        telemetry.update();
        telemetry.addData("Turret", shooter.turret.turret.getCurrentPosition()/140.003629846);
        telemetry.addData("TurretTarget", shooter.turret.targetRad);
        telemetry.addData("Turret Raw", shooter.turret.turret.getCurrentPosition());
        telemetry.addData("X", dt.getPose().getX());
        telemetry.addData("Y", dt.getPose().getY());
        telemetry.addData("Heading", dt.getPose().getHeading());
    }
}
