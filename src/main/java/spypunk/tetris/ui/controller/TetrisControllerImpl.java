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

import spypunk.tetris.ui.controller.gameloop.TetrisControllerGameLoop;
import spypunk.tetris.ui.controller.input.TetrisControllerInputHandler;

@Singleton
public class TetrisControllerImpl implements TetrisController {

    private final TetrisControllerGameLoop tetrisControllerGameLoop;

    private final TetrisControllerInputHandler tetrisControllerInputHandler;

    @Inject
    public TetrisControllerImpl(final TetrisControllerGameLoop tetrisControllerGameLoop,
            final TetrisControllerInputHandler tetrisControllerInputHandler) {

        this.tetrisControllerGameLoop = tetrisControllerGameLoop;
        this.tetrisControllerInputHandler = tetrisControllerInputHandler;
    }

    @Override
    public void start() {
        tetrisControllerGameLoop.start();
    }

    @Override
    public void onWindowClosed() {
        tetrisControllerGameLoop.stop();
    }

    @Override
    public void onProjectURLClicked() {
        tetrisControllerInputHandler.onProjectURLClicked();
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
