package org.firstinspires.ftc.teamcode.subsystems.shooter;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.util.AllianceColor;

public class Shooter {
    private Flywheel flywheel;
    private Tilt tilt;
    private Turret turret;

    public Shooter(HardwareMap hwMap) {
        flywheel = new Flywheel(hwMap);
        tilt = new Tilt(hwMap);
        turret = new Turret(hwMap);
    }

    public void runLoop(double x, double y, double heading) {
        turret.loop(x, y, heading);
        flywheel.farShoot();
        tilt.setTilt(0.25);
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
    }
}
