package org.firstinspires.ftc.teamcode.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Command {
    private BooleanSupplier event;
    private Runnable action;

    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Future<?> future = null;
    public Command(
            BooleanSupplier event,
            Runnable action
    ) {
        this.event = event;
        this.action = action;
    }

    public Command(
            Runnable action
    ) {
        this.event = () -> true;
        this.action = action;
    }

    public <T> Command(
            BooleanSupplier event,
            Supplier<T> argument,
            Consumer<T> action
    ) {
        this.event = event;
        this.action = () -> action.accept(argument.get());
    }

    public <T, U> Command(
            BooleanSupplier event,
            Supplier<T> argument1,
            Supplier<U> argument2,
            BiConsumer<T, U> action
    ) {
        this.event = event;
        this.action = () -> action.accept(
                argument1.get(),
                argument2.get()
        );
    }

    public BooleanSupplier getEvent() {
        return event;
    }
    public Runnable action() {
        return action;
    }

    public Boolean eventHasOccurred() {
        return event.getAsBoolean();
    }

    public void runAction() {
        action.run();
    }

    public void runActionAsync() {
        this.future = this.executor.submit(action);
    }

    public void stopAsync() {
        if (future != null && !future.isDone()) {
            future.cancel(true);
        }
    }

    /**
     * Run this in OpMode.stop()
     */
    public void kill() {
        this.stopAsync();
        executor.shutdown();
        executor = null;
        future = null;
    }
}
