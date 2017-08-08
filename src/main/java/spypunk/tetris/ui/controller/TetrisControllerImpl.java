/*
 * Copyright © 2016-2017 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.controller;

import javax.inject.Inject;
import javax.inject.Singleton;

import spypunk.tetris.guice.TetrisModule.TetrisProvider;
import spypunk.tetris.model.Tetris;
import spypunk.tetris.ui.controller.gameloop.TetrisControllerGameLoop;
import spypunk.tetris.ui.controller.input.TetrisControllerInputHandler;
import spypunk.tetris.ui.util.SwingUtils;
import spypunk.tetris.ui.view.TetrisMainView;

@Singleton
public class TetrisControllerImpl implements TetrisController {

    private final Tetris tetris;

    private final TetrisControllerGameLoop tetrisControllerGameLoop;

    private final TetrisControllerInputHandler tetrisControllerInputHandler;

    private final TetrisMainView tetrisMainView;

    @Inject
    public TetrisControllerImpl(final TetrisControllerGameLoop tetrisControllerGameLoop,
            final TetrisControllerInputHandler tetrisControllerInputHandler,
            final TetrisMainView tetrisMainView,
            final @TetrisProvider Tetris tetris) {

        this.tetrisControllerGameLoop = tetrisControllerGameLoop;
        this.tetrisControllerInputHandler = tetrisControllerInputHandler;
        this.tetrisMainView = tetrisMainView;
        this.tetris = tetris;
    }

    @Override
    public void start() {
        tetrisMainView.show();
    }

    @Override
    public void onWindowOpened() {
        tetrisControllerGameLoop.start();
    }

    @Override
    public void onWindowClosed() {
        tetrisControllerGameLoop.stop();
    }

    @Override
    public void onURLOpen() {
        SwingUtils.openURI(tetris.getProjectURI());
    }

    @Override
    public void onKeyPressed(final int keyCode) {
        tetrisControllerInputHandler.onKeyPressed(keyCode);
    }

    @Override
    public void onKeyReleased(final int keyCode) {
        tetrisControllerInputHandler.onKeyReleased(keyCode);
    }
}
