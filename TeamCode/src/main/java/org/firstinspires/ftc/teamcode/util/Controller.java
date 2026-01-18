package org.firstinspires.ftc.teamcode.util;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("all")
public class Controller {
    private ArrayList<BooleanSupplier> criterias = new ArrayList<>();
    private ArrayList<Runnable> actions = new ArrayList<>();

    public <T> void bindAction(BooleanSupplier condition, Supplier<T> actionParameter, Consumer<T> action) {
        this.criterias.add(condition);
        this.actions.add(() -> action.accept(actionParameter.get()));
    }

    public <T, U> void bindAction(BooleanSupplier condition, Supplier<T> actionParameter1, Supplier<U> actionParameter2, BiConsumer<T, U> action) {
        this.criterias.add(condition);
        this.actions.add(() -> action.accept(actionParameter1.get(), actionParameter2.get()));
    }

    public void update() {
        for (int i = 0; i < criterias.size(); i++) {
            if (criterias.get(i).getAsBoolean()) {
                    this.actions.get(i).run();
            }
        }
    }
}
