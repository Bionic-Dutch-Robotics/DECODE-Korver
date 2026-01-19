package org.firstinspires.ftc.teamcode.subsystems.shooter;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.util.AllianceColor;

public class Shooter {
    public Flywheel flywheel;
    public Tilt tilt;
    public Turret turret;
    private AllianceColor alliance;

    public Shooter(HardwareMap hwMap) {
        flywheel = new Flywheel(hwMap);
        tilt = new Tilt(hwMap);
        turret = new Turret(hwMap);
    }

    public void runLoop(double x, double y, double heading) {
        turret.loop(x, y, heading);
        flywheel.adaptive(x, y, alliance);
        tilt.auto(flywheel.getDistance(x, y, alliance));
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
