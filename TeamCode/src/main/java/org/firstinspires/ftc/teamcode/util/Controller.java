package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class Controller {
    private Gamepad gamepad;
    private ArrayList<BooleanSupplier> criterias = new ArrayList<>();
    private ArrayList<Runnable> actions = new ArrayList<>();
    private ArrayList<Object> actionParameters = new ArrayList<>();

    public Controller(Gamepad gamepad) {
        this.gamepad = gamepad;
    }

    public <T> void bindAction(BooleanSupplier condition, T actionParameter, Consumer<T> action) {
        this.criterias.add(condition);
        this.actions.add(() -> action.accept(actionParameter));
        this.actionParameters.add(actionParameter);
    }

    public void update() {
        for (int i = 0; i < criterias.size(); i++) {
            if (criterias.get(i).getAsBoolean()) {
                    this.actions.get(i).run();
            }
        }
    }
}
