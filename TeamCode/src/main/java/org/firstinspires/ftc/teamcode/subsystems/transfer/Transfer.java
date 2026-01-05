package org.firstinspires.ftc.teamcode.subsystems.transfer;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.util.Artifact;

public class Transfer {
    public Kicker kicker;
    public Sorter sorter;

    public Transfer (HardwareMap hwMap) {
        kicker = new Kicker(hwMap);
        sorter = new Sorter(hwMap);
    }

    public void start() {
        kicker.start();
    }

    public void fireSortedArtifacts() {
        kicker.setFireSequence(sorter.getOrder());
        kicker.runFireSequence();
    }

    public void cancelFire() {
        kicker.cancelSequence();
    }

    public void kickServoUp(int servoIndex) {
        kicker.kickServoDown(servoIndex);
    }

    public void kickServoDown(int servoIndex) {
        kicker.kickServoUp(servoIndex);
    }

    public void kickAllServosDown() {
        kicker.kickAllServosDown();
    }

    public void setMotif(Artifact[] motif) {
        sorter.setMotif(motif);
    }

    public Artifact[] getMotif() {
        return sorter.getMotif();
    }
}
