/*
 * Copyright © 2016-2017 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.controller.gameloop;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spypunk.tetris.service.TetrisService;
import spypunk.tetris.ui.controller.event.TetrisControllerTetrisEventHandler;
import spypunk.tetris.ui.controller.input.TetrisControllerInputHandler;
import spypunk.tetris.ui.view.TetrisView;

@Singleton
public final class TetrisControllerGameLoopImpl implements TetrisControllerGameLoop, Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(TetrisControllerGameLoopImpl.class);

    private static final int TICKS_PER_SECOND = 60;

    private static final int SKIP_TICKS = 1000 / TICKS_PER_SECOND;

    private final ExecutorService executorService;

    private final TetrisControllerInputHandler tetrisControllerInputHandler;

    private final TetrisControllerTetrisEventHandler tetrisControllerTetrisEventHandler;

    private final TetrisService tetrisService;

    private final TetrisView tetrisView;

    private volatile boolean running;

    @Inject
    public TetrisControllerGameLoopImpl(final TetrisService tetrisService,
            final TetrisControllerInputHandler tetrisControllerInputHandler,
            final TetrisControllerTetrisEventHandler tetrisControllerTetrisEventHandler,
            final TetrisView tetrisView) {

        this.tetrisService = tetrisService;
        this.tetrisControllerInputHandler = tetrisControllerInputHandler;
        this.tetrisControllerTetrisEventHandler = tetrisControllerTetrisEventHandler;
        this.tetrisView = tetrisView;

        executorService = Executors
                .newSingleThreadExecutor(runnable -> new Thread(runnable, "TetrisControllerGameLoop"));
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
        while (running) {
            long currentTick = System.currentTimeMillis();

            update();

            for (final long nextTick = currentTick + SKIP_TICKS; currentTick < nextTick; currentTick = System
                    .currentTimeMillis()) {
                waitMore();
            }
        }
    }

    private void update() {
        tetrisControllerInputHandler.handleInputs();

        tetrisService.update();

        tetrisControllerTetrisEventHandler.handleEvents();

        tetrisView.update();
    }

    private void waitMore() {
        try {
            Thread.sleep(1);
        } catch (final InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
            stop();
        }
    }
}
