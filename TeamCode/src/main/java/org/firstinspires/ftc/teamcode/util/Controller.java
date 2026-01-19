package org.firstinspires.ftc.teamcode.util;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("all")
public class Controller {
    private ArrayList<Binding> bindings = new ArrayList<>();

    public <T> void bind(BooleanSupplier condition, Runnable action) {
        bindings.add(
                new Binding(
                        condition,
                        action
                )
        );
    }
    public <T> void bind(BooleanSupplier condition, Supplier<T> actionParameter, Consumer<T> action) {
        bindings.add(
                new Binding(
                        condition,
                        () -> action.accept(actionParameter.get())
                )
        );
    }

    public <T, U> void bind(BooleanSupplier condition, Supplier<T> actionParameter1, Supplier<U> actionParameter2, BiConsumer<T, U> action) {
        bindings.add(
                new Binding(
                        condition,
                        () -> action.accept(actionParameter1.get(), actionParameter2.get())
                )
        );
    }

    public void update() {
        for (Binding binding : bindings) {
            if (binding.condition.getAsBoolean()) {
                binding.action.run();
            }
        }
    }

    public static class Binding {
        private final BooleanSupplier condition;
        private final Runnable action;

        public Binding(BooleanSupplier condition, Runnable action) {
            this.condition = condition;
            this.action = action;
        }

        public BooleanSupplier getCondition() {
            return condition;
        }
        public Runnable getAction() {
            return action;
        }
    }
}
