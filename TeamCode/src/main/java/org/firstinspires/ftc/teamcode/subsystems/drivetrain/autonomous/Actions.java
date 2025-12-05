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
    public static final Pose redRow1Target = new Pose(112, 33, 0);
    public static final Pose redRow1Control = new Pose(70, 31);
    public static final Pose redRow2Control = new Pose(70,56.5);
    public static final Pose redRow2Target = new Pose(110,55, 0);
    public static final Pose redRow3Control = new Pose(70, 76);
    public static final Pose redRow3Target = new Pose(105,80, 0);
    public static final Pose redExitTriangle = new Pose(96, 72, Math.toRadians(90));
    public final Path shoot1, goToLever;

    private final AllianceColor allianceColor;
    public final PathChain redIntakeRow1, redIntakeRow2, redIntakeRow3;


    public Actions(Selection allianceColor) {
        this.allianceColor = new AllianceColor(allianceColor);

        shoot1 = new Path(new BezierLine(
                this.allianceColor.isRed() ? Constants.redStartPose : Constants.redStartPose.mirror(),
                this.allianceColor.isRed() ? Constants.farRedShoot : Constants.farRedShoot.mirror()
        ));

        shoot1.setLinearHeadingInterpolation(
                this.allianceColor.isRed() ? Constants.redStartPose.getHeading() : Constants.redStartPose.mirror().getHeading(),
                this.allianceColor.isRed() ? Constants.farRedShoot.getHeading() : Constants.farRedShoot.mirror().getHeading());

        goToLever = new Path(new BezierLine(
                this.allianceColor.isRed() ? Constants.farRedShoot : Constants.farRedShoot.mirror(),
                redExitTriangle
        ));
        goToLever.setLinearHeadingInterpolation(
                this.allianceColor.isRed() ? Constants.farRedShoot.getHeading() : Constants.farRedShoot.mirror().getHeading(),
                this.allianceColor.isRed() ? redExitTriangle.getHeading() : redExitTriangle.mirror().getHeading()
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
                        this.allianceColor.isRed() ? redRow1Control : redRow1Control.mirror(),
                        this.allianceColor.isRed() ? redRow1Target : redRow1Target.mirror())
                )
        );

        Objects.requireNonNull(redIntakePaths.get("Intake1")).get(0).setLinearHeadingInterpolation(
                this.allianceColor.isRed() ? Constants.farRedShoot.getHeading() : Constants.farBlueShoot.getHeading(),
                this.allianceColor.isRed() ? redRow1Target.getHeading() : redRow1Target.mirror().getHeading(),
                0.5
        );


        Objects.requireNonNull(redIntakePaths.get("Intake1")).add(
                Objects.requireNonNull(redIntakePaths.get("Intake1")).get(0).getReversed()
        );

        Objects.requireNonNull(redIntakePaths.get("Intake1")).get(1).setLinearHeadingInterpolation(
                this.allianceColor.isRed() ? redRow1Target.getHeading() : redRow1Target.mirror().getHeading(),
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
                        this.allianceColor.isRed() ? redRow2Control : redRow2Control.mirror(),
                        this.allianceColor.isRed() ? redRow2Target : redRow2Target.mirror()
                ))
        );

        Objects.requireNonNull(redIntakePaths.get("Intake2")).get(0).setLinearHeadingInterpolation(
                this.allianceColor.isRed() ? Constants.farRedShoot.getHeading() : Constants.farBlueShoot.getHeading(),
                this.allianceColor.isRed() ? redRow2Target.getHeading() : redRow2Target.mirror().getHeading(),
                0.5
        );


        Objects.requireNonNull(redIntakePaths.get("Intake2")).add(
                Objects.requireNonNull(redIntakePaths.get("Intake2")).get(0).getReversed()
        );
        Objects.requireNonNull(redIntakePaths.get("Intake2")).get(1).setLinearHeadingInterpolation(
                this.allianceColor.isRed() ? redRow2Target.getHeading() : redRow2Target.mirror().getHeading(),
                this.allianceColor.isRed() ? Constants.farRedShoot.getHeading() : Constants.farBlueShoot.getHeading()
        );

        return new PathChain(Objects.requireNonNull(redIntakePaths.get("Intake2")));
    }

    public PathChain initIntake3(HashMap<String, ArrayList<Path>> redIntakePaths) {
        redIntakePaths.put("Intake3", new ArrayList<>());
        Objects.requireNonNull(redIntakePaths.get("Intake3")).add(
                new Path(new BezierCurve(
                        this.allianceColor.isRed() ? Constants.farRedShoot : Constants.farBlueShoot,
                        this.allianceColor.isRed() ? redRow3Control : redRow3Control.mirror(),
                        this.allianceColor.isRed() ? redRow3Target : redRow3Target.mirror()
                ))
        );

        Objects.requireNonNull(redIntakePaths.get("Intake3")).get(0).setLinearHeadingInterpolation(
                this.allianceColor.isRed() ? Constants.farRedShoot.getHeading() : Constants.farBlueShoot.getHeading(),
                this.allianceColor.isRed() ? redRow3Target.getHeading() : redRow3Target.mirror().getHeading(),
                0.5
        );

        Objects.requireNonNull(redIntakePaths.get("Intake3")).add(
                Objects.requireNonNull(redIntakePaths.get("Intake3")).get(0).getReversed()
        );

        Objects.requireNonNull(redIntakePaths.get("Intake3")).get(1).setLinearHeadingInterpolation(
                this.allianceColor.isRed() ? redRow3Target.getHeading() : redRow3Target.mirror().getHeading(),
                this.allianceColor.isRed() ? Constants.farRedShoot.getHeading() : Constants.farBlueShoot.getHeading(),
                0.8
        );

        return new PathChain(Objects.requireNonNull(redIntakePaths.get("Intake3")));
    }

    public Selection getAlliance() {
        return allianceColor.getSelection();
    }
}
