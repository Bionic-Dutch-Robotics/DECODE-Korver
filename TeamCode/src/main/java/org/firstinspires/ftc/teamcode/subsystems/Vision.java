package org.firstinspires.ftc.teamcode.subsystems;

import android.graphics.Canvas;

import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionProcessor;

import org.opencv.imgproc.Imgproc;
import org.opencv.core.Core;
import org.opencv.core.*;

import java.util.ArrayList;
import java.util.List;

//TODO: Merge Vision.java and Intake.java for intake automation
public class Vision implements VisionProcessor {

    public Scalar lowerRed1 = new Scalar(0, 100, 200);
    public Scalar upperRed1 = new Scalar(10, 255, 240);
    public Scalar lowerRed2 = new Scalar(165, 75, 150);
    public Scalar upperRed2 = new Scalar(179, 255, 240);

    public Mat binary = new Mat();
    public Mat redMask1;
    public Mat redMask2;
    public List<MatOfPoint> contours = new ArrayList<>();
    public Mat edge;

    public RotatedRect largestRect = new RotatedRect(new Point(0,0), new Size(0, 0), 0);

    public MatOfPoint2f contour2f;

    public Mat box;

    @Override
    public void init(int width, int height, CameraCalibration calibration) {

    }
    public Object processFrame(Mat frame, long captureTimeNanos) {
        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGB2HSV);

        Core.inRange(frame, lowerRed1, upperRed1, redMask1);
        Core.inRange(frame, lowerRed2, upperRed2, redMask2);

        Core.bitwise_and(redMask1, redMask2, redMask1);

        Imgproc.Canny(redMask1, edge, 100, 200, 3);
        Imgproc.findContours(edge, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        for (MatOfPoint contour : contours) {
            contour.convertTo(contour2f, CvType.CV_32FC2);

            RotatedRect rect = Imgproc.minAreaRect(contour2f);
            if (rect.size.width * rect.size.height > largestRect.size.width * largestRect.size.height) {
                largestRect = rect;
            }
        }
        Imgproc.boxPoints(largestRect, box);

        return box;

    }

    public Mat getBox() {
        return box;
    }

    @Override
    public void onDrawFrame(Canvas canvas, int screenWidth, int screenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {
        return;
    }
}