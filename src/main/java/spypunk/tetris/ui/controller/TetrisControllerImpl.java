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

import spypunk.tetris.factory.GameLoopFactory;
import spypunk.tetris.factory.TetrisFactory;
import spypunk.tetris.gameloop.GameLoop;
import spypunk.tetris.gameloop.GameLoopListener;
import spypunk.tetris.model.Movement;
import spypunk.tetris.model.Tetris;
import spypunk.tetris.service.TetrisService;
import spypunk.tetris.ui.factory.TetrisViewFactory;
import spypunk.tetris.ui.util.SwingUtils;
import spypunk.tetris.ui.view.TetrisView;

@Singleton
public class TetrisControllerImpl implements TetrisController, GameLoopListener {

    private final TetrisService tetrisService;

    private final TetrisView tetrisView;

    private final Tetris tetris;

    private final GameLoop gameLoop;

    private volatile boolean newGame;

    private volatile Optional<Movement> movement;

    private volatile boolean pause;

    @Inject
    public TetrisControllerImpl(TetrisFactory tetrisFactory, TetrisViewFactory tetrisViewFactory,
            GameLoopFactory gameLoopFactory, TetrisService tetrisService) {
        this.tetrisService = tetrisService;
        tetris = tetrisFactory.createTetris();
        tetrisView = tetrisViewFactory.createTetrisView(tetris);
        gameLoop = gameLoopFactory.createGameLoop(this);
        movement = Optional.empty();
    }

    @Override
    public void start() {
        tetrisService.newInstance(tetris);

        tetrisView.show();

        gameLoop.start();
    }

    @Override
    public void onWindowClosed() {
        gameLoop.stop();
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
    public void onUpdate() {
        handleNewGame();
        handleMovement();
        handlePause();

        tetrisService.updateInstance(tetris.getTetrisInstance());
    }

    @Override
    public void onRender() {
        tetrisView.update();
    }

    private void handlePause() {
        if (pause) {
            tetrisService.pauseInstance(tetris.getTetrisInstance());
            pause = false;
        }
    }

    private void handleMovement() {
        if (movement.isPresent()) {
            tetrisService.updateInstanceMovement(tetris.getTetrisInstance(), movement.get());
            movement = Optional.empty();
        }
    }

    private void handleNewGame() {
        if (newGame) {
            tetrisService.newInstance(tetris);
            newGame = false;
        }
    }

    private void onMovement(Movement movement) {
        this.movement = Optional.of(movement);
    }
}
