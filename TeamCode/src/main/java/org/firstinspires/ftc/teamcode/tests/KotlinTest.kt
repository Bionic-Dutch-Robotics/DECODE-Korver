package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.util.AllianceColor
import org.firstinspires.ftc.teamcode.util.MatchSettings
import org.firstinspires.ftc.teamcode.util.Hardware.intake
import org.firstinspires.ftc.teamcode.util.Hardware.dt


@TeleOp(name="KOTLIN :)")
class KotlinTest : OpMode() {
    private var intakespeed = 0.0

    override fun init (){
        MatchSettings.initSelection(hardwareMap, AllianceColor(AllianceColor.Selection.BLUE), gamepad1)
        MatchSettings.start()
        telemetry.addLine("IT WORKS")
        telemetry.update()
        dt.startTeleOpDrive()
    }

    override fun init_loop() {
        dt.update()
        dt.teleOpDrive(
            (-gamepad1.left_stick_y).toDouble(),
            (-gamepad1.left_stick_x).toDouble(),
            (-gamepad1.right_stick_x).toDouble()
        )

        if (gamepad1.aWasPressed()) intakespeed += 0.05
        else if (gamepad1.bWasPressed()) intakespeed -= 0.05
        else if (gamepad1.xWasPressed()) intakespeed = 0.0
        else if (gamepad1.yWasPressed()) intakespeed = 1.0

        intake.custom(intakespeed)
    }
    override fun loop() {

    }
}