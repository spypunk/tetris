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
import spypunk.tetris.ui.controller.command.cache.TetrisControllerCommandCache;
import spypunk.tetris.ui.controller.command.cache.TetrisControllerCommandCache.TetrisControllerCommandType;

@Singleton
public class TetrisControllerTetrisEventHandlerImpl implements TetrisControllerTetrisEventHandler {

    private final Map<TetrisEvent, TetrisControllerCommandType> tetrisControllerCommandTypes = Maps
            .newHashMap();

    private final Tetris tetris;

    private final TetrisControllerCommandCache tetrisControllerCommandCache;

    @Inject
    public TetrisControllerTetrisEventHandlerImpl(final TetrisControllerCommandCache tetrisControllerCommandCache,
            @TetrisProvider final Tetris tetris) {

        this.tetris = tetris;
        this.tetrisControllerCommandCache = tetrisControllerCommandCache;

        tetrisControllerCommandTypes.put(TetrisEvent.SHAPE_LOCKED, TetrisControllerCommandType.SHAPE_LOCKED);
        tetrisControllerCommandTypes.put(TetrisEvent.GAME_OVER, TetrisControllerCommandType.GAME_OVER);
        tetrisControllerCommandTypes.put(TetrisEvent.ROWS_COMPLETED, TetrisControllerCommandType.ROWS_COMPLETED);
    }

    @Override
    public void handleEvents() {
        final List<TetrisEvent> tetrisEvents = tetris.getTetrisEvents();

        tetrisEvents.stream()
                .map(tetrisControllerCommandTypes::get)
                .map(tetrisControllerCommandCache::getTetrisControllerCommand)
                .forEach(TetrisControllerCommand::execute);

        tetrisEvents.clear();
    }
}
