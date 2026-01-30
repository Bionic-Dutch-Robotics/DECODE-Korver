package org.firstinspires.ftc.teamcode.pedroPathing;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.control.FilteredPIDFCoefficients;
import com.pedropathing.control.*;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.Mecanum;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.util.Settings;

@Configurable
public class Constants {
        public static final Pose blueStartPose = new Pose(56.875, 8.5, Math.PI);
        public static final Pose redStartPose = new Pose(80, 8, Math.toRadians(90));
        public static final Pose farRedShoot = new Pose(86, 16, 2.78);
        public static final Pose farBlueShoot = new Pose(64, 30, -2.7);
        public static final Pose redCloseShoot = new Pose(76,76,Math.toRadians(135));
        public static final Pose blueCloseShoot = new Pose(68,72, Math.toRadians(-135));
        public static final double closeShootPower = 210;   //Targets 230, really reaches 140
        public static final double farShootPower = 248;     //Targets 295, really reaches 180
        public static final Pose redPark = new Pose(35,30, 0);
        public static final Pose bluePark = new Pose(100,30, 0);

        public static PIDFCoefficients shooterCoefficients = new PIDFCoefficients(0.00527, 0,0.000013,0);





    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(9.6)
            .forwardZeroPowerAcceleration(-39.082554597030864)
            .lateralZeroPowerAcceleration(-71.21712910009308)
            .translationalPIDFCoefficients(new com.pedropathing.control.PIDFCoefficients(
                    0.3,
                    0,
                    0.025,
                    0.024
            ))
            .headingPIDFCoefficients(new PIDFCoefficients(
                    1.78,
                    0.00,
                    0.055,
                    0.025
            ))
            .drivePIDFCoefficients(new FilteredPIDFCoefficients(
                    0.75,
                    0,
                    0.005,
                    0.6,
                    0.025
            ))
            .centripetalScaling(0.0005)
            .useSecondaryDrivePIDF(false)
            .useSecondaryHeadingPIDF(false)
            .useSecondaryTranslationalPIDF(false);

    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(1.0)
            .xVelocity(58.94098332923229)
            .yVelocity(45.84543639656127)
            .leftFrontMotorName(Settings.HardwareNames.Drivetrain.FRONT_LEFT_DRIVE)
            .leftRearMotorName(Settings.HardwareNames.Drivetrain.BACK_LEFT_DRIVE)
            .rightFrontMotorName(Settings.HardwareNames.Drivetrain.FRONT_RIGHT_DRIVE)
            .rightRearMotorName(Settings.HardwareNames.Drivetrain.BACK_RIGHT_DRIVE)
            .leftFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .leftRearMotorDirection(DcMotorSimple.Direction.REVERSE)
            .rightFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .rightRearMotorDirection(DcMotorSimple.Direction.FORWARD);

    public static PinpointConstants localizerConstants = new PinpointConstants()
                .distanceUnit(DistanceUnit.INCH)
                .hardwareMapName(Settings.HardwareNames.Drivetrain.PINPOINT_NAME)
                .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
                .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD)
                .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD)
                .forwardPodY(0.157480315)
                .strafePodX(7.5590551181);


    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .pathConstraints(pathConstraints)
                .setDrivetrain(new Mecanum(hardwareMap, driveConstants))
                .pinpointLocalizer(localizerConstants)
                .build();
    }
}
