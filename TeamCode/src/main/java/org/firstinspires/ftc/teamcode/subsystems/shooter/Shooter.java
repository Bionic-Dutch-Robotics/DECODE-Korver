package org.firstinspires.ftc.teamcode.subsystems.shooter;

import com.pedropathing.geometry.Pose;
import com.pedropathing.math.Vector;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.util.AllianceColor;
import org.firstinspires.ftc.teamcode.util.Settings;


public class Shooter {
    public Flywheel flywheel;
    public Tilt tilt;
    public Turret turret;
    private AllianceColor alliance;
    private Pose currentPose = new Pose(), previousPose = new Pose(), velocity = new Pose();

    public Shooter(HardwareMap hwMap) {
        flywheel = new Flywheel(hwMap, new AllianceColor(AllianceColor.Selection.BLUE));
        tilt = new Tilt(hwMap);
        turret = new Turret(hwMap);
    }

    public void runLoop(Pose currentPose, Vector velocity, double headingVel) {
        this.previousPose = this.currentPose;
        this.currentPose = currentPose;
        Pose predictedPose = new Pose();
        if (velocity.getMagnitude() > 0.5) {
            predictedPose = currentPose.plus(
                    new Pose(
                            velocity.getXComponent(),
                            velocity.getYComponent(),
                            headingVel
                    ).times(Settings.Positions.Transfer.RUN_TO_POS_TIME /*0.05*/)
            );
        }
        velocity.getClass();
        turret.loop(predictedPose.getX(), predictedPose.getY(), predictedPose.getHeading());
        flywheel.adaptive(predictedPose.getX(), predictedPose.getY());
        tilt.auto(flywheel.getDistance(predictedPose.getX(), predictedPose.getY(), alliance));
    }

    public Tilt getTilt() {
        return tilt;
    }
    public Flywheel getFlywheel() {
        return flywheel;
    }
    public Turret getTurret() {
        return turret;
    }

    public void setAlliance(AllianceColor alliance) {

        turret.setAlliance(alliance);
        this.alliance = alliance;
    }
}
