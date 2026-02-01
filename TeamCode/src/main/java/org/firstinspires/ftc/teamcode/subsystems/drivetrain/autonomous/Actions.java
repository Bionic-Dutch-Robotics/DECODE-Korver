package org.firstinspires.ftc.teamcode.subsystems.drivetrain.autonomous;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.util.AllianceColor;
import org.firstinspires.ftc.teamcode.util.AllianceColor.Selection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

@Configurable
public class Actions {
    public static final Pose redRow1Target = new Pose(112, 37, 0);
    public static final Pose redRow1Control = new Pose(76, 35);
    public static final Pose redRow2Control = new Pose(76,62);
    public static final Pose redRow2Target = new Pose(105,61, 0);
    public static final Pose redRow3Control = new Pose(76, 76);
    public static final Pose redRow3Target = new Pose(105,78, 0);
    public static final Pose redExitTriangle = new Pose(96, 72, Math.toRadians(0));
    public static final Pose blueExitTriangle = new Pose(40, 72, Math.toRadians(180));

    public static final Pose blueRow1Target = new Pose(28, 42, Math.toRadians(180));
    public static final Pose blueRow1Control = new Pose(68, 39);
    public static final Pose blueRow2Control = new Pose(64, 63);
    public static final Pose blueRow2Target = new Pose(33, 66, Math.toRadians(180));
    public static final Pose blueRow3Control = new Pose(68,78);
    public static final Pose blueRow3Target = new Pose(28, 86, Math.toRadians(180));
    public final Path shoot1, goToLever;

    private final AllianceColor allianceColor;
    public final PathChain redIntakeRow1, redIntakeRow2, redIntakeRow3;


    public Actions(Selection allianceColor) {
        this.allianceColor = new AllianceColor(allianceColor);

        shoot1 = new Path(new BezierLine(
                this.allianceColor.isRed() ? Constants.redStartPose : Constants.blueStartPose,
                this.allianceColor.isRed() ? Constants.farRedShoot : Constants.farBlueShoot
        ));

        shoot1.setLinearHeadingInterpolation(
                this.allianceColor.isRed() ? Constants.redStartPose.getHeading() : Constants.blueStartPose.getHeading(),
                this.allianceColor.isRed() ? Constants.farRedShoot.getHeading() : Constants.farBlueShoot.getHeading());

        goToLever = new Path(new BezierLine(
                this.allianceColor.isRed() ? Constants.farRedShoot : Constants.farBlueShoot,
                this.allianceColor.isRed() ? redExitTriangle : blueExitTriangle
        ));
        goToLever.setLinearHeadingInterpolation(
                this.allianceColor.isRed() ? Constants.farRedShoot.getHeading() : Constants.farBlueShoot.getHeading(),
                this.allianceColor.isRed() ? redExitTriangle.getHeading() : blueExitTriangle.getHeading()
        );


        HashMap<String, ArrayList<Path>> redIntakePaths = new HashMap<>();
        redIntakeRow1 = this.initIntake1(redIntakePaths);
        redIntakeRow2 = this.initIntake2(redIntakePaths);
        redIntakeRow3 = this.initIntake3(redIntakePaths);
    }

    public PathChain initIntake1(HashMap<String, ArrayList<Path>> redIntakePaths) {
        redIntakePaths.put("Intake1", new ArrayList<>());
        Objects.requireNonNull(redIntakePaths.get("Intake1")).add(
                new Path(new BezierCurve(
                        this.allianceColor.isRed() ? Constants.farRedShoot : Constants.farBlueShoot,
                        this.allianceColor.isRed() ? redRow1Control : blueRow1Control,
                        this.allianceColor.isRed() ? redRow1Target : blueRow1Target
                ))
        );

        Objects.requireNonNull(redIntakePaths.get("Intake1")).get(0).setLinearHeadingInterpolation(
                this.allianceColor.isRed() ? Constants.farRedShoot.getHeading() : Constants.farBlueShoot.getHeading(),
                this.allianceColor.isRed() ? redRow1Target.getHeading() : blueRow1Target.getHeading(),
                0.5
        );


        Objects.requireNonNull(redIntakePaths.get("Intake1")).add(
                Objects.requireNonNull(redIntakePaths.get("Intake1")).get(0).getReversed()
        );

        Objects.requireNonNull(redIntakePaths.get("Intake1")).get(1).setLinearHeadingInterpolation(
                this.allianceColor.isRed() ? redRow1Target.getHeading() : blueRow1Target.getHeading(),
                this.allianceColor.isRed() ? Constants.farRedShoot.getHeading() : Constants.farBlueShoot.getHeading(),
                0.8
        );

        return new PathChain(Objects.requireNonNull(redIntakePaths.get("Intake1")));
    }

    public PathChain initIntake2(HashMap<String, ArrayList<Path>> redIntakePaths) {
        redIntakePaths.put("Intake2", new ArrayList<>());
        Objects.requireNonNull(redIntakePaths.get("Intake2")).add(
                new Path(new BezierCurve(
                        this.allianceColor.isRed() ? Constants.farRedShoot : Constants.farBlueShoot,
                        this.allianceColor.isRed() ? redRow2Control : blueRow2Control,
                        this.allianceColor.isRed() ? redRow2Target : blueRow2Target
                ))
        );

        Objects.requireNonNull(redIntakePaths.get("Intake2")).get(0).setLinearHeadingInterpolation(
                this.allianceColor.isRed() ? Constants.farRedShoot.getHeading() : Constants.farBlueShoot.getHeading(),
                this.allianceColor.isRed() ? redRow2Target.getHeading() : blueRow2Target.getHeading(),
                0.5
        );


        Objects.requireNonNull(redIntakePaths.get("Intake2")).add(
                Objects.requireNonNull(redIntakePaths.get("Intake2")).get(0).getReversed()
        );
        Objects.requireNonNull(redIntakePaths.get("Intake2")).get(1).setLinearHeadingInterpolation(
                this.allianceColor.isRed() ? redRow2Target.getHeading() : blueRow2Target.getHeading(),
                this.allianceColor.isRed() ? Constants.farRedShoot.getHeading() : Constants.farBlueShoot.getHeading()
        );

        return new PathChain(Objects.requireNonNull(redIntakePaths.get("Intake2")));
    }

    public PathChain initIntake3(HashMap<String, ArrayList<Path>> redIntakePaths) {
        redIntakePaths.put("Intake3", new ArrayList<>());
        Objects.requireNonNull(redIntakePaths.get("Intake3")).add(
                new Path(new BezierCurve(
                        this.allianceColor.isRed() ? Constants.farRedShoot : Constants.farBlueShoot,
                        this.allianceColor.isRed() ? redRow3Control : blueRow3Control,
                        this.allianceColor.isRed() ? redRow3Target : blueRow3Target
                ))
        );

        Objects.requireNonNull(redIntakePaths.get("Intake3")).get(0).setLinearHeadingInterpolation(
                this.allianceColor.isRed() ? Constants.farRedShoot.getHeading() : Constants.farBlueShoot.getHeading(),
                this.allianceColor.isRed() ? redRow3Target.getHeading() : blueRow3Target.getHeading(),
                0.5
        );

        Objects.requireNonNull(redIntakePaths.get("Intake3")).add(
                Objects.requireNonNull(redIntakePaths.get("Intake3")).get(0).getReversed()
        );

        Objects.requireNonNull(redIntakePaths.get("Intake3")).get(1).setLinearHeadingInterpolation(
                this.allianceColor.isRed() ? redRow3Target.getHeading() : blueRow3Target.getHeading(),
                this.allianceColor.isRed() ? Constants.farRedShoot.getHeading() : Constants.farBlueShoot.getHeading(),
                0.8
        );

        return new PathChain(Objects.requireNonNull(redIntakePaths.get("Intake3")));
    }

    public Selection getAlliance() {
        return allianceColor.getSelection();
    }
}
