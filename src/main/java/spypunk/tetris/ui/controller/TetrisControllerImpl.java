/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.controller;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import spypunk.tetris.factory.TetrisFactory;
import spypunk.tetris.model.Tetris;
import spypunk.tetris.model.TetrisEvent;
import spypunk.tetris.service.TetrisService;
import spypunk.tetris.ui.controller.command.TetrisControllerCommand;
import spypunk.tetris.ui.controller.event.TetrisControllerTetrisEventHandler;
import spypunk.tetris.ui.controller.gameloop.TetrisControllerGameLoop;
import spypunk.tetris.ui.controller.input.TetrisControllerInputHandler;
import spypunk.tetris.ui.factory.TetrisViewFactory;
import spypunk.tetris.ui.util.SwingUtils;
import spypunk.tetris.ui.view.TetrisView;

@Singleton
public class TetrisControllerImpl implements TetrisController {

    private final TetrisService tetrisService;

    private final TetrisView tetrisView;

    private final Tetris tetris;

    private final TetrisControllerGameLoop tetrisControllerGameLoop;

    private final TetrisControllerInputHandler tetrisControllerInputHandler;

    private final TetrisControllerTetrisEventHandler tetrisControllerTetrisEventHandler;

    @Inject
    public TetrisControllerImpl(final TetrisFactory tetrisFactory, final TetrisViewFactory tetrisViewFactory,
            final TetrisControllerGameLoop tetrisControllerGameLoop, final TetrisService tetrisService,
            final TetrisControllerInputHandler tetrisControllerInputHandler,
            final TetrisControllerTetrisEventHandler tetrisControllerTetrisEventHandler) {
        this.tetrisService = tetrisService;
        this.tetrisControllerInputHandler = tetrisControllerInputHandler;
        this.tetrisControllerTetrisEventHandler = tetrisControllerTetrisEventHandler;
        this.tetrisControllerGameLoop = tetrisControllerGameLoop;

        tetris = tetrisFactory.createTetris();
        tetrisView = tetrisViewFactory.createTetrisView(tetris);
    }

    @Override
    public void start() {
        tetrisView.show();
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
    public void onGameLoopUpdate() {
        executeTetrisControllerCommands(tetrisControllerInputHandler.handleInputs());

        tetrisControllerInputHandler.reset();

        tetrisService.update(tetris);

        final List<TetrisEvent> tetrisEvents = tetris.getTetrisEvents();

        executeTetrisControllerCommands(
            tetrisControllerTetrisEventHandler.handleEvents(tetrisEvents));

        tetrisEvents.clear();

        tetrisView.update();
    }

    @Override
    public void onKeyPressed(final int keyCode) {
        tetrisControllerInputHandler.onKeyPressed(keyCode);
    }

    @Override
    public void onKeyReleased(final int keyCode) {
        tetrisControllerInputHandler.onKeyReleased(keyCode);
    }

    private void executeTetrisControllerCommands(final Collection<TetrisControllerCommand> tetrisControllerCommands) {
        tetrisControllerCommands.forEach(tetrisControllerCommand -> tetrisControllerCommand.execute(tetris));
    }
}
