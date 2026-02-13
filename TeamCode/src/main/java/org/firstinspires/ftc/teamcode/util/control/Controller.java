package org.firstinspires.ftc.teamcode.util.control;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("all")
public class Controller {
    private ArrayList<Command> bindings = new ArrayList<>();

    public void bind(Command command) {
        bindings.add(command);
    }
    public <T> void bind(BooleanSupplier condition, Runnable action) {
        bindings.add(
                new Command (
                        condition,
                        action
                )
        );
    }
    public <T> void bind(BooleanSupplier condition, Supplier<T> actionParameter, Consumer<T> action) {
        bindings.add(
                new Command(
                        condition,
                        () -> action.accept(actionParameter.get())
                )
        );
    }

    public <T, U> void bind(BooleanSupplier condition, Supplier<T> actionParameter1, Supplier<U> actionParameter2, BiConsumer<T, U> action) {
        bindings.add(
                new Command(
                        condition,
                        () -> action.accept(actionParameter1.get(), actionParameter2.get())
                )
        );
    }

    public void removeBindingFromCondition(BooleanSupplier condition) {
        for (int i=0; i < bindings.size(); i++) {
            if (bindings.get(i).getEvent().equals(condition)) {
                bindings.get(i).kill();
                bindings.remove(i);
            }
        }
    }

    public void update() {
        for (Command command : bindings) {
            if (command.eventHasOccurred()) {
                command.runAction();
            }
        }
    }

    /**
     * Run in OpMode.stop()
     */
    public void stop() {
        for (int i=0; i < bindings.size(); i++) {
            bindings.get(i).kill();
            bindings.remove(i);
        }
    }

    public void removeAllBindings() {
        for (int i=0; i < bindings.size(); i++) {
            bindings.get(i).kill();
            bindings.remove(i);
        }
    }
}

