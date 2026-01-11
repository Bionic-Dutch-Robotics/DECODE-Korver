package org.firstinspires.ftc.teamcode.pedroPathing;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.control.FilteredPIDFCoefficients;
import com.pedropathing.control.*;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Configurable
public class Constants {
        public static final Pose blueCloseStart = new Pose(30.9436, 123.8405, -2.27);
        public static final Pose redCloseStart = new Pose(121.6233, 131.271, 2.2);
        public static final Pose blueStartPose = new Pose(64, 4, Math.toRadians(90));
        public static final Pose redStartPose = new Pose(80, 4, Math.toRadians(90));
        public static final Pose farRedShoot = new Pose(86, 16, 2.74);
        public static final Pose farBlueShoot = new Pose(64, 20, -2.73);
        public static final Pose redCloseShoot = new Pose(76,76,Math.toRadians(135));
        public static final Pose blueCloseShoot = new Pose(68,72, Math.toRadians(-135));
        public static final double closeShootPower = 210;   //Targets 230, really reaches 140
        public static final double farShootPower = 238;     //Targets 295, really reaches 180
        public static final Pose redPark = new Pose(35,30, 0);
        public static final Pose bluePark = new Pose(100,30, 0);

        public static PIDFCoefficients shooterCoefficients = new PIDFCoefficients(0.00527, 0,0.000013,0);   //SHOOTER PIDF. original P was 0.0052
        public static Pose teleOpStartPose = null;
        public static Follower follower = null;





    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(8)
            .forwardZeroPowerAcceleration(-34.53919833757035)
            .lateralZeroPowerAcceleration(-60.56271797569653)
            .translationalPIDFCoefficients(new com.pedropathing.control.PIDFCoefficients(
                    0.115,
                    0,
                    0.01,
                    0.02
            ))
            .translationalPIDFSwitch(4)
            .drivePIDFCoefficients(new FilteredPIDFCoefficients(
                    0.07,
                    0,
                    0.02,
                    0,
                    0
            ))
            .secondaryTranslationalPIDFCoefficients(new PIDFCoefficients(
                    0.4,
                    0,
                    0.005,
                    0.0006
            ))
            .headingPIDFCoefficients(new PIDFCoefficients(
                    1.5,
                    0.001,
                    0.08,
                    0.0
            ))
            .secondaryHeadingPIDFCoefficients(new PIDFCoefficients(
                    2.5,
                    0,
                    0.1,
                    0.0005
            ))
            .drivePIDFCoefficients(new FilteredPIDFCoefficients(
                    0.3,
                    0,
                    0.00035,
                    0.6,
                    0.015
            ))
            .secondaryDrivePIDFCoefficients(new FilteredPIDFCoefficients(
                    0.02,
                    0,
                    0.000005,
                    0.6,
                    0.01
            ))
            .drivePIDFSwitch(15)
            .centripetalScaling(0.0005)
            .useSecondaryDrivePIDF(false)
            .useSecondaryHeadingPIDF(false)
            .useSecondaryTranslationalPIDF(false);


    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(1)
            .xVelocity(57.58679439514641)
            .yVelocity(45.69038835660679)
            .leftFrontMotorName("frontLeft")
            .leftRearMotorName("backLeft")
            .rightFrontMotorName("frontRight")
            .rightRearMotorName("backRight")
            .leftFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
            .leftRearMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .rightRearMotorDirection(DcMotorSimple.Direction.REVERSE);

    public static PinpointConstants localizerConstants = new PinpointConstants()
                .distanceUnit(DistanceUnit.INCH)
                .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
                .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD)
                .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED)
                .forwardPodY(0.157480315)
                .strafePodX(7.5590551181);


    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1.4, 1);

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .pathConstraints(pathConstraints)
                .mecanumDrivetrain(driveConstants)
                .pinpointLocalizer(localizerConstants)
                .build();
    }
}
