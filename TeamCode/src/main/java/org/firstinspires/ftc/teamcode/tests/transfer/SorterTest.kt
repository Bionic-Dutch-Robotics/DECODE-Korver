package org.firstinspires.ftc.teamcode.tests.transfer

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.util.AllianceColor
import org.firstinspires.ftc.teamcode.util.Hardware.transfer
import org.firstinspires.ftc.teamcode.util.MatchSettings

@TeleOp(name="Sorting")
class SorterTest : OpMode() {
    override fun init() {
        MatchSettings.initSelection(
            hardwareMap, AllianceColor(AllianceColor.Selection.BLUE),
            gamepad1
        )
        MatchSettings.start()
    }

    override fun loop() {
        telemetry.update()
        telemetry.addLine("Kicker 1")
        telemetry.addData(
            "Color Red", transfer.sorter.rawSensorColors[0].red
        )
        telemetry.addData(
            "Color Green", transfer.sorter.rawSensorColors[0].green
        )
        telemetry.addData(
            "Color Blue", transfer.sorter.rawSensorColors[0].blue
        )
        telemetry.addData(
            "Distance", transfer.sorter.rawSensorDistances[0]
        )


        telemetry.addLine("Kicker 2")
        telemetry.addData(
            "Color Red", transfer.sorter.rawSensorColors[1].red
        )
        telemetry.addData(
            "Color Green", transfer.sorter.rawSensorColors[1].green
        )
        telemetry.addData(
            "Color Blue", transfer.sorter.rawSensorColors[1].blue
        )
        telemetry.addData(
            "Distance", transfer.sorter.rawSensorDistances[1]
        )



        telemetry.addLine("Kicker 3")
        telemetry.addData(
            "Color Red", transfer.sorter.rawSensorColors[2].red
        )
        telemetry.addData(
            "Color Green", transfer.sorter.rawSensorColors[2].green
        )
        telemetry.addData(
            "Color Blue", transfer.sorter.rawSensorColors[2].blue
        )
        telemetry.addData(
            "Distance", transfer.sorter.rawSensorDistances[2]
        )
    }
}