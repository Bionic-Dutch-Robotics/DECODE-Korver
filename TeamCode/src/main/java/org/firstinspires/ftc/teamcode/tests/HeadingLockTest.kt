package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.util.AllianceColor
import org.firstinspires.ftc.teamcode.util.Hardware.dt
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
        dt.follower.startTeleopDrive()
    }

    override fun loop() {
        dt.follower.update()
        dt.teleOpDrive(
            minus(gamepad1.left_stick_y),
            minus(gamepad1.left_stick_x),
            minus(gamepad1.right_stick_x)
        )
    }
}

private operator fun Any.minus(fl: Float): Double {
    return (Double) -1*fl
}
