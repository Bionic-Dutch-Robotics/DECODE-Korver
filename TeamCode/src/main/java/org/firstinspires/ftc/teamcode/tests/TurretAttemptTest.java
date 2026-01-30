package org.firstinspires.ftc.teamcode.tests;

import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.control.PIDFController;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@TeleOp(name="Turret Attempt")
public class TurretAttemptTest extends OpMode {
    private DcMotorEx turret;
    private Follower follower;
    private double targetRad, targetTicks;
    private Pose currentPos;
    private PIDFController turretPid;
    @Override
    public void init() {
        turret = hardwareMap.get(DcMotorEx.class, "turret");
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(Constants.blueStartPose);
        turretPid = new PIDFController(new PIDFCoefficients(0.025, 0, 0, 0));
        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    @Override
    public void start() {
        follower.startTeleopDrive(false);
    }

    @Override
    public void loop() {
        follower.update();
        follower.setTeleOpDrive(
                -gamepad1.left_stick_y,
                -gamepad1.left_stick_x,
                -gamepad1.right_stick_x,
                true
        );
        telemetry.update();
        turretPid.updatePosition(turret.getCurrentPosition());
        currentPos = follower.getPose();
        targetRad = Math.atan2(144-currentPos.getY(), 0-currentPos.getX()) - currentPos.getHeading() + Math.PI/2;
        targetTicks = targetRad * 140.003629846;
        turretPid.setTargetPosition(targetTicks);

        turret.setPower(turretPid.run() / 2);

        telemetry.addData("Turret Pos", turret.getCurrentPosition());
        telemetry.addData("Turret Target", targetTicks);
    }
}
