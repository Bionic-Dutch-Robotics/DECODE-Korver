package org.firstinspires.ftc.teamcode.subsystems;

import android.util.Size;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcontroller.external.samples.ConceptAprilTagMultiPortal;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.teamcode.util.Artifact;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

public class Vision {
    private AprilTagProcessor aprilTag1;
    private AprilTagProcessor aprilTag2;
    private VisionPortal visionPortal1, visionPortal2;

    public Vision (HardwareMap hwMap) {
        aprilTag1 = AprilTagProcessor.easyCreateWithDefaults();
        aprilTag2 = AprilTagProcessor.easyCreateWithDefaults();
        int[] viewIds = VisionPortal.makeMultiPortalView(2, VisionPortal.MultiPortalLayout.VERTICAL);
        visionPortal1 = new VisionPortal.Builder()
                .addProcessor(aprilTag1)
                .setCamera(hwMap.get(CameraName.class, "webcam1"))
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .setLiveViewContainerId(0)
                .setCameraResolution(new Size(640, 480))
                .build();

        visionPortal2 = new VisionPortal.Builder()
                .addProcessor(aprilTag2)
                .setCamera(hwMap.get(CameraName.class, "webcam2"))
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .setLiveViewContainerId(0)
                .setCameraResolution(new Size(1920, 1080))
                .build();
    }

    public Artifact[] findMotif(Telemetry tm) {
        List<AprilTagDetection> currentDetections = aprilTag2.getDetections();
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
    public Double findTurretErrorFromBlueGoal() {
        List<AprilTagDetection> currentDetections = aprilTag1.getFreshDetections();

        if (currentDetections != null) {
            for (AprilTagDetection detection : currentDetections) {
                if (detection.metadata != null && detection.id == 20) {
                    //return () -> (double) detection.ftcPose.bearing;
                    return -detection.ftcPose.yaw;
                }
            }
        }
        return 0.0;
    }


    public void stop() {
        visionPortal1.close();
        visionPortal2.close();
    }
}