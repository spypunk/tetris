/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.gameloop;

import java.util.concurrent.ExecutorService;

public final class GameLoopImpl implements GameLoop, Runnable {

    private static final int TICKS_PER_SECOND = 60;

    private static final int SKIP_TICKS = 1000 / TICKS_PER_SECOND;

    private final ExecutorService executorService;

    private final GameLoopListener gameLoopListener;

    private volatile boolean running;

    public GameLoopImpl(final ExecutorService executorService, final GameLoopListener gameLoopListener) {
        this.executorService = executorService;
        this.gameLoopListener = gameLoopListener;
    }

    @Override
    public void start() {
        running = true;
        executorService.execute(this);
    }

    @Override
    public void stop() {
        running = false;
        executorService.shutdown();
    }

    @Override
    public void run() {
        long lastTick = System.currentTimeMillis();

        while (running) {
            long newTick = System.currentTimeMillis();

            for (; newTick - lastTick < SKIP_TICKS; newTick = System
                    .currentTimeMillis()) {
            }

            lastTick = newTick;

            gameLoopListener.onUpdate();
            gameLoopListener.onRender();
        }
    }
}
