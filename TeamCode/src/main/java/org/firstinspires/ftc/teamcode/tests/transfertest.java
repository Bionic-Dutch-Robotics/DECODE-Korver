package org.firstinspires.ftc.teamcode.tests;

import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Transfer;

@TeleOp(name="TRANSFER")
public class transfertest extends OpMode {
    private Transfer transfer;
    @Override
    public void init() {
        transfer = new Transfer(hardwareMap);
    }

    @Override
    public void loop() {
        transfer.reload();
        try{
        sleep(1000);} catch(InterruptedException ignored){}
        transfer.feed();
        try{
            sleep(1000);} catch(InterruptedException ignored){}
    }
}
