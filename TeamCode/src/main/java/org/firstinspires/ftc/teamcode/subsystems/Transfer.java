package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.util.Settings.HardwareNames;
import org.firstinspires.ftc.teamcode.util.Settings.Positions;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Transfer {
    private Servo transfer;

    public Transfer(HardwareMap hwMap) {
        transfer = hwMap.get(Servo.class, HardwareNames.Transfer.TRANSFER);
    }

    public void reload() {
        transfer.setPosition(Positions.Transfer.RELOAD_POS);
    }
    public void feed() {
        transfer.setPosition(Positions.Transfer.FEED_SHOOTER);
    }
}
