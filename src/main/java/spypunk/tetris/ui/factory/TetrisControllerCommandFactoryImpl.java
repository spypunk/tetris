/*
 * Copyright © 2016-2017 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.factory;

import javax.inject.Inject;
import javax.inject.Singleton;

import spypunk.tetris.guice.TetrisModule.TetrisProvider;
import spypunk.tetris.model.Movement;
import spypunk.tetris.model.Tetris;
import spypunk.tetris.model.Tetris.State;
import spypunk.tetris.service.TetrisService;
import spypunk.tetris.sound.Sound;
import spypunk.tetris.sound.service.SoundService;
import spypunk.tetris.ui.controller.command.TetrisControllerCommand;
import spypunk.tetris.ui.view.TetrisView;

@Singleton
public class TetrisControllerCommandFactoryImpl implements TetrisControllerCommandFactory {

    private final TetrisService tetrisService;

    private final SoundService soundService;

    private final Tetris tetris;

    private final TetrisView tetrisView;

    @Inject
    public TetrisControllerCommandFactoryImpl(final TetrisService tetrisService,
            final SoundService soundService,
            @TetrisProvider final Tetris tetris,
            final TetrisView tetrisView) {
        this.tetrisService = tetrisService;
        this.soundService = soundService;
        this.tetris = tetris;
        this.tetrisView = tetrisView;
    }

    @Override
    public TetrisControllerCommand createNewGameTetrisControllerCommand() {
        return () -> {
            tetrisService.start();
            soundService.playMusic(Sound.BACKGROUND);
        };
    }

    @Override
    public TetrisControllerCommand createPauseTetrisControllerCommand() {
        return () -> {
            tetrisService.pause();

            final State state = tetris.getState();

            if (State.PAUSED.equals(state)) {
                soundService.pauseMusic();
            } else if (State.RUNNING.equals(state)) {
                soundService.resumeMusic();
            }
        };
    }

    @Override
    public TetrisControllerCommand createMoveTetrisControllerCommand(final Movement movement) {
        return () -> tetrisService.move(movement);
    }

    @Override
    public TetrisControllerCommand createShapeLockedTetrisControllerCommand() {
        return () -> soundService.playSound(Sound.SHAPE_LOCKED);
    }

    @Override
    public TetrisControllerCommand createMuteTetrisControllerCommand() {
        return () -> {
            tetrisService.mute();

            final boolean muted = tetris.isMuted();

            tetrisView.setMuted(muted);
            soundService.setMuted(muted);
        };
    }

    @Override
    public TetrisControllerCommand createGameOverTetrisControllerCommand() {
        return () -> soundService.playMusic(Sound.GAME_OVER);
    }

    @Override
    public TetrisControllerCommand createRowsCompletedTetrisControllerCommand() {
        return () -> soundService.playSound(Sound.ROWS_COMPLETED);
    }

    @Override
    public TetrisControllerCommand createIncreaseVolumeTetrisControllerCommand() {
        return soundService::increaseVolume;
    }

    @Override
    public TetrisControllerCommand createDecreaseVolumeTetrisControllerCommand() {
        return soundService::decreaseVolume;
    }

    @Override
    public TetrisControllerCommand createHardDropTetrisControllerCommand() {
        return tetrisService::hardDrop;
    }
}
