/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.controller;

import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.collect.Maps;

import spypunk.tetris.factory.TetrisFactory;
import spypunk.tetris.model.Movement;
import spypunk.tetris.model.Tetris;
import spypunk.tetris.service.TetrisService;
import spypunk.tetris.ui.util.SwingUtils;
import spypunk.tetris.ui.view.TetrisView;

@Singleton
public class TetrisControllerImpl implements TetrisController {

    private static final int RENDER_PERIOD = 1000 / 60;

    @Inject
    private ScheduledExecutorService scheduledExecutorService;

    @Inject
    private TetrisView tetrisView;

    @Inject
    private TetrisService tetrisService;

    private final Tetris tetris;

    private final Map<Integer, Runnable> pressedKeyHandlers = Maps.newHashMap();

    private final Map<Integer, Runnable> releasedKeyHandlers = Maps.newHashMap();

    private volatile boolean newGame = true;

    private volatile Optional<Movement> movement = Optional.empty();

    private volatile boolean pause;

    private Future<?> loopThread;

    @Inject
    public TetrisControllerImpl(TetrisFactory tetrisFactory) {
        tetris = tetrisFactory.createTetris();
        initializeKeyHandlers();
    }

    @Override
    public void start() {
        tetrisView.setVisible(true);

        loopThread = scheduledExecutorService.scheduleAtFixedRate(() -> onGameLoop(), 0, RENDER_PERIOD,
            TimeUnit.MILLISECONDS);
    }

    @Override
    public void onWindowClosed() {
        loopThread.cancel(false);
        scheduledExecutorService.shutdown();
    }

    @Override
    public void onKeyPressed(int keyCode) {
        onKeyEvent(pressedKeyHandlers, keyCode);
    }

    @Override
    public void onKeyReleased(int keyCode) {
        onKeyEvent(releasedKeyHandlers, keyCode);
    }

    @Override
    public void onURLClicked() {
        SwingUtils.openURI(tetris.getProjectURI());
    }

    @Override
    public Tetris getTetris() {
        return tetris;
    }

    private void initializeKeyHandlers() {
        pressedKeyHandlers.put(KeyEvent.VK_LEFT, () -> movement = Optional.of(Movement.LEFT));
        pressedKeyHandlers.put(KeyEvent.VK_RIGHT, () -> movement = Optional.of(Movement.RIGHT));
        pressedKeyHandlers.put(KeyEvent.VK_DOWN, () -> movement = Optional.of(Movement.DOWN));

        releasedKeyHandlers.put(KeyEvent.VK_SPACE, () -> newGame = true);
        releasedKeyHandlers.put(KeyEvent.VK_P, () -> pause = true);
        releasedKeyHandlers.put(KeyEvent.VK_UP, () -> movement = Optional.of(Movement.ROTATE_CW));
    }

    private void onKeyEvent(Map<Integer, Runnable> keyHandlers, int keyCode) {
        if (keyHandlers.containsKey(keyCode)) {
            keyHandlers.get(keyCode).run();
        }
    }

    private void onGameLoop() {
        handleNewGame();
        handleMovement();
        handlePause();

        tetrisService.update(tetris);
        tetrisView.update();
    }

    private void handlePause() {
        if (pause) {
            tetrisService.pause(tetris);
            pause = false;
        }
    }

    private void handleMovement() {
        tetrisService.updateMovement(tetris, movement);
        movement = Optional.empty();
    }

    private void handleNewGame() {
        if (newGame) {
            tetrisService.newGame(tetris);
            newGame = false;
        }
    }
}
