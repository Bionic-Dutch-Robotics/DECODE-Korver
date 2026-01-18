package org.firstinspires.ftc.teamcode.util;

import java.util.ArrayList;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

@SuppressWarnings("all")
public class Controller {
    private ArrayList<BooleanSupplier> criterias = new ArrayList<>();
    private ArrayList<Runnable> actions = new ArrayList<>();
    private ArrayList<Object> actionParameters = new ArrayList<>();

    public <T> void bindAction(BooleanSupplier condition, T actionParameter, Consumer<T> action) {
        this.criterias.add(condition);
        this.actions.add(() -> action.accept(actionParameter));
        this.actionParameters.add(actionParameter);
    }
    public void start() {
        actionParameters.clear();
        actionParameters = null;
    }

    public void update() {
        for (int i = 0; i < criterias.size(); i++) {
            if (criterias.get(i).getAsBoolean()) {
                    this.actions.get(i).run();
            }
        }
    }
}
