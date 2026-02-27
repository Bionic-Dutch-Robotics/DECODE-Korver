package org.firstinspires.ftc.teamcode.tests.drivetrain

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.util.AllianceColor
import org.firstinspires.ftc.teamcode.util.Hardware
import org.firstinspires.ftc.teamcode.util.MatchSettings

@TeleOp(name="Heading Lock")
class HeadingLockTest : OpMode() {
    private val alliance = AllianceColor(AllianceColor.Selection.BLUE)

    override fun init() {
        MatchSettings.initSelection(
            hardwareMap,
            alliance,
            gamepad1
        )
        MatchSettings.start()
        //dt.follower.startHeadingLock()
        Hardware.dt.follower.startTeleopDrive()
    }

    override fun loop() {
        Hardware.dt.follower.update()
        Hardware.dt.teleOpDrive(
            -gamepad1.left_stick_y.toDouble(),
            -gamepad1.left_stick_x.toDouble(),
            -gamepad1.right_stick_x.toDouble()
        )
    }
}