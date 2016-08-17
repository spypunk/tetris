/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.factory;

import javax.inject.Inject;
import javax.inject.Singleton;

import spypunk.tetris.model.Movement;
import spypunk.tetris.model.TetrisInstance.State;
import spypunk.tetris.service.TetrisService;
import spypunk.tetris.sound.Sound;
import spypunk.tetris.sound.service.SoundService;
import spypunk.tetris.ui.controller.command.TetrisControllerCommand;

@Singleton
public class TetrisControllerCommandFactoryImpl implements TetrisControllerCommandFactory {

    private final TetrisService tetrisService;

    private final SoundService soundService;

    @Inject
    public TetrisControllerCommandFactoryImpl(TetrisService tetrisService, SoundService soundService) {
        this.tetrisService = tetrisService;
        this.soundService = soundService;
    }

    @Override
    public TetrisControllerCommand createNewGameTetrisControllerCommand() {
        return tetris -> {
            tetrisService.newInstance(tetris);
            soundService.playMusic(Sound.BACKGROUND);
        };
    }

    @Override
    public TetrisControllerCommand createPauseTetrisControllerCommand() {
        return tetris -> {
            tetrisService.pauseInstance(tetris);

            if (!State.GAME_OVER.equals(tetris.getTetrisInstance().getState())) {
                soundService.pauseMusic();
            }
        };
    }

    @Override
    public TetrisControllerCommand createMovementTetrisControllerCommand(Movement movement) {
        return tetris -> tetrisService.updateInstanceMovement(tetris, movement);
    }

    @Override
    public TetrisControllerCommand createShapeLockedTetrisControllerCommand() {
        return tetris -> soundService.playSound(Sound.SHAPE_LOCKED);
    }

    @Override
    public TetrisControllerCommand createMuteTetrisControllerCommand() {
        return tetris -> soundService.mute();
    }

    @Override
    public TetrisControllerCommand createGameOverTetrisControllerCommand() {
        return tetris -> soundService.playMusic(Sound.GAME_OVER);
    }

    @Override
    public TetrisControllerCommand createRowsCompletedTetrisControllerCommand() {
        return tetris -> soundService.playSound(Sound.ROWS_COMPLETED);
    }
}
