package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.subsystems.Intake

@TeleOp(name="KOTLIN :)")
class KotlinTest : OpMode() {
    lateinit var intake: Intake;


    override fun init (){
        intake = Intake(hardwareMap)
        telemetry.addLine("IT WORKS")
        telemetry.update()
    }

    override fun init_loop() {
        if (gamepad1.a) intake.run()
        else intake.stop()
    }
    override fun loop() {

    }
}