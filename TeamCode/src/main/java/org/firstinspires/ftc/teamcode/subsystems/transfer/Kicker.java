package org.firstinspires.ftc.teamcode.subsystems.transfer;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.util.Settings;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.CompletableFuture;

import dev.frozenmilk.dairy.core.FeatureRegistrar;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.mercurial.subsystems.SubsystemObjectCell;
import kotlin.annotation.MustBeDocumented;

@SuppressWarnings("all")
public class Kicker implements Subsystem {
    public static final Kicker INSTANCE = new Kicker() {};
    private SubsystemObjectCell<Servo[]> kickers;
    public static Servo[] getServos() {
        return INSTANCE.kickers.get();
    }
    private ElapsedTime servoTimer;
    private Integer[] order;
    private CompletableFuture<Void> runFireSequence = null;

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @MustBeDocumented
    @Inherited
    public @interface Attach{}

    private Dependency<?> dependency =
            // the default dependency ensures that mercurial is attached
            Subsystem.DEFAULT_DEPENDENCY
                    // this is the standard attach annotation that is recommended for features
                    // if you are using other features, you should add them as
                    // dependencies as well
                    // you can also use the annotation to set up and manage
                    // declarative settings for your subsystem, if desired
                    .and(new SingleAnnotation<>(Attach.class));



    // init code might go in here
    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        // default command should be set up here, not in the constructor
        setDefaultCommand(/*simpleCommand()*/null);
    }

    /**
     * TODO: Swap to Mercurial 2.0
     */
    public Kicker() {
            for (int i = 0; i < kickers.get().length; i++) {
                subsystemCell(() -> FeatureRegistrar.getActiveOpMode().hardwareMap.get(Servo.class, Settings.HardwareNames.Transfer.KICKERS[0]));
            }

            servoTimer = new ElapsedTime(ElapsedTime.Resolution.SECONDS);
            order = new Integer[3];
    }

    public void start() {
        this.kickAllServosDown();
    }
    public void setFireSequence(Integer[] order) {
        this.order = order;
    }

    public void runFireSequence ()  {
        this.runFireSequence = CompletableFuture.runAsync(this::createFireSequence);
    }
    private void createFireSequence() {
        for (int i : this.order) {
            servoTimer.reset();
            while (servoTimer.time() < Settings.Positions.Transfer.RUN_TO_POS_TIME) {
                kickServoUp(i);
            }

            servoTimer.reset();
            while (servoTimer.time() < Settings.Positions.Transfer.RUN_TO_POS_TIME) {
                kickServoDown(i);
            }
        }
        this.runFireSequence.complete(null);
    }
    public void cancelSequence() {
        if (runFireSequence != null && !runFireSequence.isDone()) {
            runFireSequence.complete(null);
            //runFireSequence.cancel(true); // Cancels the future and interrupts the sleep
            this.kickAllServosDown();
        }

    }

    public void kickServoUp(int servoIndex) {
        kickers.get()[servoIndex].setPosition(Settings.Positions.Transfer.upPos[servoIndex]);
    }

    public void kickServoDown(int servoIndex) {
        kickers.get()[servoIndex].setPosition(Settings.Positions.Transfer.downPos[servoIndex]);
    }

    public void kickAllServosDown() {
        for (int i = 0; i < kickers.get().length; i++) {
            this.kickServoDown(i);
        }
    }

    public Double[] getServoPositions() {
        return new Double[] {kickers.get()[0].getPosition(), kickers.get()[1].getPosition(), kickers.get()[2].getPosition()};
    }

    @NonNull
    @Override
    public Dependency<?> getDependency() {
        return dependency;
    }

    @Override
    public void setDependency(@NonNull Dependency<?> dependency) {
        this.dependency = dependency;
    }

    @NonNull
    public static Lambda simpleCommand() {
        // we need to give commands names
        // names help to give helpful error messages when something goes wrong in your command
        // Mercurial will automatically rename your command to match the standard convention
        // learn more about names and error messages in the names and messages overview
        return new Lambda("simple")
                .addRequirements(INSTANCE)
                .setInit(() -> INSTANCE.createFireSequence())
                .setEnd(interrupted -> {
                    if (!interrupted) INSTANCE.kickAllServosDown();
                });
    }
}