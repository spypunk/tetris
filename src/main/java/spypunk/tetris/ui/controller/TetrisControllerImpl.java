/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.controller;

import java.util.Optional;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import spypunk.tetris.factory.TetrisFactory;
import spypunk.tetris.model.Movement;
import spypunk.tetris.model.Tetris;
import spypunk.tetris.service.TetrisService;
import spypunk.tetris.ui.util.SwingUtils;
import spypunk.tetris.ui.view.TetrisView;

@Singleton
public class TetrisControllerImpl implements TetrisController {

    private static final int RENDER_PERIOD = 1000 / 60;

    private final Tetris tetris;

    private volatile boolean newGame = true;

    private volatile Optional<Movement> movement = Optional.empty();

    private volatile boolean pause;

    private Future<?> loopThread;

    @Inject
    private ScheduledExecutorService scheduledExecutorService;

    @Inject
    private TetrisView tetrisView;

    @Inject
    private TetrisService tetrisService;

    @Inject
    public TetrisControllerImpl(TetrisFactory tetrisFactory) {
        tetris = tetrisFactory.createTetris();
    }

    @Override
    public void start() {
        tetrisView.show();

        loopThread = scheduledExecutorService.scheduleAtFixedRate(() -> onGameLoop(), 0, RENDER_PERIOD,
            TimeUnit.MILLISECONDS);
    }

    @Override
    public void onWindowClosed() {
        loopThread.cancel(false);
        scheduledExecutorService.shutdown();
    }

    @Override
    public void onMoveLeft() {
        onMovement(Movement.LEFT);
    }

    @Override
    public void onMoveRight() {
        onMovement(Movement.RIGHT);
    }

    @Override
    public void onMoveDown() {
        onMovement(Movement.DOWN);
    }

    @Override
    public void onNewGame() {
        newGame = true;
    }

    @Override
    public void onPause() {
        pause = true;
    }

    @Override
    public void onRotate() {
        onMovement(Movement.ROTATE_CW);
    }

    @Override
    public void onURLClicked() {
        SwingUtils.openURI(tetris.getProjectURI());
    }

    @Override
    public Tetris getTetris() {
        return tetris;
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

    private void onMovement(Movement movement) {
        this.movement = Optional.of(movement);
    }
}
