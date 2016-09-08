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
import spypunk.tetris.model.TetrisInstance;
import spypunk.tetris.model.TetrisInstance.State;
import spypunk.tetris.service.TetrisInstanceService;
import spypunk.tetris.sound.Sound;
import spypunk.tetris.sound.service.SoundService;
import spypunk.tetris.ui.controller.TetrisController;
import spypunk.tetris.ui.controller.command.TetrisControllerCommand;

@Singleton
public class TetrisControllerCommandFactoryImpl implements TetrisControllerCommandFactory {

    private final TetrisInstanceService tetrisInstanceService;

    private final SoundService soundService;

    private final TetrisController tetrisController;

    @Inject
    public TetrisControllerCommandFactoryImpl(final TetrisInstanceService tetrisInstanceService,
            final SoundService soundService,
            final TetrisController tetrisController) {
        this.tetrisInstanceService = tetrisInstanceService;
        this.soundService = soundService;
        this.tetrisController = tetrisController;
    }

    @Override
    public TetrisControllerCommand createNewGameTetrisControllerCommand() {
        return tetris -> {
            tetrisInstanceService.create(tetris);

            soundService.playMusic(Sound.BACKGROUND);
        };
    }

    @Override
    public TetrisControllerCommand createPauseTetrisControllerCommand() {
        return tetris -> {
            final TetrisInstance tetrisInstance = tetris.getTetrisInstance();

            tetrisInstanceService.pause(tetrisInstance);

            final State state = tetrisInstance.getState();

            if (State.RUNNING.equals(state) || State.PAUSED.equals(state)) {
                soundService.pauseMusic();
            }
        };
    }

    @Override
    public TetrisControllerCommand createMovementTetrisControllerCommand(final Movement movement) {
        return tetris -> tetrisInstanceService.triggerMovement(tetris.getTetrisInstance(), movement);
    }

    @Override
    public TetrisControllerCommand createShapeLockedTetrisControllerCommand() {
        return tetris -> soundService.playSound(Sound.SHAPE_LOCKED);
    }

    @Override
    public TetrisControllerCommand createMuteTetrisControllerCommand() {
        return tetris -> {
            soundService.mute();
            tetrisController.getTetrisView().setMute(soundService.isMute());
        };
    }

    @Override
    public TetrisControllerCommand createGameOverTetrisControllerCommand() {
        return tetris -> soundService.playMusic(Sound.GAME_OVER);
    }

    @Override
    public TetrisControllerCommand createRowsCompletedTetrisControllerCommand() {
        return tetris -> soundService.playSound(Sound.ROWS_COMPLETED);
    }

    @Override
    public TetrisControllerCommand createIncreaseVolumeTetrisControllerCommand() {
        return tetris -> soundService.increaseVolume();
    }

    @Override
    public TetrisControllerCommand createDecreaseVolumeTetrisControllerCommand() {
        return tetris -> soundService.decreaseVolume();
    }

    @Override
    public TetrisControllerCommand createHardDropTetrisControllerCommand() {
        return tetris -> tetrisInstanceService.triggerHardDrop(tetris.getTetrisInstance());
    }
}
