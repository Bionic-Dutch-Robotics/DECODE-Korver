package org.firstinspires.ftc.teamcode.subsystems;

import android.util.Size;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Supplier;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.teamcode.util.Artifact;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

public class Vision {
    private AprilTagProcessor aprilTag;
    private VisionPortal visionPortal;

    public Vision (HardwareMap hwMap) {
        aprilTag = AprilTagProcessor.easyCreateWithDefaults();
        visionPortal = new VisionPortal.Builder()
                .addProcessor(aprilTag)
                .setCamera(hwMap.get(CameraName.class, "webcam1"))
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .enableLiveView(true)
                .setCameraResolution(new Size(640, 480))
                .build();
    }

    public Artifact[] findMotif(Telemetry tm) {
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata != null) {
                tm.addData("Tag ID: ", detection.id);
                if (detection.id == 21) {
                    return new Artifact[] {Artifact.GREEN, Artifact.PURPLE, Artifact.PURPLE};
                }
                else if (detection.id == 22) {
                    return new Artifact[] {Artifact.PURPLE, Artifact.GREEN, Artifact.PURPLE};
                }
                else if (detection.id == 23) {
                    return new Artifact[] {Artifact.PURPLE, Artifact.PURPLE, Artifact.GREEN};
                }
            }
        }
        return null;
    }

    /**
     * @return  Error from AprilTag in degrees. Positive is to the right, negative is to the left.
     *  Will return null if tag is not found
     */
    public Supplier<Double> findTurretErrorFromBlueGoal() {
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();

        if (currentDetections != null) {
            for (AprilTagDetection detection : currentDetections) {
                if (detection.metadata != null && detection.id == 20) {
                    //return () -> (double) detection.ftcPose.bearing;
                    return () -> detection.robotPose.getOrientation().getYaw();
                }
            }
        }
        return () -> (double) -99999;
    }


    public void stop() {
        visionPortal.close();
    }
}