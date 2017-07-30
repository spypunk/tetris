/*
 * Copyright © 2016-2017 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.controller.event;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.collect.Maps;

import spypunk.tetris.guice.TetrisModule.TetrisProvider;
import spypunk.tetris.model.Tetris;
import spypunk.tetris.model.TetrisEvent;
import spypunk.tetris.ui.controller.command.TetrisControllerCommand;
import spypunk.tetris.ui.factory.TetrisControllerCommandFactory;

@Singleton
public class TetrisControllerTetrisEventHandlerImpl implements TetrisControllerTetrisEventHandler {

    private final Map<TetrisEvent, TetrisControllerCommand> tetrisControllerCommands = Maps
            .newHashMap();

    private final Tetris tetris;

    @Inject
    public TetrisControllerTetrisEventHandlerImpl(final TetrisControllerCommandFactory tetrisControllerCommandFactory,
            @TetrisProvider final Tetris tetris) {

        this.tetris = tetris;

        tetrisControllerCommands.put(TetrisEvent.SHAPE_LOCKED,
            tetrisControllerCommandFactory.createShapeLockedCommand());

        tetrisControllerCommands.put(TetrisEvent.GAME_OVER,
            tetrisControllerCommandFactory.createGameOverCommand());

        tetrisControllerCommands.put(TetrisEvent.ROWS_COMPLETED,
            tetrisControllerCommandFactory.createRowsCompletedCommand());
    }

    @Override
    public void handleEvents() {
        final List<TetrisEvent> tetrisEvents = tetris.getTetrisEvents();

        tetrisEvents.stream().map(tetrisControllerCommands::get).forEach(TetrisControllerCommand::execute);

        tetrisEvents.clear();
    }
}
