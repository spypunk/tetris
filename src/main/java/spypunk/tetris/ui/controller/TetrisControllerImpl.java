/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.controller;

import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import spypunk.tetris.factory.TetrisFactory;
import spypunk.tetris.gameloop.TetrisGameLoop;
import spypunk.tetris.gameloop.TetrisGameLoopListener;
import spypunk.tetris.model.Movement;
import spypunk.tetris.model.Tetris;
import spypunk.tetris.service.TetrisService;
import spypunk.tetris.ui.factory.TetrisViewFactory;
import spypunk.tetris.ui.util.SwingUtils;
import spypunk.tetris.ui.view.TetrisView;

@Singleton
public class TetrisControllerImpl implements TetrisController, TetrisGameLoopListener {

    private final TetrisView tetrisView;

    private final Tetris tetris;

    private final TetrisGameLoop tetrisGameLoop = new TetrisGameLoop(this);

    private volatile boolean newGame;

    private volatile Optional<Movement> movement = Optional.empty();

    private volatile boolean pause;

    @Inject
    private TetrisService tetrisService;

    @Inject
    public TetrisControllerImpl(TetrisFactory tetrisFactory, TetrisViewFactory tetrisViewFactory) {
        tetris = tetrisFactory.createTetris();
        tetrisView = tetrisViewFactory.createTetrisView(tetris);
    }

    @Override
    public void start() {
        tetrisService.newGame(tetris);

        tetrisView.show();

        tetrisGameLoop.start();
    }

    @Override
    public void onWindowClosed() {
        tetrisGameLoop.stop();
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
    public void onRotate() {
        onMovement(Movement.ROTATE_CW);
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
    public void onURLOpen() {
        SwingUtils.openURI(tetris.getProjectURI());
    }

    @Override
    public void update() {
        handleNewGame();
        handleMovement();
        handlePause();

        tetrisService.update(tetris);
    }

    @Override
    public void render() {
        tetrisView.update();
    }

    private void handlePause() {
        if (pause) {
            tetrisService.pause(tetris);
            pause = false;
        }
    }

    private void handleMovement() {
        if (movement.isPresent()) {
            tetrisService.updateMovement(tetris, movement.get());
            movement = Optional.empty();
        }
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
