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
import spypunk.tetris.model.Tetris;
import spypunk.tetris.service.TetrisService;
import spypunk.tetris.sound.service.SoundService;
import spypunk.tetris.ui.controller.event.TetrisControllerTetrisEventHandler;
import spypunk.tetris.ui.controller.input.TetrisControllerInputHandler;
import spypunk.tetris.ui.controller.input.TetrisControllerInputTranslator;
import spypunk.tetris.ui.factory.TetrisControllerInputTranslatorFactory;
import spypunk.tetris.ui.factory.TetrisViewFactory;
import spypunk.tetris.ui.util.SwingUtils;
import spypunk.tetris.ui.view.TetrisView;

@Singleton
public class TetrisControllerImpl implements TetrisController, GameLoopListener {

    private final TetrisService tetrisService;

    private final TetrisView tetrisView;

    private final Tetris tetris;

    private final GameLoop gameLoop;

    private final TetrisControllerInputTranslator tetrisControllerInputTranslator;

    private final TetrisControllerInputHandler tetrisControllerInputHandler;

    private final TetrisControllerTetrisEventHandler tetrisControllerTetrisEventHandler;

    private final SoundService soundService;

    @Inject
    public TetrisControllerImpl(TetrisFactory tetrisFactory, TetrisViewFactory tetrisViewFactory,
            GameLoopFactory gameLoopFactory, TetrisService tetrisService,
            TetrisControllerInputHandler tetrisControllerInputHandler,
            TetrisControllerInputTranslatorFactory tetrisControllerInputTranslatorFactory,
            TetrisControllerTetrisEventHandler tetrisControllerTetrisEventHandler,
            SoundService soundService) {
        this.tetrisService = tetrisService;
        this.tetrisControllerInputHandler = tetrisControllerInputHandler;
        this.tetrisControllerTetrisEventHandler = tetrisControllerTetrisEventHandler;
        this.soundService = soundService;

        tetrisControllerInputTranslator = tetrisControllerInputTranslatorFactory
                .createTetrisControllerInputTranslator(tetrisControllerInputHandler);

        tetris = tetrisFactory.createTetris();
        tetrisView = tetrisViewFactory.createTetrisView(tetris);
        gameLoop = gameLoopFactory.createGameLoop(this);
    }

    @Override
    public void start() {
        tetrisControllerInputHandler.onNewGame();

        tetrisView.show();

        gameLoop.start();
    }

    @Override
    public void onWindowClosed() {
        soundService.shutdown();

        gameLoop.stop();
    }

    @Override
    public void onURLOpen() {
        SwingUtils.openURI(tetris.getProjectURI());
    }

    @Override
    public void onUpdate() {
        tetrisControllerInputHandler.handleInput()
                .forEach(tetrisControllerCommand -> tetrisControllerCommand.execute(tetris));

        tetrisService.updateInstance(tetris);

        tetrisControllerTetrisEventHandler.handleEvents(tetris.getTetrisInstance().getTetrisEvents())
                .forEach(tetrisControllerCommand -> tetrisControllerCommand.execute(tetris));

        tetrisControllerInputHandler.reset();
    }

    @Override
    public void onRender() {
        tetrisView.update();
    }

    @Override
    public void onKeyPressed(int keyCode) {
        tetrisControllerInputTranslator.onKeyPressed(keyCode);
    }

    @Override
    public void onKeyReleased(int keyCode) {
        tetrisControllerInputTranslator.onKeyReleased(keyCode);
    }
}
