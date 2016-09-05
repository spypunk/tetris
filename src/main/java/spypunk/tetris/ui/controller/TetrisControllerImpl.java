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

import org.apache.commons.collections4.CollectionUtils;

import spypunk.tetris.factory.GameLoopFactory;
import spypunk.tetris.factory.TetrisFactory;
import spypunk.tetris.gameloop.GameLoop;
import spypunk.tetris.gameloop.GameLoopListener;
import spypunk.tetris.model.Tetris;
import spypunk.tetris.model.TetrisEvent;
import spypunk.tetris.service.TetrisService;
import spypunk.tetris.ui.controller.command.TetrisControllerCommand;
import spypunk.tetris.ui.controller.event.TetrisControllerTetrisEventHandler;
import spypunk.tetris.ui.controller.event.TetrisControllerTetrisEventHandlerImpl;
import spypunk.tetris.ui.controller.input.TetrisControllerInputHandler;
import spypunk.tetris.ui.controller.input.TetrisControllerInputHandlerImpl;
import spypunk.tetris.ui.factory.TetrisControllerCommandFactory;
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

    private final TetrisControllerTetrisEventHandler tetrisControllerTetrisEventHandler;

    @Inject
    public TetrisControllerImpl(final TetrisFactory tetrisFactory, final TetrisViewFactory tetrisViewFactory,
            final GameLoopFactory gameLoopFactory, final TetrisService tetrisService,
            final TetrisControllerCommandFactory tetrisControllerCommandFactory) {
        this.tetrisService = tetrisService;
        tetrisControllerInputHandler = new TetrisControllerInputHandlerImpl(tetrisControllerCommandFactory);
        tetrisControllerTetrisEventHandler = new TetrisControllerTetrisEventHandlerImpl(
                tetrisControllerCommandFactory);

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
    public void onURLOpen() {
        SwingUtils.openURI(tetris.getProjectURI());
    }

    @Override
    public void onUpdate() {
        executeTetrisControllerCommands(tetrisControllerInputHandler.handleInputs());

        tetrisControllerInputHandler.reset();

        tetrisService.updateInstance(tetris);

        final List<TetrisEvent> tetrisEvents = tetris.getTetrisInstance().getTetrisEvents();

        executeTetrisControllerCommands(
            tetrisControllerTetrisEventHandler.handleEvents(tetrisEvents));
    }

    @Override
    public void onRender() {
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

    @Override
    public TetrisView getTetrisView() {
        return tetrisView;
    }

    private void executeTetrisControllerCommands(final Collection<TetrisControllerCommand> tetrisControllerCommands) {
        if (CollectionUtils.isEmpty(tetrisControllerCommands)) {
            return;
        }

        tetrisControllerCommands.forEach(tetrisControllerCommand -> tetrisControllerCommand.execute(tetris));
    }
}
