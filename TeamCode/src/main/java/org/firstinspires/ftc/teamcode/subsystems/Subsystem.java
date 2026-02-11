package org.firstinspires.ftc.teamcode.subsystems;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class Subsystem {
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Future<?> future = null;

    public void init() {}
    public void loop() {}
    public void stop() {}
}
