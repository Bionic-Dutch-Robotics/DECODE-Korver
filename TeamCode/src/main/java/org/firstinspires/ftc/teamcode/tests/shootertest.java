package org.firstinspires.ftc.teamcode.tests;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.control.PIDFController;
import com.pedropathing.math.MathFunctions;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

@Configurable
@TeleOp(name="Shooter")
public class shootertest extends OpMode {
    //public static Shooter shooter;
    public static DcMotorEx shooter;
    public static com.pedropathing.control.PIDFCoefficients pidfCoefficients = new PIDFCoefficients(0.005, 0,0.000011,0);
    public static com.pedropathing.control.PIDFController shooterPidf;
    public static double shooterTarget;

    @Override
    public void init() {
        //shooter = new Shooter(hardwareMap, Constants.shooterCoefficients);
        shooter = hardwareMap.get(DcMotorEx.class, "shooter");
        shooterPidf = new PIDFController(pidfCoefficients);
        //shooter.setVelocityPIDFCoefficients(Constants.shooterCoefficients.p, Constants.shooterCoefficients.i, Constants.shooterCoefficients.d, Constants.shooterCoefficients.f);
        shooterTarget = 295;    //230 TARGET SPEED = 140 ACTUAL SPEED   //295 TARGET SPEED = 180 ACTUAL SPEED
    }

    @Override
    public void loop() {
        shooterPidf.updatePosition(shooter.getVelocity(AngleUnit.DEGREES));
        shooterPidf.setTargetPosition(shooterTarget);
        shooter.setPower(MathFunctions.clamp(shooterPidf.run(), -1, 1));
        telemetry.addData("Shooter Speed: ", shooter.getVelocity(AngleUnit.DEGREES));
        telemetry.addData("Shooter Power: ", shooter.getPower());
        telemetry.addData("Shooter Pos: ", shooter.getCurrentPosition());
        telemetry.addData("PIDF: ", shooterPidf.getCoefficients());
        telemetry.addData("Current: ", shooter.getCurrent(CurrentUnit.AMPS));
        telemetry.update();
    }
}
