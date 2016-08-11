/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.controller;

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

    private final TetrisControllerInputHandler tetrisControllerInputHandler;

    @Inject
    public TetrisControllerImpl(TetrisFactory tetrisFactory, TetrisViewFactory tetrisViewFactory,
            GameLoopFactory gameLoopFactory, TetrisService tetrisService,
            TetrisControllerInputHandler tetrisControllerInputHandler) {
        this.tetrisService = tetrisService;
        this.tetrisControllerInputHandler = tetrisControllerInputHandler;
        tetris = tetrisFactory.createTetris();
        tetrisView = tetrisViewFactory.createTetrisView(tetris);
        gameLoop = gameLoopFactory.createGameLoop(this);
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
        tetrisControllerInputHandler.onNewGame();
    }

    @Override
    public void onPause() {
        tetrisControllerInputHandler.onPause();
    }

    @Override
    public void onURLOpen() {
        SwingUtils.openURI(tetris.getProjectURI());
    }

    @Override
    public void onUpdate() {
        tetrisControllerInputHandler.handleInput()
                .forEach(tetrisControllerCommand -> tetrisControllerCommand.execute(tetris));

        tetrisService.updateInstance(tetris.getTetrisInstance());

        tetrisControllerInputHandler.reset();
    }

    @Override
    public void onRender() {
        tetrisView.update();
    }

    private void onMovement(Movement movement) {
        tetrisControllerInputHandler.onMovement(movement);
    }
}
