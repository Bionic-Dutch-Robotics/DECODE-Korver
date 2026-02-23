package org.firstinspires.ftc.teamcode.tests;

import android.util.Size;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.teamcode.subsystems.shooter.turret.Turret;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

@TeleOp(name="Fusion")
public class FusionTest extends OpMode {
    private AprilTagProcessor aprilTag;
    private double lastDetectedOffset = 0.0;
    private VisionPortal visionPortal;
    private Turret turret;

    @Override
    public void init() {
        turret = new Turret(hardwareMap);
        aprilTag = AprilTagProcessor.easyCreateWithDefaults();
        visionPortal = new VisionPortal.Builder()
                .addProcessor(aprilTag)
                .setCamera(hardwareMap.get(CameraName.class, "webcam1"))
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .enableLiveView(true)
                .setCameraResolution(new Size(640, 480))
                .build();

        telemetry.setMsTransmissionInterval(20);
    }

    @Override
    public void loop() {
        telemetry.update();
        turret.loop(new Pose(72,72, Math.PI));
        List<AprilTagDetection> tags = aprilTag.getFreshDetections();

        if (tags != null && !tags.isEmpty()) {
            for (AprilTagDetection tag : tags) {
                if (tag.metadata != null) {
                    if (tag.id == 20) {
                        telemetry.addData("Error", (Double) tag.ftcPose.yaw);
                        lastDetectedOffset = tag.ftcPose.yaw;
                    }
                }
            }
        }

        if (gamepad1.aWasPressed()) {
            turret.setLiveOffset(lastDetectedOffset);
        }
    }
}
