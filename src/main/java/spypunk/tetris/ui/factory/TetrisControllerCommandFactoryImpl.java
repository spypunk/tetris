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
import spypunk.tetris.service.TetrisService;
import spypunk.tetris.sound.Sound;
import spypunk.tetris.sound.service.SoundService;
import spypunk.tetris.ui.controller.TetrisController;
import spypunk.tetris.ui.controller.command.TetrisControllerCommand;

@Singleton
public class TetrisControllerCommandFactoryImpl implements TetrisControllerCommandFactory {

    private final TetrisService tetrisService;

    private final SoundService soundService;

    private final TetrisController tetrisController;

    @Inject
    public TetrisControllerCommandFactoryImpl(final TetrisService tetrisService, final SoundService soundService,
            final TetrisController tetrisController) {
        this.tetrisService = tetrisService;
        this.soundService = soundService;
        this.tetrisController = tetrisController;
    }

    @Override
    public TetrisControllerCommand createNewGameTetrisControllerCommand() {
        return tetris -> {

            final TetrisInstance tetrisInstance = tetris.getTetrisInstance();

            if (!State.NEW.equals(tetrisInstance.getState())) {
                tetrisService.newInstance(tetris);
            }

            tetrisService.startInstance(tetris);

            soundService.playMusic(Sound.BACKGROUND);

            tetrisController.getTetrisView().onTetrisInstanceChanged(tetris);
        };
    }

    @Override
    public TetrisControllerCommand createPauseTetrisControllerCommand() {
        return tetris -> {
            tetrisService.pauseInstance(tetris);

            final State state = tetris.getTetrisInstance().getState();

            if (State.RUNNING.equals(state) || State.PAUSED.equals(state)) {
                soundService.pauseMusic();
            }

            tetrisController.getTetrisView().onTetrisInstanceGridChanged();
        };
    }

    @Override
    public TetrisControllerCommand createMovementTetrisControllerCommand(final Movement movement) {
        return tetris -> tetrisService.updateInstanceMovement(tetris, movement);
    }

    @Override
    public TetrisControllerCommand createShapeLockedTetrisControllerCommand() {
        return tetris -> soundService.playSound(Sound.SHAPE_LOCKED);
    }

    @Override
    public TetrisControllerCommand createMuteTetrisControllerCommand() {
        return tetris -> {
            soundService.mute();
            tetrisController.getTetrisView().onMuteChanged(soundService.isMute());
        };
    }

    @Override
    public TetrisControllerCommand createGameOverTetrisControllerCommand() {
        return tetris -> {
            soundService.playMusic(Sound.GAME_OVER);
            tetrisController.getTetrisView().onTetrisInstanceGridChanged();
        };
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
    public TetrisControllerCommand createNewShapeTetrisControllerCommand() {
        return tetris -> tetrisController.getTetrisView().onTetrisInstanceGridChanged();
    }

    @Override
    public TetrisControllerCommand createShapeMovedTetrisControllerCommand() {
        return tetris -> tetrisController.getTetrisView().onTetrisInstanceGridChanged();
    }

    @Override
    public TetrisControllerCommand createLevelUpdatedTetrisControllerCommand() {
        return tetris -> tetrisController.getTetrisView().onTetrisInstanceInfoChanged();
    }

    @Override
    public TetrisControllerCommand createScoreUpdatedTetrisControllerCommand() {
        return tetris -> tetrisController.getTetrisView().onTetrisInstanceInfoChanged();
    }

    @Override
    public TetrisControllerCommand createRowsUpdatedTetrisControllerCommand() {
        return tetris -> tetrisController.getTetrisView().onTetrisInstanceInfoChanged();
    }

    @Override
    public TetrisControllerCommand createNextShapeUpdatedTetrisControllerCommand() {
        return tetris -> tetrisController.getTetrisView().onTetrisInstanceInfoChanged();
    }

    @Override
    public TetrisControllerCommand createStatisticsUpdatedTetrisControllerCommand() {
        return tetris -> tetrisController.getTetrisView().onTetrisInstanceStatisticsChanged();
    }
}
