package org.firstinspires.ftc.teamcode.tests;

import android.util.Size;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;

import static org.firstinspires.ftc.teamcode.util.Hardware.dt;
import static org.firstinspires.ftc.teamcode.util.Hardware.shooter;
import org.firstinspires.ftc.teamcode.util.AllianceColor;
import org.firstinspires.ftc.teamcode.util.MatchSettings;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

@TeleOp(name="Fusion")
public class FusionTest extends OpMode {
    //private AprilTagProcessor aprilTag;
    private double lastDetectedOffset = 0.0;
    //private VisionPortal visionPortal;

    @Override
    public void init() {
        MatchSettings.initSelection(
                hardwareMap,
                new AllianceColor(AllianceColor.Selection.BLUE),
                gamepad1
        );
        MatchSettings.start();
        /*aprilTag = AprilTagProcessor.easyCreateWithDefaults();
        visionPortal = new VisionPortal.Builder()
                .addProcessor(aprilTag)
                .setCamera(hardwareMap.get(CameraName.class, "webcam1"))
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .enableLiveView(true)
                .setCameraResolution(new Size(640, 480))
                .build();*/

        telemetry.setMsTransmissionInterval(100);
        dt.startTeleOpDrive();
    }

    @Override
    public void loop() {
        telemetry.update();
        dt.update();
        dt.teleOpDrive(
                -gamepad1.left_stick_y,
                -gamepad1.left_stick_x,
                -gamepad1.right_stick_x
        );
        shooter.turret.loop(dt.getPose());
        lastDetectedOffset = -1*MatchSettings.findError();
        /*List<AprilTagDetection> tags = aprilTag.getFreshDetections();

        if (tags != null && !tags.isEmpty()) {
            for (AprilTagDetection tag : tags) {
                if (tag.metadata != null) {
                    if (tag.id == 20) {
                        telemetry.addData("Error", (Double) (-tag.ftcPose.yaw));
                        lastDetectedOffset = -tag.ftcPose.yaw;
                    }
                }
            }
        }*/

        telemetry.addData("Offset", lastDetectedOffset);
        if (gamepad1.aWasPressed()) {
            shooter.turret.setLiveOffset(Math.toRadians(lastDetectedOffset));
        }
    }
}
