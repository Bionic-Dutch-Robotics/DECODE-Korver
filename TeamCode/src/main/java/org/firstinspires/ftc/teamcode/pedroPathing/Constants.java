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
    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(9.6)
            .forwardZeroPowerAcceleration(-24.947085808209017)
            .lateralZeroPowerAcceleration(-70.51911769287462)
            .translationalPIDFCoefficients(new com.pedropathing.control.PIDFCoefficients(
                    0.85,
                    0,
                    0.08308,
                    0.024
            ))
            .translationalPIDFSwitch(4)
            .drivePIDFCoefficients(new FilteredPIDFCoefficients(
                    .5,
                    0,
                    0.045,
                    0.6,
                    0.0
            ))
            .headingPIDFCoefficients(new PIDFCoefficients(
                    3.0,
                    0.00,
                    0.067,
                    0.02455
            ))
            .centripetalScaling(0.0005)
            .useSecondaryDrivePIDF(false)
            .useSecondaryHeadingPIDF(false)
            .useSecondaryTranslationalPIDF(false);

    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(1.0)
            .xVelocity(75.69462765671139)
            .yVelocity(57.2784505528728)
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
                .forwardPodY(-7.5590551181)
                .strafePodX(0.157480315);


    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, .75, 1);

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .pathConstraints(pathConstraints)
                .setDrivetrain(new Mecanum(hardwareMap, driveConstants))
                .pinpointLocalizer(localizerConstants)
                .build();
    }
}
