/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.factory;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import spypunk.tetris.model.Movement;
import spypunk.tetris.service.TetrisService;
import spypunk.tetris.sound.Sound;
import spypunk.tetris.sound.service.SoundService;
import spypunk.tetris.ui.controller.command.TetrisControllerCommand;

@Singleton
public class TetrisControllerCommandFactoryImpl implements TetrisControllerCommandFactory {

    private final TetrisControllerCommand newGameTetrisControllerCommand;

    private final TetrisControllerCommand pauseTetrisControllerCommand;

    private final Map<Movement, TetrisControllerCommand> movementTetrisControllerCommands;

    private final TetrisControllerCommand shapeLockedTetrisControllerCommand;

    private final TetrisControllerCommand muteTetrisControllerCommand;

    private final TetrisControllerCommand gameOverTetrisControllerCommand;

    @Inject
    public TetrisControllerCommandFactoryImpl(TetrisService tetrisService, SoundService soundService) {
        newGameTetrisControllerCommand = tetris -> {
            tetrisService.newInstance(tetris);
            soundService.stopMusic();
            soundService.playMusic(Sound.BACKGROUND);
        };

        pauseTetrisControllerCommand = tetris -> {
            tetrisService.pauseInstance(tetris);
            soundService.pauseMusic();
        };

        movementTetrisControllerCommands = Maps.newHashMap();

        Lists.newArrayList(Movement.values()).stream()
                .forEach(movement -> movementTetrisControllerCommands.put(movement,
                    tetris -> tetrisService.updateInstanceMovement(tetris, movement)));

        shapeLockedTetrisControllerCommand = tetris -> soundService.playSound(Sound.SHAPE_LOCKED);
        muteTetrisControllerCommand = tetris -> soundService.mute();
        gameOverTetrisControllerCommand = tetris -> soundService.playMusic(Sound.GAME_OVER);
    }

    @Override
    public TetrisControllerCommand createNewGameTetrisControllerCommand() {
        return newGameTetrisControllerCommand;
    }

    @Override
    public TetrisControllerCommand createPauseTetrisControllerCommand() {
        return pauseTetrisControllerCommand;
    }

    @Override
    public TetrisControllerCommand createMovementTetrisControllerCommand(Movement movement) {
        return movementTetrisControllerCommands.get(movement);
    }

    @Override
    public TetrisControllerCommand createShapeLockedTetrisControllerCommand() {
        return shapeLockedTetrisControllerCommand;
    }

    @Override
    public TetrisControllerCommand createMuteTetrisControllerCommand() {
        return muteTetrisControllerCommand;
    }

    @Override
    public TetrisControllerCommand createGameOverTetrisControllerCommand() {
        return gameOverTetrisControllerCommand;
    }
}
