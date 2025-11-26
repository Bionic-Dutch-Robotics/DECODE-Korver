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
        public static final Pose blueStartPose = new Pose(69, 5.5, Math.PI / 2);
        public static final Pose redStartPose = new Pose(85, 5.5, Math.toRadians(90));
        public static final Pose farRedShoot = new Pose(87, 16, 2.7);
        public static final Pose farBlueShoot = new Pose(57, 20, -2.75);
        public static final Pose redCloseShoot = new Pose(72,72,Math.toRadians(135));
        public static final Pose blueCloseShoot = new Pose(72,72, Math.toRadians(-135));
        public static final double closeShootPower = 250;   //Targets 230, really reaches 140
        public static final double farShootPower = 300;     //Targets 295, really reaches 180
        public static PIDFCoefficients shooterCoefficients = new PIDFCoefficients(0.005, 0,0.000011,0);   //SHOOTER PIDF





    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(8)
            .forwardZeroPowerAcceleration(-41.36529811151215)
            .lateralZeroPowerAcceleration(-53.76135527463098)
            .translationalPIDFCoefficients(new com.pedropathing.control.PIDFCoefficients(
                    0.1,
                    0,
                    0.01,
                    0
            ))
            .translationalPIDFSwitch(4)
            .drivePIDFCoefficients(new FilteredPIDFCoefficients(
                    0.1,
                    0,
                    0.01,
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
            .xVelocity(61.099649264117865)
            .yVelocity(48.879322442482774)
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
                .forwardPodY(2)
                .strafePodX(2.4);


    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .pathConstraints(pathConstraints)
                .mecanumDrivetrain(driveConstants)
                .pinpointLocalizer(localizerConstants)
                .build();
    }
}
