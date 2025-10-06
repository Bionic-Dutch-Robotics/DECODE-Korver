package org.firstinspires.ftc.teamcode.tests;

import static java.lang.System.in;
import static java.util.EnumSet.range;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.firstinspires.ftc.teamcode.subsystems.Vision;
import org.opencv.core.Mat;
import org.opencv.core.Point;

@TeleOp (name="whatchamacallit")
public class visionTest extends OpMode {
    public Vision pipeline;
    public VisionPortal visionPortal;

    public Mat box;

    public Point[] points = new Point[4];

    public Point centerOfSample = new Point();

    @Override
    public void init() {
        pipeline = new Vision();

        visionPortal = VisionPortal.easyCreateWithDefaults(
                hardwareMap.get(CameraName.class, "webcam1"),
                pipeline
        );
    }

    @Override
    public void start() {
        visionPortal.stopStreaming();
    }

    @Override
    public void loop() {
        box = pipeline.getBox();

        for (int i = 0; i < 3; i++) {
            double[] ptData = box.get(i,0);
            points[i] = new Point(ptData[0], ptData[1]);
        }

        centerOfSample = new Point(points[0].x + (points[1].x - points[0].x), points[0].y + (points[2].y - points[0].y));

        telemetry.addData("Area: ", box.size().area());
        telemetry.addData("PositionX", centerOfSample.x);
        telemetry.addData("PositionY", centerOfSample.y);
    }
}